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
public interface UrlRepository extends JpaRepository<Url, Long>{
    Optional<Url> findByShortCode(String shortCode);
    Optional<Url> findByLongUrl(String longUrl);
    List<Url>  findTop10ByOrderByUsageCountDesc();
    @Query("SELECT u FROM Url u WHERE u.valid = true AND u.lastUsedAt < :time")
    List<Url> findUrlsToInvalidate(@Param("time") LocalDateTime time);
}
