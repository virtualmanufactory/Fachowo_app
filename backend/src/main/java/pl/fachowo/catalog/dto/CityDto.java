package pl.fachowo.catalog.dto;

import java.util.UUID;

public record CityDto(UUID id, String slug, String name, String voivodeshipSlug) {
}
