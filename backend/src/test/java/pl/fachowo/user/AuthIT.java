package pl.fachowo.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.fachowo.AbstractIT;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthIT extends AbstractIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerLoginAndReadProfile() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"nowa@fachowo.pl","password":"haslo1234"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.email").value("nowa@fachowo.pl"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"nowa@fachowo.pl","password":"haslo1234"}
                                """))
                .andExpect(status().isConflict());

        MvcResult login = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"nowa@fachowo.pl","password":"haslo1234"}
                                """))
                .andExpect(status().isOk())
                .andReturn();
        String token = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(login.getResponse().getContentAsString())
                .get("token").asText();

        mockMvc.perform(get("/api/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("nowa@fachowo.pl"))
                .andExpect(jsonPath("$.companyId").isEmpty());
    }

    @Test
    void loginRejectsBadPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"kowalski@demo.fachowo.pl","password":"zlehaslo"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void meRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/me")).andExpect(status().isForbidden());
    }
}
