package com.neueda.urlshortenerexample.presentation.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatisticsResponse {
    private Long totalShortUrlCount;
    private List<String> mostShortenedUrls;
    private List<String> mostRedirectedUrls;
}
