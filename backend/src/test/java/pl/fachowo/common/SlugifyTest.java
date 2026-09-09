package pl.fachowo.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SlugifyTest {

    @Test
    void transliteratesPolishCharacters() {
        assertThat(Slugify.slugify("Hydraulika Kowalski")).isEqualTo("hydraulika-kowalski");
        assertThat(Slugify.slugify("Łódź")).isEqualTo("lodz");
        assertThat(Slugify.slugify("Świętokrzyskie")).isEqualTo("swietokrzyskie");
    }

    @Test
    void returnsEmptyForBlank() {
        assertThat(Slugify.slugify(null)).isEmpty();
        assertThat(Slugify.slugify("   ")).isEmpty();
    }
}
