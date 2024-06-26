package com.example.urlshortener.dto;

import com.example.urlshortener.model.Url;

import java.time.LocalDateTime;

public record UrlTopResponse(Data data) {
    public UrlTopResponse(Url url) {
        this(new UrlTopResponse.Data(url));
    }

    public record Data(String type, String id, UrlTopResponse.Attributes attributes) {
        public Data(Url url) {
            this("urls", url.getId().toString(), new UrlTopResponse.Attributes(url));
        }
    }

    public record Attributes(String longUrl, int usageCount, LocalDateTime lastUsedAt) {
        public Attributes(Url url) {
            this(url.getLongUrl(), url.getUsageCount(), url.getLastUsedAt());
        }
    }
}
