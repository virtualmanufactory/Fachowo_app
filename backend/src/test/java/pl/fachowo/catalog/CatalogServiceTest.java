package pl.fachowo.catalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.fachowo.common.NotFoundException;
import pl.fachowo.company.CompanyRepository;
import pl.fachowo.company.ServiceOfferRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private VoivodeshipRepository voivodeshipRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private ServiceOfferRepository serviceOfferRepository;

    private CatalogService catalogService;

    @BeforeEach
    void setUp() {
        catalogService = new CatalogService(
                categoryRepository, voivodeshipRepository, cityRepository, companyRepository, serviceOfferRepository
        );
    }

    @Test
    void compareThrowsWhenCityMissing() {
        when(categoryRepository.findBySlug("hydraulika")).thenReturn(Optional.of(new Category()));
        when(voivodeshipRepository.findBySlug("wielkopolskie")).thenReturn(Optional.of(new Voivodeship()));
        when(cityRepository.findByVoivodeship_SlugAndSlug("wielkopolskie", "brak")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> catalogService.compare("hydraulika", "wielkopolskie", "brak"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("miasta");
    }

    @Test
    void searchReturnsEmptyForBlankQuery() {
        assertThat(catalogService.search("  ")).isEmpty();
        assertThat(catalogService.search(null)).isEmpty();
    }

    @Test
    void searchFindsCategoryByName() {
        Category category = new Category();
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        set(category, "slug", "hydraulika");
        set(category, "name", "Hydraulika");
        when(cityRepository.findAll()).thenReturn(List.of());
        when(companyRepository.search("hydra")).thenReturn(List.of());

        assertThat(catalogService.search("hydra"))
                .singleElement()
                .satisfies(hit -> {
                    assertThat(hit.type()).isEqualTo("category");
                    assertThat(hit.path()).isEqualTo("/kategoria/hydraulika");
                });
    }

    private static void set(Object target, String field, Object value) {
        try {
            var declared = target.getClass().getDeclaredField(field);
            declared.setAccessible(true);
            declared.set(target, value);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
