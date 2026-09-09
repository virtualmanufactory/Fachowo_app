package pl.fachowo.verification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class NipValidatorTest {

    @Test
    void acceptsValidNipAndStripsFormatting() {
        assertThat(NipValidator.isValid("5252345178")).isTrue();
        assertThat(NipValidator.normalize("525-234-51-78")).isEqualTo("5252345178");
        assertThat(NipValidator.isValid("525-234-51-78")).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"5252345170", "123", "", "abcdefghij"})
    void rejectsInvalidNip(String nip) {
        assertThat(NipValidator.isValid(nip)).isFalse();
    }
}
