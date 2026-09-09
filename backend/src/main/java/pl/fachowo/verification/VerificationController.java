package pl.fachowo.verification;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fachowo.security.UserPrincipal;
import pl.fachowo.verification.dto.NipLookupRequest;
import pl.fachowo.verification.dto.RegistryCompanyData;

@RestController
@RequestMapping("/api/verifications")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/nip")
    public RegistryCompanyData lookup(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody NipLookupRequest request
    ) {
        return verificationService.requireLookup(request.nip());
    }
}
