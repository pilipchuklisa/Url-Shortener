package com.example.urlshortener.service;

import com.example.urlshortener.dto.JsonApiRequest;
import com.example.urlshortener.dto.JsonApiResponse;
import com.example.urlshortener.mapper.UrlMapper;
import com.example.urlshortener.model.Url;
import com.example.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UrlServiceTest {

    private UrlService urlService;
    private UrlRepository urlRepository;
    private UrlMapper urlMapper;

    @BeforeEach
    void setUp() {
        urlRepository = mock(UrlRepository.class);
        urlMapper = mock(UrlMapper.class);
        urlService = new UrlService(urlRepository, urlMapper);
    }

    @Test
    void testCreateShortUrl() {
        String longUrl = "https://jsonapi.org/";
        JsonApiRequest request = new JsonApiRequest(
                new JsonApiRequest.Data(
                        "urls",
                        new JsonApiRequest.Attributes(
                                longUrl)));
        Url url = new Url(1L, "shortCode", longUrl);
        JsonApiResponse expectedResponse = new JsonApiResponse(url);

        when(urlRepository.findByShortCode(anyString())).thenReturn(Optional.empty());
        when(urlRepository.save(any(Url.class))).thenReturn(url);
        when(urlMapper.toJsonApiResponse(any(Url.class))).thenReturn(expectedResponse);

        JsonApiResponse actualResponse = urlService.createShortUrl(request);

        assertEquals(expectedResponse, actualResponse);

        ArgumentCaptor<Url> urlCaptor = ArgumentCaptor.forClass(Url.class);
        verify(urlRepository).save(urlCaptor.capture());
        Url savedUrl = urlCaptor.getValue();

        assertEquals(longUrl, savedUrl.getLongUrl());
        assertNotNull(savedUrl.getShortCode());
    }

    @Test
    void testGenerateShortCode() {
        String shortCode = urlService.generateShortCode();

        assertNotNull(shortCode);
        assertEquals(6, shortCode.length());
        assertTrue(shortCode.matches("[A-Za-z0-9]+"));
    }

    @Test
    void testGetOriginalUrl() {
        String shortCode = "shortCode";
        Url url = new Url(1L, shortCode, "https://jsonapi.org/");
        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(url));

        Optional<Url> actualUrl = urlService.getOriginalUrl(shortCode);

        assertTrue(actualUrl.isPresent());
        assertEquals(url, actualUrl.get());
    }

    @Test
    void testGetOriginalUrlNotFound() {
        String shortCode = "nonExistentCode";
        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.empty());

        Optional<Url> actualUrl = urlService.getOriginalUrl(shortCode);

        assertFalse(actualUrl.isPresent());
    }
}
