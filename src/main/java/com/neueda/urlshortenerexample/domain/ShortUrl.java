package com.neueda.urlshortenerexample.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "domain")
public class ShortUrl {

    @Id
    private String shortenedUrl;

    @Indexed(unique = true)
    private String originalUrl;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createDate;

    private Long shortenCount;

    private Long redirectionCount;
}
