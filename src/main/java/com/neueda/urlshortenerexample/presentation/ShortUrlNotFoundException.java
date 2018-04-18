package com.neueda.urlshortenerexample.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class ShortUrlNotFoundException extends RuntimeException {
    private String shortUrl;
}
