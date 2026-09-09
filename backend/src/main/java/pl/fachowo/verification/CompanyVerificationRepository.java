package pl.fachowo.verification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyVerificationRepository extends JpaRepository<CompanyVerification, UUID> {
}
