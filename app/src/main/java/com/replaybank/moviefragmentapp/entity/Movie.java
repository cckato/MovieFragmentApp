package com.replaybank.moviefragmentapp.entity;

import lombok.Data;

/**
 * Created by kato on 14/11/25.
 */
@Data
public class Movie {
    int id;
    String name;
    String introduction;
    String bodyUrl;
    String thumbUrl;
}
