package pl.fachowo.config;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pl.fachowo.verification.NipValidator;

import static org.assertj.core.api.Assertions.assertThat;

class StageSeedNipTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "2000000001", "2000000018", "2000000024", "2000000030", "2000000047",
            "2000000053", "2000000076", "2000000082", "2000000099", "2000000107",
            "2000000113", "2000000136", "2000000142", "2000000159"
    })
    void stageNipsAreValid(String nip) {
        assertThat(NipValidator.isValid(nip)).isTrue();
    }
}
