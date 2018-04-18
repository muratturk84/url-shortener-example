package com.neueda.urlshortenerexample.presentation.responses;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@Data
public class ShortUrlResponse {

    private String shortenedUrl;
    private String originalUrl;

    public ShortUrlResponse(String shortenedUrl, String originalUrl){
        this.shortenedUrl = convertToUrl(shortenedUrl);
        this.originalUrl = originalUrl;
    }

    private String convertToUrl(String shortenedUrl) {

        return "http://localhost:8080/" + shortenedUrl;
    }
}
