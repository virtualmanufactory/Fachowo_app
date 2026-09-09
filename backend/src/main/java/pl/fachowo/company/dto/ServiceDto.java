package pl.fachowo.company.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ServiceDto(UUID id, String name, BigDecimal price, String unit, boolean available) {
}
