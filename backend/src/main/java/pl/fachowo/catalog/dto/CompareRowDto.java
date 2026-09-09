package pl.fachowo.catalog.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CompareRowDto(
        UUID companyId,
        String slug,
        String name,
        String city,
        BigDecimal price,
        String unit,
        boolean available,
        boolean servesCustomersAtHome,
        boolean verified
) {
}
