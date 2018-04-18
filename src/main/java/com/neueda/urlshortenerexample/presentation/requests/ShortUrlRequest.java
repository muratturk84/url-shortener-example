package com.neueda.urlshortenerexample.presentation.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShortUrlRequest implements Serializable {
    private String url;
}
