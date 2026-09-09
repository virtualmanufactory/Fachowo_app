package pl.fachowo.company.dto;

public record UpdateCompanyRequest(
        String description,
        String address,
        Boolean servesCustomersAtHome,
        String phone,
        String email,
        String website,
        Boolean available
) {
}
