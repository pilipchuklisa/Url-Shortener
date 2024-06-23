package com.example.urlshortener.controller;

import com.example.urlshortener.dto.JsonApiRequest;
import com.example.urlshortener.dto.JsonApiResponse;
import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UrlControllerTest {

    @Mock
    private UrlService urlService;

    @InjectMocks
    private UrlController urlController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateShortUrl() {
        JsonApiRequest request = new JsonApiRequest(
                new JsonApiRequest.Data(
                        "urls",
                        new JsonApiRequest.Attributes(
                                "https://jsonapi.org/")));
        Url url = new Url(
                1L,
                "shortCode",
                "https://jsonapi.org/");
        JsonApiResponse expectedResponse = new JsonApiResponse(url);

        when(urlService.createShortUrl(request)).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = urlController.createShortUrl(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testRedirectToOriginalFound() {
        Url url = new Url(1L, "shortCode", "https://jsonapi.org/");

        when(urlService.getOriginalUrl(anyString())).thenReturn(Optional.of(url));

        ResponseEntity<Void> responseEntity = urlController.redirectToOriginal("shortCode");

        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
        assertEquals(URI.create("https://jsonapi.org/"), responseEntity.getHeaders().getLocation());
    }

    @Test
    void testRedirectToOriginalNotFound() {
        when(urlService.getOriginalUrl(anyString())).thenReturn(Optional.empty());

        ResponseEntity<Void> responseEntity = urlController.redirectToOriginal("shortCode");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
