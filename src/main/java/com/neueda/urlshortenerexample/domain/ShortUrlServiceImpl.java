package com.neueda.urlshortenerexample.domain;

import com.google.common.hash.Hashing;
import com.neueda.urlshortenerexample.infrastructure.ShortUrlRepository;
import com.neueda.urlshortenerexample.presentation.ShortUrlNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class ShortUrlServiceImpl implements ShortUrlService {

    private static Logger log = LoggerFactory.getLogger(ShortUrlServiceImpl.class);

    @Autowired
    private ShortUrlRepository shortUrlRepository;


    /**
     * If URI is found in the repository, it is returned and shortenCount increments
     * If not fount in the repository creation begins
     *
     * @param originalURI URI to shorten
     */
    public ShortUrl shorten(URI originalURI) {
        ShortUrl shortUrl = shortUrlRepository.findByOriginalUrl(originalURI.toString());

        if (shortUrl != null) {
            log.info("Shorten: " + originalURI + " found in repository: " + shortUrl.getShortenedUrl());

            incrementShortenCount(shortUrl);
            saveShortUrl(shortUrl);

            return shortUrl;
        } else {
            Date date = new Date();
            return createShortUrl(originalURI, date);
        }
    }

    /**
     * Shortened url is searched in the repository
     * If found redirectionCount increments and returns original URL
     * If not found ShortUrlNotFoundException thrown
     *
     * @param shortenedUrl shortURL to redirect
     * @throws ShortUrlNotFoundException
     */
    @SuppressWarnings("JavaDoc")
    public ShortUrl findShortenedUrl(String shortenedUrl) throws ShortUrlNotFoundException {
        ShortUrl shortUrl = shortUrlRepository.findByShortenedUrl(shortenedUrl);

        if (shortUrl != null) {
            log.info("URL " + shortUrl + " found in repository: " + shortUrl.getOriginalUrl());
            incrementRedirectionCount(shortUrl);
            saveShortUrl(shortUrl);

            return shortUrl;
        } else {
            log.error("URL " + shortenedUrl + " not found in repository");

            throw new ShortUrlNotFoundException(shortenedUrl);
        }
    }

    /**
     * If URL found in the repository delete
     *
     * @param originalUri originalURI to delete
     */
    public void deleteOriginalUri(URI originalUri) {
        ShortUrl shortUrl = shortUrlRepository.findByOriginalUrl(originalUri.toString());

        if (shortUrl != null)
            shortUrlRepository.delete(shortUrl);
    }

    /**
     * Delete all records in repository
     */
    public void deleteAll() {
        shortUrlRepository.deleteAll();
    }

    /**
     * Get number of records in the repository
     */
    public Long getAllCount() {
        return shortUrlRepository.countDistinctByShortenedUrlIsNotNull();
    }

    /**
     * New shortUrl record is created and stored in the repository
     *
     * @param originalUri URL to shorten
     */
    public ShortUrl createShortUrl(URI originalUri, Date date) {
        final String shortenedUrl = Hashing.murmur3_32().hashString(originalUri.toString(), StandardCharsets.UTF_8).toString();
        ShortUrl shortUrl = new ShortUrl(shortenedUrl, originalUri.toString(), date, 1L, 0L);

        log.info("Created short originalUri " + shortUrl.getShortenedUrl() + " for " + shortUrl.getOriginalUrl());

        saveShortUrl(shortUrl);

        return shortUrl;
    }

    /**
     * Used to save shortUrl record in the repository
     *
     * @param shortUrl URL to save
     */
    //
    public void saveShortUrl(ShortUrl shortUrl) {
        shortUrlRepository.save(shortUrl);
    }

    /**
     * Increment shortenCount matching the shortUrl
     *
     * @param shortUrl URL to increment shortenCount
     */
    public void incrementShortenCount(ShortUrl shortUrl) {
        shortUrl.setShortenCount(shortUrl.getShortenCount() + 1);
    }

    /**
     * Increment redirectionCount matching the shortUrl
     *
     * @param shortUrl URL to increment redirectionCount
     */
    public void incrementRedirectionCount(ShortUrl shortUrl) {
        shortUrl.setRedirectionCount(shortUrl.getRedirectionCount() + 1);
    }

    /**
     * Return list of most shortened urls
     */
    public List<String> getMostShortenedUrls() {
        ShortUrl shortUrlRecord = shortUrlRepository.findFirstByOrderByShortenCountDesc();
        Long highestShortenCount;

        if (shortUrlRecord != null) {
            highestShortenCount = shortUrlRecord.getShortenCount();
            List<ShortUrl> shortUrls = shortUrlRepository.findAllByShortenCount(highestShortenCount);

            List<String> strings = new ArrayList<>();

            for (ShortUrl shortUrl : shortUrls) {
                strings.add(shortUrl.getShortenedUrl());
            }
            return strings;

        } else {
            return null;
        }

    }

    /**
     * Return list of most redirected urls
     */
    public List<String> getMostRedirectedUrls() {
        ShortUrl shortUrlRecord = shortUrlRepository.findFirstByOrderByRedirectionCountDesc();
        Long highestRedirectionCount;

        if (shortUrlRecord != null) {
            highestRedirectionCount = shortUrlRecord.getRedirectionCount();
            List<ShortUrl> shortUrls = shortUrlRepository.findAllByRedirectionCount(highestRedirectionCount);
            List<String> strings = new ArrayList<>();

            for (ShortUrl shortUrl : shortUrls) {
                strings.add(shortUrl.getOriginalUrl());
            }

            return strings;
        } else {
            return null;
        }
    }
}
