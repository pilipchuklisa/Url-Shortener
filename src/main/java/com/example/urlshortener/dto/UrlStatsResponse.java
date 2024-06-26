package com.example.urlshortener.dto;

import com.example.urlshortener.model.Url;

import java.time.LocalDateTime;

public record UrlStatsResponse(Data data) {
    public UrlStatsResponse(Url url) {
        this(new Data(url));
    }

    public record Data(String type, String id, Attributes attributes) {
        public Data(Url url) {
            this("urls", url.getId().toString(), new Attributes(url));
        }
    }

    public record Attributes(LocalDateTime createdAt, int usageCount, LocalDateTime lastUsedAt) {
        public Attributes(Url url) {
            this(url.getCreatedAt(), url.getUsageCount(), url.getLastUsedAt());
        }
    }
}
