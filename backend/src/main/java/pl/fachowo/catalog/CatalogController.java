package pl.fachowo.catalog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.fachowo.catalog.dto.CategoryDto;
import pl.fachowo.catalog.dto.CityDto;
import pl.fachowo.catalog.dto.CompareResponse;
import pl.fachowo.catalog.dto.SearchHitDto;
import pl.fachowo.catalog.dto.VoivodeshipDto;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/categories")
    public List<CategoryDto> categories() {
        return catalogService.categories();
    }

    @GetMapping("/voivodeships")
    public List<VoivodeshipDto> voivodeships() {
        return catalogService.voivodeships();
    }

    @GetMapping("/voivodeships/{slug}/cities")
    public List<CityDto> cities(@PathVariable String slug) {
        return catalogService.cities(slug);
    }

    @GetMapping("/compare")
    public CompareResponse compare(
            @RequestParam String category,
            @RequestParam String voivodeship,
            @RequestParam String city
    ) {
        return catalogService.compare(category, voivodeship, city);
    }

    @GetMapping("/search")
    public List<SearchHitDto> search(@RequestParam String q) {
        return catalogService.search(q);
    }
}
