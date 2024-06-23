package com.example.urlshortener.controller;

import com.example.urlshortener.dto.JsonApiRequest;
import com.example.urlshortener.dto.JsonApiResponse;
import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(value ="/api", produces = "application/vnd.api+json")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping(value = "/urls", consumes = "application/vnd.api+json")
    public ResponseEntity<?> createShortUrl(@RequestBody JsonApiRequest request) {
        JsonApiResponse jsonApiResponse = urlService.createShortUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(jsonApiResponse);
    }

    @Cacheable(value = "urls", key = "#shortCode", sync = true)
    @GetMapping(value = "/{shortCode}")
    public ResponseEntity<Void> redirectToOriginal(@PathVariable String shortCode) {
        Optional<Url> urlOptional = urlService.getOriginalUrl(shortCode);
        if (urlOptional.isPresent()) {
            Url url = urlOptional.get();
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(url.getLongUrl()))
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
