package pl.fachowo.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fachowo.common.BadRequestException;
import pl.fachowo.common.ConflictException;
import pl.fachowo.company.CompanyRepository;
import pl.fachowo.security.JwtService;
import pl.fachowo.security.UserPrincipal;
import pl.fachowo.user.dto.AuthResponse;
import pl.fachowo.user.dto.ForgotPasswordRequest;
import pl.fachowo.user.dto.ForgotPasswordResponse;
import pl.fachowo.user.dto.LoginRequest;
import pl.fachowo.user.dto.MeResponse;
import pl.fachowo.user.dto.RegisterRequest;
import pl.fachowo.user.dto.ResetPasswordRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;

@Service
public class AuthService {

    private static final String RESET_MESSAGE = "Jeśli konto istnieje, możesz ustawić nowe hasło.";

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final boolean exposeResetToken;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthService(
            UserRepository userRepository,
            CompanyRepository companyRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            @Value("${app.auth.expose-reset-token:false}") boolean exposeResetToken
    ) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.exposeResetToken = exposeResetToken;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("Konto z tym adresem e-mail już istnieje");
        }
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(user), user.getId(), user.getEmail());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new BadCredentialsException("bad credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("bad credentials");
        }
        return new AuthResponse(jwtService.generateToken(user), user.getId(), user.getEmail());
    }

    @Transactional
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        String email = request.email().trim().toLowerCase();
        User user = userRepository.findByEmailIgnoreCase(email).orElse(null);
        if (user == null) {
            return new ForgotPasswordResponse(RESET_MESSAGE, null);
        }
        passwordResetTokenRepository.deleteByUser_Id(user.getId());
        String rawToken = randomToken();
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setTokenHash(sha256(rawToken));
        token.setExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS));
        passwordResetTokenRepository.save(token);
        return new ForgotPasswordResponse(RESET_MESSAGE, exposeResetToken ? rawToken : null);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken token = passwordResetTokenRepository.findByTokenHash(sha256(request.token().trim()))
                .orElseThrow(() -> new BadRequestException("Link do resetu jest nieprawidłowy lub wygasł"));
        if (token.getUsedAt() != null || token.getExpiresAt().isBefore(Instant.now())) {
            throw new BadRequestException("Link do resetu jest nieprawidłowy lub wygasł");
        }
        token.getUser().setPasswordHash(passwordEncoder.encode(request.password()));
        token.setUsedAt(Instant.now());
    }

    @Transactional(readOnly = true)
    public MeResponse me(UserPrincipal principal) {
        var company = companyRepository.findByOwnerId(principal.id());
        return new MeResponse(
                principal.id(),
                principal.email(),
                principal.role(),
                company.map(c -> c.getId()).orElse(null),
                company.map(c -> c.getSlug()).orElse(null)
        );
    }

    private String randomToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    static String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
