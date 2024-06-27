package com.example.urlshortener.dto;

import com.example.urlshortener.model.Url;

import java.time.LocalDateTime;

public record AdminUrlResponse(Data data) {
    public AdminUrlResponse(Url url) {
        this(new AdminUrlResponse.Data(url));
    }

    public record Data(String type,
                       String id,
                       AdminUrlResponse.Attributes attributes) {
        public Data(Url url) {
            this("urls", url.getId().toString(), new AdminUrlResponse.Attributes(url));
        }
    }

    public record Attributes(String shortCode,
                             String longUrl,
                             LocalDateTime createdAt,
                             int usageCount,
                             LocalDateTime lastUsedAt,
                             boolean valid) {
        public Attributes(Url url) {
            this(url.getShortCode(),
                    url.getLongUrl(),
                    url.getCreatedAt(),
                    url.getUsageCount(),
                    url.getLastUsedAt(),
                    url.isValid());
        }
    }
}