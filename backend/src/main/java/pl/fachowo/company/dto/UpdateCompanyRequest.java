package pl.fachowo.company.dto;

import java.util.UUID;

public record UpdateCompanyRequest(
        String name,
        String description,
        String address,
        Boolean servesCustomersAtHome,
        String phone,
        String email,
        String website,
        Boolean available,
        UUID categoryId,
        UUID cityId
) {
}
