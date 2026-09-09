package pl.fachowo.user.dto;

import java.util.UUID;

public record MeResponse(UUID id, String email, String role, UUID companyId, String companySlug) {
}
