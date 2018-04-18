package com.neueda.urlshortenerexample.presentation;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

class UriValidatorImpl implements UriValidator {
    private List<String> validProtocols = Arrays.asList("http", "https");

    public boolean validate(String uri) {
        return absoluteUri(uri) && validProtocol(uri) && globallyAccessibleUri(uri);
    }

    /**
     * Constructs a URI by parsing the given string.
     *
     * @param uri URI string to be validated
     */
    public boolean absoluteUri(String uri) {
        try {
            new URI(uri);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Validates the protocol provided in @validProtocols.
     *
     * @param uri URI string to be validated
     */
    public boolean validProtocol(String uri) {
        try {
            String protocol = new URI(uri).getScheme().toLowerCase();
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
     * @param uri URI string to be validated
     */
    public boolean globallyAccessibleUri(String uri) {
        if (uri.matches("\\w+://localhost.*")) {
            return false;
        } else if (uri.matches("\\w+://192\\.168(?:\\.\\d{1,3}){2}.*")) {
            return false;
        } else return !uri.matches("\\w+://(127\\.0\\.0\\.1|0\\.0\\.0\\.0)\\D?.*");
    }
}
