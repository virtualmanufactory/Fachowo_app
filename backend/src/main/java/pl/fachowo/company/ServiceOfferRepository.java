package pl.fachowo.company;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceOfferRepository extends JpaRepository<ServiceOffer, UUID> {
    List<ServiceOffer> findByCompanyId(UUID companyId);

    long countByCompanyId(UUID companyId);

    List<ServiceOffer> findByCompany_IdIn(Collection<UUID> companyIds);

    Optional<ServiceOffer> findByIdAndCompany_Id(UUID id, UUID companyId);
}
