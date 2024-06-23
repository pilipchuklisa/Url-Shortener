package com.example.urlshortener.mapper;

import com.example.urlshortener.dto.JsonApiResponse;
import com.example.urlshortener.model.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UrlMapperTest {

    private UrlMapper urlMapper;

    @BeforeEach
    void setUp() {
        urlMapper = new UrlMapper();
    }

    @Test
    void testToJsonApiResponse() {
        Url url = new Url(1L, "shortCode", "https://jsonapi.org/");

        JsonApiResponse expectedResponse = new JsonApiResponse(url);
        JsonApiResponse actualResponse = urlMapper.toJsonApiResponse(url);

        assertEquals(expectedResponse, actualResponse);
    }
}
