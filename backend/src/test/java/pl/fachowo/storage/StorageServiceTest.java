package pl.fachowo.storage;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import pl.fachowo.common.BadRequestException;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class StorageServiceTest {

    private final StorageService storageService = new StorageService(
            mock(S3Client.class),
            "fachowo",
            "http://localhost:9000"
    );

    @Test
    void rejectsUnsupportedContentType() {
        MockMultipartFile file = new MockMultipartFile("file", "note.txt", "text/plain", "x".getBytes());
        assertThatThrownBy(() -> storageService.uploadCompanyImage(UUID.randomUUID(), file))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void buildsPublicUrl() {
        assertThat(storageService.publicUrl("companies/1/a.jpg"))
                .isEqualTo("http://localhost:9000/fachowo/companies/1/a.jpg");
    }
}
