package pl.fachowo.catalog.dto;

import java.util.List;

public record CompareResponse(
        String categoryName,
        String voivodeshipName,
        String cityName,
        List<CompareRowDto> rows
) {
}
