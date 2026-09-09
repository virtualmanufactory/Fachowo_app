package pl.fachowo.catalog;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fachowo.catalog.dto.CategoryDto;
import pl.fachowo.catalog.dto.CityDto;
import pl.fachowo.catalog.dto.CompareResponse;
import pl.fachowo.catalog.dto.CompareRowDto;
import pl.fachowo.catalog.dto.SearchHitDto;
import pl.fachowo.catalog.dto.VoivodeshipDto;
import pl.fachowo.common.NotFoundException;
import pl.fachowo.company.Company;
import pl.fachowo.company.CompanyRepository;
import pl.fachowo.company.ServiceOffer;
import pl.fachowo.company.ServiceOfferRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private final CategoryRepository categoryRepository;
    private final VoivodeshipRepository voivodeshipRepository;
    private final CityRepository cityRepository;
    private final CompanyRepository companyRepository;
    private final ServiceOfferRepository serviceOfferRepository;

    public CatalogService(
            CategoryRepository categoryRepository,
            VoivodeshipRepository voivodeshipRepository,
            CityRepository cityRepository,
            CompanyRepository companyRepository,
            ServiceOfferRepository serviceOfferRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.voivodeshipRepository = voivodeshipRepository;
        this.cityRepository = cityRepository;
        this.companyRepository = companyRepository;
        this.serviceOfferRepository = serviceOfferRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "categories", unless = "#result == null || #result.isEmpty()")
    public List<CategoryDto> categories() {
        return new ArrayList<>(categoryRepository.findAll().stream()
                .sorted(Comparator.comparing(Category::getName))
                .map(c -> new CategoryDto(c.getId(), c.getSlug(), c.getName()))
                .toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "voivodeships", unless = "#result == null || #result.isEmpty()")
    public List<VoivodeshipDto> voivodeships() {
        return new ArrayList<>(voivodeshipRepository.findAll().stream()
                .sorted(Comparator.comparing(Voivodeship::getName))
                .map(v -> new VoivodeshipDto(v.getId(), v.getSlug(), v.getName()))
                .toList());
    }

    @Transactional(readOnly = true)
    public List<CityDto> cities(String voivodeshipSlug) {
        voivodeshipRepository.findBySlug(voivodeshipSlug)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono województwa"));
        return cityRepository.findByVoivodeship_SlugOrderByNameAsc(voivodeshipSlug).stream()
                .map(c -> new CityDto(c.getId(), c.getSlug(), c.getName(), voivodeshipSlug))
                .toList();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "compare", key = "#category + '-' + #voivodeship + '-' + #city")
    public CompareResponse compare(String category, String voivodeship, String city) {
        Category cat = categoryRepository.findBySlug(category)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono kategorii"));
        Voivodeship voi = voivodeshipRepository.findBySlug(voivodeship)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono województwa"));
        City cityEntity = cityRepository.findByVoivodeship_SlugAndSlug(voivodeship, city)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono miasta"));

        List<Company> companies = companyRepository.findForCompare(category, voivodeship, city);
        List<UUID> ids = companies.stream().map(Company::getId).toList();
        Map<UUID, List<ServiceOffer>> services = ids.isEmpty()
                ? Map.of()
                : serviceOfferRepository.findByCompany_IdIn(ids).stream()
                .collect(Collectors.groupingBy(s -> s.getCompany().getId()));

        List<CompareRowDto> rows = companies.stream()
                .map(company -> toRow(company, services.getOrDefault(company.getId(), List.of())))
                .sorted(Comparator
                        .comparing((CompareRowDto row) -> !row.city().equalsIgnoreCase(cityEntity.getName()))
                        .thenComparing(row -> row.price() == null ? BigDecimal.valueOf(Long.MAX_VALUE) : row.price()))
                .toList();

        return new CompareResponse(cat.getName(), voi.getName(), cityEntity.getName(), rows);
    }

    @Transactional(readOnly = true)
    public List<SearchHitDto> search(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        String q = query.trim();
        List<SearchHitDto> hits = new java.util.ArrayList<>();
        categoryRepository.findAll().stream()
                .filter(c -> c.getName().toLowerCase().contains(q.toLowerCase()))
                .forEach(c -> hits.add(new SearchHitDto("category", c.getName(), "/kategoria/" + c.getSlug())));
        cityRepository.findAll().stream()
                .filter(c -> c.getName().toLowerCase().contains(q.toLowerCase()))
                .limit(10)
                .forEach(c -> hits.add(new SearchHitDto(
                        "city",
                        c.getName() + ", " + c.getVoivodeship().getName(),
                        "/kategoria/hydraulika/" + c.getVoivodeship().getSlug() + "/" + c.getSlug()
                )));
        companyRepository.search(q).stream()
                .limit(10)
                .forEach(c -> hits.add(new SearchHitDto("company", c.getName(), "/firma/" + c.getSlug())));
        return hits;
    }

    private CompareRowDto toRow(Company company, List<ServiceOffer> services) {
        ServiceOffer cheapest = services.stream()
                .min(Comparator.comparing(ServiceOffer::getPrice))
                .orElse(null);
        boolean available = company.isAvailable() && (cheapest == null || cheapest.isAvailable());
        return new CompareRowDto(
                company.getId(),
                company.getSlug(),
                company.getName(),
                company.getCity().getName(),
                cheapest == null ? null : cheapest.getPrice(),
                cheapest == null ? null : cheapest.getUnit(),
                available,
                company.isServesCustomersAtHome(),
                company.isVerified()
        );
    }
}
