package pl.fachowo.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @Query("""
            SELECT c FROM Company c
            JOIN FETCH c.category
            JOIN FETCH c.city city
            JOIN FETCH city.voivodeship
            WHERE c.slug = :slug
            """)
    Optional<Company> findBySlug(@Param("slug") String slug);

    @Query("""
            SELECT c FROM Company c
            JOIN FETCH c.category
            JOIN FETCH c.city city
            JOIN FETCH city.voivodeship
            WHERE c.owner.id = :ownerId
            """)
    Optional<Company> findByOwnerId(@Param("ownerId") UUID ownerId);

    boolean existsByOwnerId(UUID ownerId);

    boolean existsByNip(String nip);

    boolean existsBySlug(String slug);

    @Query("""
            SELECT DISTINCT c FROM Company c
            JOIN FETCH c.category cat
            JOIN FETCH c.city city
            JOIN FETCH city.voivodeship v
            WHERE cat.slug = :category
              AND (
                    (city.slug = :city AND v.slug = :voivodeship)
                    OR c.servesCustomersAtHome = true
                  )
            """)
    List<Company> findForCompare(
            @Param("category") String category,
            @Param("voivodeship") String voivodeship,
            @Param("city") String city
    );

    @Query("""
            SELECT c FROM Company c
            JOIN FETCH c.category cat
            JOIN FETCH c.city city
            JOIN FETCH city.voivodeship
            WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(city.name) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(cat.name) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    List<Company> search(@Param("query") String query);
}
