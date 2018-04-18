package com.neueda.urlshortenerexample.domain;

import java.net.URI;
import java.util.Date;
import java.util.List;

public interface ShortUrlService {
    ShortUrl shorten(URI originalURI);

    ShortUrl findShortenedUrl(String shortenedUrl);

    void deleteOriginalUri(URI originalUri);

    void deleteAll();

    Long getAllCount();

    ShortUrl createShortUrl(URI originalUri, Date date);

    void saveShortUrl(ShortUrl shortUrl);

    void incrementShortenCount(ShortUrl shortUrl);

    void incrementRedirectionCount(ShortUrl shortUrl);

    List<String> getMostShortenedUrls();

    List<String> getMostRedirectedUrls();
}
