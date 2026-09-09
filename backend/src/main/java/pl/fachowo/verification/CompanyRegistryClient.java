package pl.fachowo.verification;

import pl.fachowo.verification.dto.RegistryCompanyData;

import java.util.Optional;

public interface CompanyRegistryClient {
    Optional<RegistryCompanyData> lookup(String nip);
}
