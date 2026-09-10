package pl.fachowo.company;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CompanyImageRepository extends JpaRepository<CompanyImage, UUID> {
    List<CompanyImage> findByCompanyId(UUID companyId);

    long countByCompanyId(UUID companyId);
}
