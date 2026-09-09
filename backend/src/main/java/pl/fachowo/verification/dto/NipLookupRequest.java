package pl.fachowo.verification.dto;

import jakarta.validation.constraints.NotBlank;

public record NipLookupRequest(@NotBlank String nip) {
}
