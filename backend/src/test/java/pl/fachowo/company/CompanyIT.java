package pl.fachowo.company;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.fachowo.AbstractIT;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CompanyIT extends AbstractIT {

    private static final JsonMapper JSON = JsonMapper.builder().build();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void verifiesNipAndPublishesBusinessCard() throws Exception {
        String token = register("firma-it@fachowo.pl");

        mockMvc.perform(post("/api/verifications/nip")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nip":"1234563218"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nip").value("1234563218"))
                .andExpect(jsonPath("$.source").value("STUB"));

        mockMvc.perform(post("/api/companies")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nip":"1234563218",
                                  "name":"Hydraulika Nowa",
                                  "description":"Nowa oferta testowa",
                                  "categoryId":"11111111-1111-1111-1111-111111111001",
                                  "cityId":"33333333-3333-3333-3333-333333333001",
                                  "address":"ul. Testowa 1",
                                  "servesCustomersAtHome":false,
                                  "phone":"600000001",
                                  "email":"firma-it@fachowo.pl",
                                  "website":null,
                                  "available":true,
                                  "serviceName":"Wizyta hydraulika",
                                  "servicePrice":150,
                                  "serviceUnit":"wizyta"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slug").value("hydraulika-nowa"))
                .andExpect(jsonPath("$.verified").value(true))
                .andExpect(jsonPath("$.services[0].price").value(150));

        mockMvc.perform(get("/api/companies/hydraulika-nowa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hydraulika Nowa"));

        mockMvc.perform(post("/api/companies")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nip":"8510001238",
                                  "name":"Druga Firma",
                                  "categoryId":"11111111-1111-1111-1111-111111111001",
                                  "cityId":"33333333-3333-3333-3333-333333333001",
                                  "servesCustomersAtHome":false,
                                  "phone":"600000002",
                                  "available":true
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void nipLookupRequiresLogin() throws Exception {
        mockMvc.perform(post("/api/verifications/nip")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nip":"1234563218"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    private String register(String email) throws Exception {
        String body = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"haslo1234\"}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode json = JSON.readTree(body);
        return json.get("token").asText();
    }
}
