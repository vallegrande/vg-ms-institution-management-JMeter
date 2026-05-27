package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.adapters.in.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.exceptions.NotFoundException;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.exceptions.ConflictException;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.exceptions.DomainException;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private ServerWebExchange exchange;
    private ServerHttpRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        exchange = Mockito.mock(ServerWebExchange.class);
        request = Mockito.mock(ServerHttpRequest.class);
        
        org.springframework.http.server.RequestPath path = Mockito.mock(org.springframework.http.server.RequestPath.class);
        when(path.value()).thenReturn("/test-path");
        when(request.getPath()).thenReturn(path);
        when(exchange.getRequest()).thenReturn(request);
    }

    @Test
    void handleNotFoundException_ShouldReturnNotFound() {
        NotFoundException ex = new NotFoundException("Not found");
        StepVerifier.create(handler.handleNotFoundException(ex, exchange))
                .assertNext(response -> {
                    org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
                    org.junit.jupiter.api.Assertions.assertEquals("Not found", response.getMessage());
                })
                .verifyComplete();
    }

    @Test
    void handleConflictException_ShouldReturnConflict() {
        ConflictException ex = new ConflictException("Conflict");
        StepVerifier.create(handler.handleConflictException(ex, exchange))
                .assertNext(response -> {
                    org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
                })
                .verifyComplete();
    }

    @Test
    void handleDomainException_ShouldReturnBadRequest() {
        DomainException ex = new DomainException("Domain error");
        StepVerifier.create(handler.handleDomainException(ex, exchange))
                .assertNext(response -> {
                    org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
                })
                .verifyComplete();
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() {
        Exception ex = new Exception("Generic error");
        StepVerifier.create(handler.handleGenericException(ex, exchange))
                .assertNext(response -> {
                    org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
                })
                .verifyComplete();
    }
}
