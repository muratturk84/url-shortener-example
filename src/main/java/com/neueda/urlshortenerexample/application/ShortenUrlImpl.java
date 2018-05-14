package com.neueda.urlshortenerexample.application;

import com.neueda.urlshortenerexample.domain.ShortUrl;
import com.neueda.urlshortenerexample.domain.ShortUrlServiceImpl;
import com.neueda.urlshortenerexample.presentation.ShortUrlNotFoundException;
import com.neueda.urlshortenerexample.presentation.responses.ShortUrlResponse;
import com.neueda.urlshortenerexample.presentation.responses.StatisticsResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class ShortenUrlImpl implements ShortenUrl{

    @Autowired
    private ShortUrlServiceImpl shortUrlServiceImpl;


    /**
     * Used to shorten the validated URI
     * As a response URL and matching hash is returned in Json format
     * shortenCount for statistics increments per request in shortUrlServiceImpl
     *
     * @param uri URI to shorten
     * @return ShortUrlResponse
     */
    public ShortUrlResponse shorten(URI uri) {
        ShortUrl shortUrl = shortUrlServiceImpl.shorten(uri);

        return ShortUrlResponse.builder()
                .originalUrl(shortUrl.getOriginalUrl())
                .shortenedUrl(shortUrl.getShortenedUrl())
                .build();
    }

    /**
     * Shortened URL redirected to original URL
     * If found in repository, redirectionCounter increments in shortUrlServiceImpl
     *
     * @param shortenedUrl shortURL to redirect
     * @return String
     */
    public String getRedirectionUrl(String shortenedUrl) throws ShortUrlNotFoundException {
        ShortUrl shortUrl = shortUrlServiceImpl.findShortenedUrl(shortenedUrl);

        return shortUrl.getOriginalUrl();
    }

    /**
     * Number of records in the repository is returned
     * In the response totalShortUrlCount is included
     * @return StatisticsResponse
     */
    public StatisticsResponse statistics() {
        Long totalShortUrlCount = shortUrlServiceImpl.getAllCount();
        List<String> mostShortenedUrls = shortUrlServiceImpl.getMostShortenedUrls();
        List<String> mostRedirectedUrls = shortUrlServiceImpl.getMostRedirectedUrls();

        return new StatisticsResponse(totalShortUrlCount, mostShortenedUrls, mostRedirectedUrls);
    }

    /**
     * Delete record from the repository if exists
     *
     * @param originalUri URI to delete
     */
    public void deleteRecord(URI originalUri) {
        shortUrlServiceImpl.deleteOriginalUri(originalUri);
    }

    /**
     * Delete all records from the repository
     */
    public void deleteAllRecords() {
        shortUrlServiceImpl.deleteAll();
    }
}
