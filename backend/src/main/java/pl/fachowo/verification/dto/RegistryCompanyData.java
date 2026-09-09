package pl.fachowo.verification.dto;

public record RegistryCompanyData(
        String nip,
        String name,
        String regon,
        String address,
        String status,
        String source
) {
}
