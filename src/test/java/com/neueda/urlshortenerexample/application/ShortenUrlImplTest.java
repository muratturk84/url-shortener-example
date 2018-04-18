package com.neueda.urlshortenerexample.application;

import com.neueda.urlshortenerexample.Application;
import com.neueda.urlshortenerexample.domain.ShortUrl;
import com.neueda.urlshortenerexample.domain.ShortUrlServiceImpl;
import com.neueda.urlshortenerexample.presentation.responses.ShortUrlResponse;
import com.neueda.urlshortenerexample.presentation.responses.StatisticsResponse;
import com.neueda.urlshortenerexample.infrastructure.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ShortenUrlImplTest {

    private ShortenUrlImpl shortenUrlImpl;

    @Mock
    private ShortUrlServiceImpl shortUrlServiceImpl;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        shortenUrlImpl = new ShortenUrlImpl(shortUrlServiceImpl);
    }

    @Test
    public void testShorten() {
        ShortUrl shortUrl = new ShortUrl(TestConstants.SHORTENED_URL, TestConstants.ORIGINAL_URL, new Date(), 1L, 0L);
        when(shortUrlServiceImpl.shorten(TestConstants.ORIGINAL_URI)).thenReturn(shortUrl);
        ShortUrlResponse shortUrlResponse = ShortUrlResponse.builder()
                .originalUrl(shortUrl.getOriginalUrl())
                .shortenedUrl(shortUrl.getShortenedUrl())
                .build();

        ShortUrlResponse shortUrlResponseTest = shortenUrlImpl.shorten(TestConstants.ORIGINAL_URI);

        assertNotNull(shortUrlResponseTest);
        assertEquals(shortUrlResponse, shortUrlResponseTest);
    }

    @Test
    public void testGetRedirectionUrl() {
        ShortUrl shortUrl = new ShortUrl(TestConstants.SHORTENED_URL, TestConstants.ORIGINAL_URL, new Date(), 1L, 0L);
        when(shortUrlServiceImpl.findShortenedUrl(TestConstants.SHORTENED_URL)).thenReturn(shortUrl);
        String redirectionUrl = shortUrl.getOriginalUrl();

        String redirectionUrlTest = shortenUrlImpl.getRedirectionUrl(TestConstants.SHORTENED_URL);

        assertNotNull(redirectionUrlTest);
        assertEquals(redirectionUrl, redirectionUrlTest);
    }

    @Test
    public void testStatistics() {
        Long shortUrlCount = 5L;
        List<String> mostShortenedUrls = new ArrayList<>();
        mostShortenedUrls.add("shortenedUrl1");
        mostShortenedUrls.add("shortenedUrl2");
        mostShortenedUrls.add("shortenedUrl3");
        List<String> mostRedirectedUrls = new ArrayList<>();
        mostRedirectedUrls.add("shortenedUrl1");
        mostRedirectedUrls.add("shortenedUrl2");
        mostRedirectedUrls.add("shortenedUrl3");
        when(shortUrlServiceImpl.getAllCount()).thenReturn(shortUrlCount);
        when(shortUrlServiceImpl.getMostShortenedUrls()).thenReturn(mostShortenedUrls);
        when(shortUrlServiceImpl.getMostRedirectedUrls()).thenReturn(mostRedirectedUrls);
        StatisticsResponse statisticsResponse = new StatisticsResponse(shortUrlCount, mostShortenedUrls, mostRedirectedUrls);

        StatisticsResponse statisticsResponseTest = shortenUrlImpl.statistics();

        assertNotNull(statisticsResponseTest);
        assertEquals(statisticsResponse, statisticsResponseTest);
    }

    @Test
    public void testDeleteRecord() {
        shortenUrlImpl.deleteRecord(TestConstants.ORIGINAL_URI);

        verify(shortUrlServiceImpl, times(1)).deleteOriginalUri(TestConstants.ORIGINAL_URI);
    }

    @Test
    public void deleteAllRecords() {
        shortenUrlImpl.deleteAllRecords();

        verify(shortUrlServiceImpl, times(1)).deleteAll();
    }
}
