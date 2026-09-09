package pl.fachowo.company.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CompanyProfileDto(
        UUID id,
        String slug,
        String name,
        String nip,
        String description,
        String categorySlug,
        String categoryName,
        String voivodeshipSlug,
        String voivodeshipName,
        String citySlug,
        String cityName,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        boolean servesCustomersAtHome,
        String phone,
        String email,
        String website,
        boolean verified,
        boolean available,
        Instant createdAt,
        List<ServiceDto> services,
        List<ImageDto> images
) {
}
