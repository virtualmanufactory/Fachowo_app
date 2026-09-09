package pl.fachowo.verification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import pl.fachowo.company.Company;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "company_verifications")
public class CompanyVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(nullable = false, length = 10)
    private String nip;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationSource source;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus status;

    @Column(columnDefinition = "text")
    private String payload;

    @Column(name = "verified_at", nullable = false)
    private Instant verifiedAt = Instant.now();

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public void setSource(VerificationSource source) {
        this.source = source;
    }

    public void setStatus(VerificationStatus status) {
        this.status = status;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
