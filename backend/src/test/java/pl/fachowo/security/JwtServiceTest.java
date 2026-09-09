package pl.fachowo.security;

import org.junit.jupiter.api.Test;
import pl.fachowo.support.Users;
import pl.fachowo.user.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService(
            "fachowo-dev-secret-key-must-be-at-least-256-bits-long!!",
            60_000
    );

    @Test
    void roundTripsUserIdAndClaims() {
        User user = Users.withId("kowalski@demo.fachowo.pl");
        String token = jwtService.generateToken(user);

        assertThat(jwtService.userId(token)).isEqualTo(user.getId());
        assertThat(jwtService.parse(token).get("email", String.class)).isEqualTo("kowalski@demo.fachowo.pl");
        assertThat(jwtService.parse(token).get("role", String.class)).isEqualTo("USER");
    }

    @Test
    void rejectsInvalidToken() {
        assertThatThrownBy(() -> jwtService.parse("not-a-valid.jwt.token"))
                .isInstanceOf(RuntimeException.class);
    }
}
