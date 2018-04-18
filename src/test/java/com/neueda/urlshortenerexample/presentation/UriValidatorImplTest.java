package com.neueda.urlshortenerexample.presentation;

import com.neueda.urlshortenerexample.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class UriValidatorImplTest {

    UriValidatorImpl uriValidatorImpl = new UriValidatorImpl();


    @Test
    public void validate() {
        assertTrue(uriValidatorImpl.validate("http://www.neueda.com"));
        assertTrue(uriValidatorImpl.validate("http://www.neueda.com/contact"));
        assertTrue(uriValidatorImpl.validate("HTTP://www.neueda.com"));
        assertTrue(uriValidatorImpl.validate("https://www.neueda.com"));
        assertTrue(uriValidatorImpl.validate("https://www.neueda.com/contact"));
        assertTrue(uriValidatorImpl.validate("HTTPS://www.neueda.com"));
        assertFalse(uriValidatorImpl.validate("htt://www.neueda.com"));
        assertFalse(uriValidatorImpl.validate("HTT://www.neueda.com"));
        assertFalse(uriValidatorImpl.validate("http://localhost:8080"));
        assertFalse(uriValidatorImpl.validate("http://192.168.1.1"));
        assertFalse(uriValidatorImpl.validate("http://127.0.0.1"));
        assertFalse(uriValidatorImpl.validate("http://0.0.0.0"));
    }
}
