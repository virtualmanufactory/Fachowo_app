package pl.fachowo.common;

import java.text.Normalizer;
import java.util.Locale;

public final class Slugify {

    private Slugify() {
    }

    public static String slugify(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace("ł", "l")
                .replace("Ł", "l");
        return normalized.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}
