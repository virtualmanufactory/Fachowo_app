package pl.fachowo.verification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.fachowo.common.BadRequestException;
import pl.fachowo.common.NotFoundException;
import pl.fachowo.verification.dto.RegistryCompanyData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificationServiceTest {

    @Mock
    private MfWhiteListClient mfWhiteListClient;
    @Mock
    private CeidgClient ceidgClient;

    @Test
    void prefersCeidgWhenEnabled() {
        when(ceidgClient.enabled()).thenReturn(true);
        when(ceidgClient.lookup("5252345178")).thenReturn(Optional.of(new RegistryCompanyData(
                "5252345178", "CEIDG Firma", null, null, "AKTYWNY", "CEIDG"
        )));
        VerificationService service = new VerificationService(mfWhiteListClient, ceidgClient, true);

        RegistryCompanyData data = service.requireLookup("525-234-51-78");

        assertThat(data.name()).isEqualTo("CEIDG Firma");
        assertThat(data.source()).isEqualTo("CEIDG");
    }

    @Test
    void fallsBackToStubWhenRegistriesEmpty() {
        when(ceidgClient.enabled()).thenReturn(false);
        when(mfWhiteListClient.lookup("5252345178")).thenReturn(Optional.empty());
        VerificationService service = new VerificationService(mfWhiteListClient, ceidgClient, true);

        RegistryCompanyData data = service.requireLookup("5252345178");

        assertThat(data.source()).isEqualTo("STUB");
        assertThat(data.name()).contains("5252345178");
    }

    @Test
    void throwsWhenNipInvalid() {
        VerificationService service = new VerificationService(mfWhiteListClient, ceidgClient, true);
        assertThatThrownBy(() -> service.lookup("123"))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void throwsWhenNotFoundAndStubDisabled() {
        when(ceidgClient.enabled()).thenReturn(false);
        when(mfWhiteListClient.lookup("5252345178")).thenReturn(Optional.empty());
        VerificationService service = new VerificationService(mfWhiteListClient, ceidgClient, false);

        assertThatThrownBy(() -> service.requireLookup("5252345178"))
                .isInstanceOf(NotFoundException.class);
    }
}
