package com.example.urlshortener.mapper;

import com.example.urlshortener.dto.JsonApiResponse;
import com.example.urlshortener.model.Url;
import org.springframework.stereotype.Service;

@Service
public class UrlMapper {

    public JsonApiResponse toJsonApiResponse(Url url) {
        return new JsonApiResponse(url);
    }
}
