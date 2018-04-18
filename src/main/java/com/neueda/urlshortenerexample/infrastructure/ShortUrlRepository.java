package com.neueda.urlshortenerexample.infrastructure;

import com.neueda.urlshortenerexample.domain.ShortUrl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortUrlRepository extends MongoRepository<ShortUrl, Long> {
    ShortUrl findByOriginalUrl(String originalUrl);

    ShortUrl findByShortenedUrl(String shortenedUrl);

    Long countDistinctByShortenedUrlIsNotNull();

    ShortUrl findFirstByOrderByShortenCountDesc();

    ShortUrl findFirstByOrderByRedirectionCountDesc();

    List<ShortUrl> findAllByShortenCount(Long shortenCount);

    List<ShortUrl> findAllByRedirectionCount(Long redirectionCount);
}
