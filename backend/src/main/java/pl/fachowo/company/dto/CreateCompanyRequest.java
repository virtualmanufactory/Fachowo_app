package pl.fachowo.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateCompanyRequest(
        @NotBlank @Size(min = 10, max = 10) String nip,
        @NotBlank String name,
        String description,
        @NotNull UUID categoryId,
        @NotNull UUID cityId,
        String address,
        boolean servesCustomersAtHome,
        @NotBlank String phone,
        String email,
        String website,
        boolean available,
        String serviceName,
        BigDecimal servicePrice,
        String serviceUnit
) {
}
