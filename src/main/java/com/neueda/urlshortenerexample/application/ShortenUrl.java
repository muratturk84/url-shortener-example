package com.neueda.urlshortenerexample.application;

import com.neueda.urlshortenerexample.presentation.responses.ShortUrlResponse;
import com.neueda.urlshortenerexample.presentation.responses.StatisticsResponse;

import java.net.URI;

public interface ShortenUrl {
    ShortUrlResponse shorten(URI uri);

    String getRedirectionUrl(String shortenedUrl);

    StatisticsResponse statistics();

    void deleteRecord(URI originalUri);

    void deleteAllRecords();
}



