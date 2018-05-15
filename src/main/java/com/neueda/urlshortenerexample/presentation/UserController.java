package com.neueda.urlshortenerexample.presentation;

import com.neueda.urlshortenerexample.application.ShortenUrlImpl;
import com.neueda.urlshortenerexample.presentation.requests.ShortUrlRequest;
import com.neueda.urlshortenerexample.presentation.responses.ShortUrlResponse;
import com.neueda.urlshortenerexample.presentation.responses.StatisticsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class UserController {

    private final ShortenUrlImpl shortenUrlImpl;

    @Autowired
    public UserController(ShortenUrlImpl shortenUrlImpl) {
        this.shortenUrlImpl = shortenUrlImpl;
    }

    /**
     * Web Service used to shorten given string
     * If given string validated then shortened
     *
     * @param shortUrlRequest Request including string to be shortened
     */
    @RequestMapping(value = "/shorten", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> shortenURL(@RequestBody ShortUrlRequest shortUrlRequest) throws URISyntaxException, MalformedURLException {
        URI uri = UriValidatorImpl.validate(shortUrlRequest);

        if (uri == null) {
            throw new MalformedURLException("URI is null");
        }

        ShortUrlResponse response = shortenUrlImpl.shorten(uri);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Web Service used to redirect to original URL
     * If given string stored in the repository redirected.
     * Else 404 failure message is shown
     *
     * @param shortenedUrl Short URL string
     */
    @RequestMapping(value = "/{shortenedUrl}", method = RequestMethod.GET)
    public void redirect(@PathVariable String shortenedUrl, HttpServletResponse response) throws ShortUrlNotFoundException, IOException {
        response.sendRedirect(shortenUrlImpl.getRedirectionUrl(shortenedUrl));
    }


    /**
     * Web Service used to show statistics
     */
    @RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public StatisticsResponse getStats() {
        return shortenUrlImpl.statistics();
    }
}
