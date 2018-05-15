package com.neueda.urlshortenerexample.presentation;

import com.neueda.urlshortenerexample.presentation.requests.ShortUrlRequest;

import java.net.URI;
import java.net.URISyntaxException;

class UriValidatorImpl implements UriValidator {

    private UriValidatorImpl() {}

    static URI validate(ShortUrlRequest shortUrlRequest) throws URISyntaxException {
        String url = shortUrlRequest.getUrl();
        URI uri = null;

        if (absoluteUri(url) && validProtocol(url) && globallyAccessibleUri(url)) {
            uri = new URI(url);
        }

        return uri;
    }

    /**
     * Constructs a URI by parsing the given string.
     *
     * @param url URL string to be validated
     * @return boolean
     */
    private static boolean absoluteUri(String url) {
        try {
            new URI(url);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Validates the protocol provided in @validProtocols.
     *
     * @param url URL string to be validated
     * @return boolean
     */
    private static boolean validProtocol(String url) {
        try {
            String protocol = new URI(url).getScheme().toLowerCase();
            return validProtocols.stream().anyMatch(protocol::equals);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * The following URIs are local and should not be shortened:
     * - Anything on localhost
     * - 192.168.*.*
     * - 127.0.0.1
     * - 0.0.0.0
     *
     * @param url URL string to be validated
     * @return boolean
     */
    private static boolean globallyAccessibleUri(String url) {
        if (url.matches("\\w+://localhost.*")) {
            return false;
        } else if (url.matches("\\w+://192\\.168(?:\\.\\d{1,3}){2}.*")) {
            return false;
        } else return !url.matches("\\w+://(127\\.0\\.0\\.1|0\\.0\\.0\\.0)\\D?.*");
    }
}
