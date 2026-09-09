package pl.fachowo.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {
    List<City> findByVoivodeship_SlugOrderByNameAsc(String voivodeshipSlug);

    Optional<City> findByVoivodeship_SlugAndSlug(String voivodeshipSlug, String citySlug);
}
