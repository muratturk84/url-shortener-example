package com.neueda.urlshortenerexample.presentation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neueda.urlshortenerexample.Application;
import com.neueda.urlshortenerexample.application.ShortenUrlImpl;
import com.neueda.urlshortenerexample.presentation.requests.ShortUrlRequest;
import com.neueda.urlshortenerexample.presentation.responses.ShortUrlResponse;
import com.neueda.urlshortenerexample.presentation.responses.StatisticsResponse;
import com.neueda.urlshortenerexample.infrastructure.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class UserControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @MockBean
    private ShortenUrlImpl shortenUrlImpl;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void testShortenURL() throws Exception {
        ShortUrlResponse shortUrlResponse = new ShortUrlResponse(TestConstants.SHORTENED_URL, TestConstants.ORIGINAL_URL);
        when(shortenUrlImpl.shorten(TestConstants.ORIGINAL_URI)).thenReturn(shortUrlResponse);

        byte[] jsonString = OBJECT_MAPPER.writeValueAsBytes(new ShortUrlRequest(TestConstants.ORIGINAL_URL));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/shorten")
                .content(jsonString).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andReturn();

        ShortUrlResponse shortUrlResponseTest = OBJECT_MAPPER.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<ShortUrlResponse>() {
                });

        assertNotNull(shortUrlResponseTest);
        assertEquals(shortUrlResponse, shortUrlResponseTest);
    }

    @Test
    public void testGetStats() throws Exception {
        Long totalShortUrlCount = 10L;
        List<String> mostShortenedUrls = new ArrayList<>();
        mostShortenedUrls.add("shortenedUrl1");
        mostShortenedUrls.add("shortenedUrl2");
        mostShortenedUrls.add("shortenedUrl3");
        List<String> mostRedirectedUrls = new ArrayList<>();
        mostRedirectedUrls.add("shortenedUrl1");
        mostRedirectedUrls.add("shortenedUrl2");
        mostRedirectedUrls.add("shortenedUrl3");
        StatisticsResponse statisticsResponse = new StatisticsResponse(totalShortUrlCount, mostShortenedUrls, mostRedirectedUrls);
        when(shortenUrlImpl.statistics()).thenReturn(statisticsResponse);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/statistics")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        StatisticsResponse statisticsResponseTest = OBJECT_MAPPER.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<StatisticsResponse>() {
                });

        assertNotNull(statisticsResponse);
        assertEquals(statisticsResponse, statisticsResponseTest);
    }
}
