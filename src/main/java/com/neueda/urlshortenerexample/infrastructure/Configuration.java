package com.neueda.urlshortenerexample.infrastructure;

import com.neueda.urlshortenerexample.application.ShortenUrlImpl;
import com.neueda.urlshortenerexample.domain.ShortUrlServiceImpl;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public ShortenUrlImpl shortenUrl(ShortUrlServiceImpl shortUrlService) {
        return new ShortenUrlImpl(shortUrlService);
    }

    @Bean
    public ShortUrlServiceImpl shortUrlService(ShortUrlRepository shortUrlRepository) {
        return new ShortUrlServiceImpl(shortUrlRepository);
    }
}
