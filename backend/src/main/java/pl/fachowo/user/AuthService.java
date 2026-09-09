package pl.fachowo.user;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fachowo.common.ConflictException;
import pl.fachowo.company.CompanyRepository;
import pl.fachowo.security.JwtService;
import pl.fachowo.security.UserPrincipal;
import pl.fachowo.user.dto.AuthResponse;
import pl.fachowo.user.dto.LoginRequest;
import pl.fachowo.user.dto.MeResponse;
import pl.fachowo.user.dto.RegisterRequest;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            CompanyRepository companyRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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
}
