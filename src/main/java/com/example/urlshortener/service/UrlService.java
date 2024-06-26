package com.example.urlshortener.service;

import com.example.urlshortener.dto.JsonApiRequest;
import com.example.urlshortener.dto.JsonApiResponse;
import com.example.urlshortener.mapper.UrlMapper;
import com.example.urlshortener.model.Url;
import com.example.urlshortener.repository.UrlRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@EnableScheduling
@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final UrlMapper urlMapper;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 6;
    private final Random random = new Random();

    public UrlService(UrlRepository urlRepository, UrlMapper urlMapper) {
        this.urlRepository = urlRepository;
        this.urlMapper = urlMapper;
    }

    public JsonApiResponse createShortUrl(JsonApiRequest request) {
        String longUrl = request.data().attributes().longUrl();
        String shortCode;
        Optional<Url> existingMapping;

        do {
            shortCode = generateShortCode();
            existingMapping = urlRepository.findByShortCode(shortCode);
        } while (existingMapping.isPresent());

        Url url = new Url();
        url.setShortCode(shortCode);
        url.setLongUrl(longUrl);

        urlRepository.save(url);

        return urlMapper.toJsonApiResponse(url);
    }

    public String generateShortCode() {
        StringBuilder shortCode = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            shortCode.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return shortCode.toString();
    }

    public Optional<Url> getOriginalUrl(String shortCode) {
        Optional<Url> optionalUrl = urlRepository.findByShortCode(shortCode);
        optionalUrl.ifPresent(url -> {
            url.setUsageCount(url.getUsageCount() + 1);
            url.setLastUsedAt(LocalDateTime.now());
            urlRepository.save(url);
        });
        return optionalUrl;
    }

    public Optional<Url> getUrlStats(String shortCode) {
        return urlRepository.findByShortCode(shortCode);
    }

    public List<Url> getTop10Urls() {
        return urlRepository.findTop10ByOrderByUsageCountDesc();
    }

    @Scheduled(fixedRate = 600000)
    public void invalidateOldUrls() {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        List<Url> urlsToInvalidate = urlRepository.findUrlsToInvalidate(tenMinutesAgo);
        urlsToInvalidate.forEach(url -> {
            url.setValid(false);
            urlRepository.save(url);
        });
    }

    public Optional<Url> getShortCodeByLongUrl(String longUrl) {
        return urlRepository.findByLongUrl(longUrl);
    }

    public List<Url> getAll() {
        return urlRepository.findAll();
    }

}
