package pl.fachowo.company;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.fachowo.catalog.Category;
import pl.fachowo.catalog.CategoryRepository;
import pl.fachowo.catalog.City;
import pl.fachowo.catalog.CityRepository;
import pl.fachowo.common.BadRequestException;
import pl.fachowo.common.ConflictException;
import pl.fachowo.common.NotFoundException;
import pl.fachowo.common.Slugify;
import pl.fachowo.company.dto.CompanyProfileDto;
import pl.fachowo.company.dto.CreateCompanyRequest;
import pl.fachowo.company.dto.CreateServiceRequest;
import pl.fachowo.company.dto.ImageDto;
import pl.fachowo.company.dto.ServiceDto;
import pl.fachowo.company.dto.UpdateCompanyRequest;
import pl.fachowo.security.UserPrincipal;
import pl.fachowo.storage.StorageService;
import pl.fachowo.user.User;
import pl.fachowo.user.UserRepository;
import pl.fachowo.verification.CompanyVerification;
import pl.fachowo.verification.CompanyVerificationRepository;
import pl.fachowo.verification.NipValidator;
import pl.fachowo.verification.VerificationService;
import pl.fachowo.verification.VerificationSource;
import pl.fachowo.verification.VerificationStatus;
import pl.fachowo.verification.dto.RegistryCompanyData;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ServiceOfferRepository serviceOfferRepository;
    private final CompanyImageRepository companyImageRepository;
    private final CompanyVerificationRepository verificationRepository;
    private final CategoryRepository categoryRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;
    private final VerificationService verificationService;
    private final StorageService storageService;
    private final CacheManager cacheManager;

    public CompanyService(
            CompanyRepository companyRepository,
            ServiceOfferRepository serviceOfferRepository,
            CompanyImageRepository companyImageRepository,
            CompanyVerificationRepository verificationRepository,
            CategoryRepository categoryRepository,
            CityRepository cityRepository,
            UserRepository userRepository,
            VerificationService verificationService,
            StorageService storageService,
            CacheManager cacheManager
    ) {
        this.companyRepository = companyRepository;
        this.serviceOfferRepository = serviceOfferRepository;
        this.companyImageRepository = companyImageRepository;
        this.verificationRepository = verificationRepository;
        this.categoryRepository = categoryRepository;
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
        this.verificationService = verificationService;
        this.storageService = storageService;
        this.cacheManager = cacheManager;
    }

    @Transactional(readOnly = true)
    public CompanyProfileDto getBySlug(String slug) {
        Company company = companyRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono firmy"));
        return toProfile(company);
    }

    @Transactional(readOnly = true)
    public CompanyProfileDto getMine(UserPrincipal principal) {
        Company company = companyRepository.findByOwnerId(principal.id())
                .orElseThrow(() -> new NotFoundException("Nie masz jeszcze wizytówki"));
        return toProfile(company);
    }

    @Transactional
    public CompanyProfileDto create(UserPrincipal principal, CreateCompanyRequest request) {
        if (companyRepository.existsByOwnerId(principal.id())) {
            throw new ConflictException("Możesz dodać tylko jedną firmę");
        }
        String nip = NipValidator.normalize(request.nip());
        if (!NipValidator.isValid(nip)) {
            throw new BadRequestException("Nieprawidłowy numer NIP");
        }
        if (companyRepository.existsByNip(nip)) {
            throw new ConflictException("Firma z tym NIP już istnieje");
        }
        RegistryCompanyData registry = verificationService.requireLookup(nip);
        User owner = userRepository.findById(principal.id())
                .orElseThrow(() -> new NotFoundException("Nie znaleziono użytkownika"));
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException("Nie znaleziono kategorii"));
        City city = cityRepository.findById(request.cityId())
                .orElseThrow(() -> new NotFoundException("Nie znaleziono miasta"));

        Company company = new Company();
        company.setOwner(owner);
        company.setCategory(category);
        company.setCity(city);
        company.setName(request.name().trim());
        company.setSlug(uniqueSlug(request.name()));
        company.setNip(nip);
        company.setDescription(request.description());
        company.setAddress(firstNonBlank(request.address(), registry.address()));
        company.setServesCustomersAtHome(request.servesCustomersAtHome());
        company.setPhone(request.phone());
        company.setEmail(firstNonBlank(request.email(), owner.getEmail()));
        company.setWebsite(request.website());
        company.setVerified(true);
        company.setAvailable(request.available());
        companyRepository.save(company);

        CompanyVerification verification = new CompanyVerification();
        verification.setCompany(company);
        verification.setNip(nip);
        verification.setSource(VerificationSource.valueOf(registry.source()));
        verification.setStatus(VerificationStatus.VERIFIED);
        verification.setPayload(registry.name());
        verificationRepository.save(verification);

        if (request.servicePrice() != null && request.serviceName() != null && !request.serviceName().isBlank()) {
            ServiceOffer offer = new ServiceOffer();
            offer.setCompany(company);
            offer.setName(request.serviceName());
            offer.setPrice(request.servicePrice());
            offer.setUnit(request.serviceUnit() == null || request.serviceUnit().isBlank() ? "wizyta" : request.serviceUnit());
            offer.setAvailable(request.available());
            serviceOfferRepository.save(offer);
        }

        evictCompareCache();
        return toProfile(company);
    }

    @Transactional
    public CompanyProfileDto update(UUID companyId, UserPrincipal principal, UpdateCompanyRequest request) {
        Company company = ownedCompany(companyId, principal);
        if (request.description() != null) {
            company.setDescription(request.description());
        }
        if (request.address() != null) {
            company.setAddress(request.address());
        }
        if (request.servesCustomersAtHome() != null) {
            company.setServesCustomersAtHome(request.servesCustomersAtHome());
        }
        if (request.phone() != null) {
            company.setPhone(request.phone());
        }
        if (request.email() != null) {
            company.setEmail(request.email());
        }
        if (request.website() != null) {
            company.setWebsite(request.website());
        }
        if (request.available() != null) {
            company.setAvailable(request.available());
        }
        evictCompareCache();
        return toProfile(company);
    }

    @Transactional
    public ServiceDto addService(UUID companyId, UserPrincipal principal, CreateServiceRequest request) {
        Company company = ownedCompany(companyId, principal);
        ServiceOffer offer = new ServiceOffer();
        offer.setCompany(company);
        offer.setName(request.name());
        offer.setPrice(request.price());
        offer.setUnit(request.unit());
        offer.setAvailable(request.available());
        serviceOfferRepository.save(offer);
        evictCompareCache();
        return new ServiceDto(offer.getId(), offer.getName(), offer.getPrice(), offer.getUnit(), offer.isAvailable());
    }

    @Transactional
    public ImageDto addImage(UUID companyId, UserPrincipal principal, MultipartFile file) {
        Company company = ownedCompany(companyId, principal);
        String key = storageService.uploadCompanyImage(company.getId(), file);
        CompanyImage image = new CompanyImage();
        image.setCompany(company);
        image.setObjectKey(key);
        image.setContentType(file.getContentType());
        companyImageRepository.save(image);
        return new ImageDto(image.getId(), storageService.publicUrl(key));
    }

    private Company ownedCompany(UUID companyId, UserPrincipal principal) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono firmy"));
        if (!company.getOwner().getId().equals(principal.id())) {
            throw new BadRequestException("Brak dostępu do tej firmy");
        }
        return company;
    }

    private String uniqueSlug(String name) {
        String base = Slugify.slugify(name);
        if (base.isBlank()) {
            base = "firma";
        }
        String slug = base;
        int i = 2;
        while (companyRepository.existsBySlug(slug)) {
            slug = base + "-" + i++;
        }
        return slug;
    }

    private CompanyProfileDto toProfile(Company company) {
        List<ServiceDto> services = serviceOfferRepository.findByCompanyId(company.getId()).stream()
                .map(s -> new ServiceDto(s.getId(), s.getName(), s.getPrice(), s.getUnit(), s.isAvailable()))
                .toList();
        List<ImageDto> images = companyImageRepository.findByCompanyId(company.getId()).stream()
                .map(img -> new ImageDto(img.getId(), storageService.publicUrl(img.getObjectKey())))
                .toList();
        return new CompanyProfileDto(
                company.getId(),
                company.getSlug(),
                company.getName(),
                company.getNip(),
                company.getDescription(),
                company.getCategory().getSlug(),
                company.getCategory().getName(),
                company.getCity().getVoivodeship().getSlug(),
                company.getCity().getVoivodeship().getName(),
                company.getCity().getSlug(),
                company.getCity().getName(),
                company.getAddress(),
                company.getLatitude(),
                company.getLongitude(),
                company.isServesCustomersAtHome(),
                company.getPhone(),
                company.getEmail(),
                company.getWebsite(),
                company.isVerified(),
                company.isAvailable(),
                company.getCreatedAt(),
                services,
                images
        );
    }

    private void evictCompareCache() {
        var cache = cacheManager.getCache("compare");
        if (cache != null) {
            cache.clear();
        }
    }

    private static String firstNonBlank(String first, String second) {
        if (first != null && !first.isBlank()) {
            return first;
        }
        return second;
    }
}
