package pl.fachowo.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.fachowo.common.ConflictException;
import pl.fachowo.company.CompanyRepository;
import pl.fachowo.security.JwtService;
import pl.fachowo.support.Users;
import pl.fachowo.user.dto.LoginRequest;
import pl.fachowo.user.dto.RegisterRequest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(
                userRepository,
                companyRepository,
                passwordResetTokenRepository,
                passwordEncoder,
                jwtService,
                true
        );
    }

    @Test
    void registerCreatesUserAndReturnsToken() {
        when(userRepository.existsByEmailIgnoreCase("anna@fachowo.pl")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            Users.setId(user, UUID.randomUUID());
            return user;
        });
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        var response = authService.register(new RegisterRequest("  Anna@fachowo.pl ", "secret123"));

        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.email()).isEqualTo("anna@fachowo.pl");
    }

    @Test
    void registerRejectsDuplicateEmail() {
        when(userRepository.existsByEmailIgnoreCase("anna@fachowo.pl")).thenReturn(true);
        assertThatThrownBy(() -> authService.register(new RegisterRequest("anna@fachowo.pl", "secret123")))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void loginRejectsWrongPassword() {
        User user = Users.withId("anna@fachowo.pl");
        user.setPasswordHash("hashed");
        when(userRepository.findByEmailIgnoreCase("anna@fachowo.pl")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("bad", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(new LoginRequest("anna@fachowo.pl", "bad")))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void forgotPasswordExposesTokenForKnownEmail() {
        User user = Users.withId("anna@fachowo.pl");
        when(userRepository.findByEmailIgnoreCase("anna@fachowo.pl")).thenReturn(Optional.of(user));
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = authService.forgotPassword(new pl.fachowo.user.dto.ForgotPasswordRequest("anna@fachowo.pl"));

        assertThat(response.resetToken()).isNotBlank();
        assertThat(response.message()).contains("nowe hasło");
    }

    @Test
    void forgotPasswordHidesMissingAccount() {
        when(userRepository.findByEmailIgnoreCase("brak@fachowo.pl")).thenReturn(Optional.empty());

        var response = authService.forgotPassword(new pl.fachowo.user.dto.ForgotPasswordRequest("brak@fachowo.pl"));

        assertThat(response.resetToken()).isNull();
    }

    @Test
    void resetPasswordUpdatesHash() {
        User user = Users.withId("anna@fachowo.pl");
        user.setPasswordHash("old");
        String raw = "a".repeat(64);
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setTokenHash(AuthService.sha256(raw));
        token.setExpiresAt(java.time.Instant.now().plusSeconds(3600));
        when(passwordResetTokenRepository.findByTokenHash(AuthService.sha256(raw))).thenReturn(Optional.of(token));
        when(passwordEncoder.encode("nowehaslo1")).thenReturn("new-hash");

        authService.resetPassword(new pl.fachowo.user.dto.ResetPasswordRequest(raw, "nowehaslo1"));

        assertThat(user.getPasswordHash()).isEqualTo("new-hash");
        assertThat(token.getUsedAt()).isNotNull();
    }
}
