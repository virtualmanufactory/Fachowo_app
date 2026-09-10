package pl.fachowo.company;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.fachowo.company.dto.CompanyProfileDto;
import pl.fachowo.company.dto.CreateCompanyRequest;
import pl.fachowo.company.dto.CreateServiceRequest;
import pl.fachowo.company.dto.ImageDto;
import pl.fachowo.company.dto.ServiceDto;
import pl.fachowo.company.dto.UpdateCompanyRequest;
import pl.fachowo.company.dto.UpdateServiceRequest;
import pl.fachowo.security.UserPrincipal;

import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/{slug}")
    public CompanyProfileDto get(@PathVariable String slug) {
        return companyService.getBySlug(slug);
    }

    @GetMapping("/me/current")
    public CompanyProfileDto mine(@AuthenticationPrincipal UserPrincipal principal) {
        return companyService.getMine(principal);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyProfileDto create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateCompanyRequest request
    ) {
        return companyService.create(principal, request);
    }

    @PutMapping("/{id}")
    public CompanyProfileDto update(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdateCompanyRequest request
    ) {
        return companyService.update(id, principal, request);
    }

    @PostMapping("/{id}/services")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceDto addService(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateServiceRequest request
    ) {
        return companyService.addService(id, principal, request);
    }

    @PutMapping("/{id}/services/{serviceId}")
    public ServiceDto updateService(
            @PathVariable UUID id,
            @PathVariable UUID serviceId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateServiceRequest request
    ) {
        return companyService.updateService(id, serviceId, principal, request);
    }

    @DeleteMapping("/{id}/services/{serviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteService(
            @PathVariable UUID id,
            @PathVariable UUID serviceId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        companyService.deleteService(id, serviceId, principal);
    }

    @PostMapping("/{id}/images")
    @ResponseStatus(HttpStatus.CREATED)
    public ImageDto addImage(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestPart("file") MultipartFile file
    ) {
        return companyService.addImage(id, principal, file);
    }

    @PutMapping("/{id}/images/{imageId}")
    public ImageDto replaceImage(
            @PathVariable UUID id,
            @PathVariable UUID imageId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestPart("file") MultipartFile file
    ) {
        return companyService.replaceImage(id, imageId, principal, file);
    }

    @DeleteMapping("/{id}/images/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(
            @PathVariable UUID id,
            @PathVariable UUID imageId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        companyService.deleteImage(id, imageId, principal);
    }
}
