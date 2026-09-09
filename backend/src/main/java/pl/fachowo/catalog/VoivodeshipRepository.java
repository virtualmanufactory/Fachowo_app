package pl.fachowo.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VoivodeshipRepository extends JpaRepository<Voivodeship, UUID> {
    Optional<Voivodeship> findBySlug(String slug);
}
