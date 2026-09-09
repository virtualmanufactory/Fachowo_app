package pl.fachowo.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateServiceRequest(
        @NotBlank String name,
        @NotNull BigDecimal price,
        @NotBlank String unit,
        boolean available
) {
}
