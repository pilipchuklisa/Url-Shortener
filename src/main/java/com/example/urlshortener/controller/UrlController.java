package com.example.urlshortener.controller;

import com.example.urlshortener.dto.JsonApiRequest;
import com.example.urlshortener.dto.JsonApiResponse;
import com.example.urlshortener.dto.UrlStatsResponse;
import com.example.urlshortener.dto.UrlTopResponse;
import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value ="/api/urls", produces = "application/vnd.api+json")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping(consumes = "application/vnd.api+json")
    public ResponseEntity<JsonApiResponse> createShortUrl(@RequestBody JsonApiRequest request) {
        JsonApiResponse jsonApiResponse = urlService.createShortUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(jsonApiResponse);
    }

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

    @GetMapping()
    public ResponseEntity<List<JsonApiResponse>> getUrls() {
        List<Url> urls = urlService.getAll();
        List<JsonApiResponse> response = urls.stream()
                .map(JsonApiResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<JsonApiResponse> getShortCodeByLongUrl(@RequestParam String longUrl) {
        Optional<Url> urlOptional = urlService.getShortCodeByLongUrl(longUrl);
        if (urlOptional.isPresent()) {
            Url url = urlOptional.get();
            JsonApiResponse response = new JsonApiResponse(url);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/admin/stats/{shortCode}")
    public ResponseEntity<UrlStatsResponse> getUrlStats(@PathVariable String shortCode) {
        Optional<Url> urlOptional = urlService.getUrlStats(shortCode);
        if (urlOptional.isPresent()) {
            Url url = urlOptional.get();
            UrlStatsResponse response = new UrlStatsResponse(url);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/admin/top10")
    public ResponseEntity<List<UrlTopResponse>> getTop10UrlsAdmin() {
        List<Url> topUrls = urlService.getTop10Urls();
        List<UrlTopResponse> response = topUrls.stream()
                .map(UrlTopResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
