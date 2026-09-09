package pl.fachowo.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import pl.fachowo.AbstractIT;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CatalogIT extends AbstractIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listsSeededCategories() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].slug", hasItem("hydraulika")));
    }

    @Test
    void comparesPlumbersAroundPoznan() throws Exception {
        mockMvc.perform(get("/api/compare")
                        .param("category", "hydraulika")
                        .param("voivodeship", "wielkopolskie")
                        .param("city", "poznan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("Hydraulika"))
                .andExpect(jsonPath("$.cityName").value("Poznań"))
                .andExpect(jsonPath("$.rows", hasSize(3)))
                .andExpect(jsonPath("$.rows[0].name").value("Hydraulika Kowalski"))
                .andExpect(jsonPath("$.rows[0].available").value(true));
    }

    @Test
    void returns404ForUnknownCity() throws Exception {
        mockMvc.perform(get("/api/compare")
                        .param("category", "hydraulika")
                        .param("voivodeship", "wielkopolskie")
                        .param("city", "nieistnieje"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchFindsDemoCompany() throws Exception {
        mockMvc.perform(get("/api/search").param("q", "Kowalski"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("company"))
                .andExpect(jsonPath("$[0].path").value("/firma/hydraulika-kowalski"));
    }

    @Test
    void companyProfileIsPublic() throws Exception {
        mockMvc.perform(get("/api/companies/hydraulika-kowalski"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verified").value(true))
                .andExpect(jsonPath("$.phone").value("600100200"))
                .andExpect(jsonPath("$.services[0].price").value(100.00));
    }
}
