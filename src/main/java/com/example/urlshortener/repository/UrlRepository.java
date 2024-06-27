package com.example.urlshortener.repository;

import com.example.urlshortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByLongUrl(String longUrl);

    Optional<Url> findByShortCode(String shortCode);

    Optional<Url> findByShortCodeAndValid(String shortCode, boolean valid);

    List<Url> findAllByValid(boolean valid);

    Optional<Url> findByLongUrlAndValid(String longUrl, boolean valid);

    List<Url> findTop10ByValidTrueOrderByUsageCountDesc();

    @Query("SELECT u FROM Url u WHERE u.valid = true AND u.lastUsedAt < :time OR (u.lastUsedAt IS NULL AND u.createdAt < :time)")
    List<Url> findUrlsToInvalidate(@Param("time") LocalDateTime time);
}
