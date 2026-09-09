package pl.fachowo;

import org.junit.jupiter.api.Test;
import pl.fachowo.verification.NipValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NipValidatorTest {

    @Test
    void acceptsValidNip() {
        assertTrue(NipValidator.isValid("5252345178"));
    }

    @Test
    void rejectsInvalidChecksum() {
        assertFalse(NipValidator.isValid("5252345170"));
    }
}
