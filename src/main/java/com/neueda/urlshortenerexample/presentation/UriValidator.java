package com.neueda.urlshortenerexample.presentation;

public interface UriValidator {
    boolean validate(String uri);

    boolean absoluteUri(String uri);

    boolean validProtocol(String uri);

    boolean globallyAccessibleUri(String uri);
}
