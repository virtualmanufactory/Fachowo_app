package pl.fachowo.verification;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import pl.fachowo.verification.dto.RegistryCompanyData;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class MfWhiteListClient {

    private static final Logger log = LoggerFactory.getLogger(MfWhiteListClient.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public MfWhiteListClient(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public Optional<RegistryCompanyData> lookup(String nip) {
        String date = LocalDate.now().toString();
        String url = "https://wl-api.mf.gov.pl/api/search/nip/" + nip + "?date=" + date;
        try {
            String body = restClient.get()
                    .uri(url)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(String.class);
            if (body == null || body.isBlank()) {
                return Optional.empty();
            }
            JsonNode root = objectMapper.readTree(body);
            JsonNode subject = root.path("result").path("subject");
            if (subject.isMissingNode() || subject.isNull()) {
                return Optional.empty();
            }
            String address = firstNonBlank(subject.path("workingAddress").asText(null), subject.path("residenceAddress").asText(null));
            return Optional.of(new RegistryCompanyData(
                    nip,
                    subject.path("name").asText(null),
                    subject.path("regon").asText(null),
                    address,
                    subject.path("statusVat").asText(null),
                    VerificationSource.MF_WL.name()
            ));
        } catch (RestClientException | java.io.IOException ex) {
            log.warn("MF White List lookup failed for {}: {}", nip, ex.getMessage());
            return Optional.empty();
        }
    }

    private static String firstNonBlank(String first, String second) {
        if (first != null && !first.isBlank()) {
            return first;
        }
        return second;
    }
}
