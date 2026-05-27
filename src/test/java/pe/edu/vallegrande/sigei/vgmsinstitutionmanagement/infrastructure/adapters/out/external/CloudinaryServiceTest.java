package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.adapters.out.external;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CloudinaryServiceTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private CloudinaryService cloudinaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void uploadImage_ShouldReturnUrl() throws IOException {
        FilePart filePart = mock(FilePart.class);
        DataBuffer buffer = new DefaultDataBufferFactory().wrap("test content".getBytes());
        when(filePart.content()).thenReturn(Flux.just(buffer));

        Map<String, Object> result = Map.of("url", "http://res.cloudinary.com/test.jpg");
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(result);

        StepVerifier.create(cloudinaryService.uploadImage(filePart))
                .expectNext("http://res.cloudinary.com/test.jpg")
                .verifyComplete();
    }
}
