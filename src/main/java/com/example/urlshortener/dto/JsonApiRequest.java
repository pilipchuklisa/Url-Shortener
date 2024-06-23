package com.example.urlshortener.dto;

public record JsonApiRequest(Data data) {

    public record Data(String type, Attributes attributes) {}

    public record Attributes(String longUrl) {}
}
