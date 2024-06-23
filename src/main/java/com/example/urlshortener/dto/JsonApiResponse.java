package com.example.urlshortener.dto;

import com.example.urlshortener.model.Url;

public record JsonApiResponse(Data data) {
    public JsonApiResponse(Url url) {
        this(new Data(url));
    }

    public record Data(String type, String id, Attributes attributes) {
        public Data(Url url) {
            this("urls", url.getId().toString(), new Attributes(url));
        }
    }

    public record Attributes(String shortCode, String longUrl) {
        public Attributes(Url url) {
            this(url.getShortCode(), url.getLongUrl());
        }
    }
}
