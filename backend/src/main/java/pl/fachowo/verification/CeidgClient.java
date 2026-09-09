package pl.fachowo.verification;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import pl.fachowo.verification.dto.RegistryCompanyData;

import java.util.Optional;

@Component
public class CeidgClient {

    private static final Logger log = LoggerFactory.getLogger(CeidgClient.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String token;

    public CeidgClient(
            RestClient restClient,
            ObjectMapper objectMapper,
            @Value("${app.registry.ceidg-token:}") String token
    ) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.token = token;
    }

    public boolean enabled() {
        return StringUtils.hasText(token);
    }

    public Optional<RegistryCompanyData> lookup(String nip) {
        if (!enabled()) {
            return Optional.empty();
        }
        try {
            String body = restClient.get()
                    .uri("https://dane.biznes.gov.pl/api/ceidg/v3/firmy?nip=" + nip)
                    .header("Authorization", "Bearer " + token)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(String.class);
            if (body == null || body.isBlank()) {
                return Optional.empty();
            }
            JsonNode root = objectMapper.readTree(body);
            JsonNode firm = root.path("firmy").isArray() && !root.path("firmy").isEmpty()
                    ? root.path("firmy").get(0)
                    : root.path("firma");
            if (firm.isMissingNode() || firm.isNull() || firm.isEmpty()) {
                return Optional.empty();
            }
            JsonNode address = firm.path("adresDzialalnosci");
            String addressLine = String.join(" ",
                    address.path("ulica").asText(""),
                    address.path("budynek").asText(""),
                    address.path("miasto").asText("")
            ).trim();
            return Optional.of(new RegistryCompanyData(
                    nip,
                    firm.path("nazwa").asText(null),
                    firm.path("wlasciciel").path("regon").asText(null),
                    addressLine.isBlank() ? null : addressLine,
                    firm.path("status").asText(null),
                    VerificationSource.CEIDG.name()
            ));
        } catch (RestClientException | java.io.IOException ex) {
            log.warn("CEIDG lookup failed for {}: {}", nip, ex.getMessage());
            return Optional.empty();
        }
    }
}
