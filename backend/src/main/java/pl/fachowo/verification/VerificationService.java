package pl.fachowo.verification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.fachowo.common.BadRequestException;
import pl.fachowo.common.NotFoundException;
import pl.fachowo.verification.dto.RegistryCompanyData;

import java.util.Optional;

@Service
public class VerificationService implements CompanyRegistryClient {

    private final MfWhiteListClient mfWhiteListClient;
    private final CeidgClient ceidgClient;
    private final boolean stubEnabled;

    public VerificationService(
            MfWhiteListClient mfWhiteListClient,
            CeidgClient ceidgClient,
            @Value("${app.registry.stub:true}") boolean stubEnabled
    ) {
        this.mfWhiteListClient = mfWhiteListClient;
        this.ceidgClient = ceidgClient;
        this.stubEnabled = stubEnabled;
    }

    @Override
    @Cacheable(value = "nip-lookup", key = "#nip")
    public Optional<RegistryCompanyData> lookup(String nip) {
        String normalized = NipValidator.normalize(nip);
        if (!NipValidator.isValid(normalized)) {
            throw new BadRequestException("Nieprawidłowy numer NIP");
        }
        if (ceidgClient.enabled()) {
            Optional<RegistryCompanyData> ceidg = ceidgClient.lookup(normalized);
            if (ceidg.isPresent()) {
                return ceidg;
            }
        }
        Optional<RegistryCompanyData> mf = mfWhiteListClient.lookup(normalized);
        if (mf.isPresent()) {
            return mf;
        }
        if (stubEnabled) {
            return Optional.of(new RegistryCompanyData(
                    normalized,
                    "Firma " + normalized,
                    null,
                    null,
                    "STUB",
                    VerificationSource.STUB.name()
            ));
        }
        return Optional.empty();
    }

    public RegistryCompanyData requireLookup(String nip) {
        return lookup(nip).orElseThrow(() -> new NotFoundException("Nie znaleziono firmy o podanym NIP"));
    }
}
