package com.neueda.urlshortenerexample.domain;

import com.neueda.urlshortenerexample.Application;
import com.neueda.urlshortenerexample.infrastructure.ShortUrlRepository;
import com.neueda.urlshortenerexample.presentation.ShortUrlNotFoundException;
import com.neueda.urlshortenerexample.infrastructure.TestConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ShortUrlServiceImplTest {

    private ShortUrlServiceImpl shortUrlServiceImpl;

    @Mock
    ShortUrlRepository shortUrlRepository;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        shortUrlServiceImpl = new ShortUrlServiceImpl(shortUrlRepository);
    }

    @Test
    public void testIncrementShortenCount() {
        ShortUrl shortUrl = new ShortUrl(TestConstants.SHORTENED_URL, TestConstants.ORIGINAL_URL, new Date(), 5L, 0L);
        shortUrl.setShortenCount(shortUrl.getShortenCount() + 1);

        ShortUrl shortUrlTest = new ShortUrl(TestConstants.SHORTENED_URL, TestConstants.ORIGINAL_URL, new Date(), 5L, 0L);
        shortUrlServiceImpl.incrementShortenCount(shortUrlTest);

        assertNotNull(shortUrlTest);
        assertEquals(shortUrl, shortUrlTest);
    }

    @Test
    public void testIncrementRedirectionCount() {
        ShortUrl shortUrl = new ShortUrl(TestConstants.SHORTENED_URL, TestConstants.ORIGINAL_URL, new Date(), 5L, 0L);
        shortUrl.setRedirectionCount(shortUrl.getRedirectionCount() + 1);

        ShortUrl shortUrlTest = new ShortUrl(TestConstants.SHORTENED_URL, TestConstants.ORIGINAL_URL, new Date(), 5L, 0L);
        shortUrlServiceImpl.incrementRedirectionCount(shortUrlTest);

        assertNotNull(shortUrlTest);
        assertEquals(shortUrl, shortUrlTest);
    }

    @Test
    public void testSaveShortUrl() {
        ShortUrl shortUrl = new ShortUrl(TestConstants.SHORTENED_URL, TestConstants.ORIGINAL_URL, new Date(), 1L, 0L);
        shortUrlServiceImpl.saveShortUrl(shortUrl);

        verify(shortUrlRepository, times(1)).save(shortUrl);
    }

    @Test
    public void testCreateShortUrl() {
        Date date = new Date();
        ShortUrl shortUrl = new ShortUrl(TestConstants.SHORTENED_URL, TestConstants.ORIGINAL_URL, date, 1L, 0L);
        shortUrlServiceImpl.saveShortUrl(shortUrl);

        ShortUrl shortUrlTest = shortUrlServiceImpl.createShortUrl(TestConstants.ORIGINAL_URI, date);

        assertNotNull(shortUrlTest);
        assertEquals(shortUrl, shortUrlTest);
    }

    @Test
    public void testShortenNewOriginalUri() {
        ShortUrl shortUrlTest = shortUrlServiceImpl.shorten(TestConstants.ORIGINAL_URI);

        when(shortUrlRepository.findByOriginalUrl(TestConstants.ORIGINAL_URL)).thenReturn(null);
        ShortUrl shortUrl = shortUrlServiceImpl.createShortUrl(TestConstants.ORIGINAL_URI, shortUrlTest.getCreateDate());


        assertNotNull(shortUrlTest);
        assertEquals(shortUrl, shortUrlTest);
    }

    @Test
    public void testShortenOriginalUriAlreadyInRepository() {
        ShortUrl shortUrl = new ShortUrl(TestConstants.SHORTENED_URL, TestConstants.ORIGINAL_URL, new Date(), 5L, 0L);
        when(shortUrlRepository.findByOriginalUrl(TestConstants.ORIGINAL_URL)).thenReturn(shortUrl);
        shortUrlServiceImpl.incrementShortenCount(shortUrl);
        shortUrlServiceImpl.saveShortUrl(shortUrl);

        ShortUrl shortUrlTest = shortUrlServiceImpl.shorten(TestConstants.ORIGINAL_URI);

        assertNotNull(shortUrlTest);
        assertEquals(shortUrl, shortUrlTest);
    }

    @Test
    public void testFindShortenedUrlInRepository() {
        ShortUrl shortUrl = new ShortUrl(TestConstants.SHORTENED_URL, TestConstants.ORIGINAL_URL, new Date(), 5L, 0L);
        when(shortUrlRepository.findByShortenedUrl(TestConstants.SHORTENED_URL)).thenReturn(shortUrl);
        shortUrlServiceImpl.incrementRedirectionCount(shortUrl);
        shortUrlServiceImpl.saveShortUrl(shortUrl);

        ShortUrl shortUrlTest = shortUrlServiceImpl.findShortenedUrl(TestConstants.SHORTENED_URL);

        assertNotNull(shortUrlTest);
        assertEquals(shortUrl, shortUrlTest);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testFindShortenedUrlNotInRepository() throws ShortUrlNotFoundException {
        when(shortUrlRepository.findByShortenedUrl(TestConstants.SHORTENED_URL)).thenReturn(null);

        thrown.expect(ShortUrlNotFoundException.class);
        shortUrlServiceImpl.findShortenedUrl(TestConstants.SHORTENED_URL);

    }

    @Test
    public void testDeleteOriginalUri() {
        ShortUrl shortUrl = new ShortUrl(TestConstants.SHORTENED_URL, TestConstants.ORIGINAL_URL, new Date(), 5L, 0L);
        when(shortUrlRepository.findByOriginalUrl(TestConstants.ORIGINAL_URL)).thenReturn(shortUrl);

        shortUrlServiceImpl.deleteOriginalUri(TestConstants.ORIGINAL_URI);

        verify(shortUrlRepository, times(1)).delete(shortUrl);
    }

    @Test
    public void testDeleteAll() {
        shortUrlServiceImpl.deleteAll();

        verify(shortUrlRepository, times(1)).deleteAll();
    }

    @Test
    public void testGetAllCount() {
        shortUrlServiceImpl.getAllCount();

        verify(shortUrlRepository, times(1)).countDistinctByShortenedUrlIsNotNull();
    }

    @Test
    public void testGetMostShortenedUrls() {
        shortUrlServiceImpl.getMostShortenedUrls();

        verify(shortUrlRepository, times(1)).findFirstByOrderByShortenCountDesc();
    }

    @Test
    public void testGetMostRedirectedUrls() {
        shortUrlServiceImpl.getMostRedirectedUrls();

        verify(shortUrlRepository, times(1)).findFirstByOrderByRedirectionCountDesc();
    }
}
