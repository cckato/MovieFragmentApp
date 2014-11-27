package com.replaybank.moviefragmentapp.model;

import com.replaybank.moviefragmentapp.common.BusHolder;
import com.replaybank.moviefragmentapp.entity.Movie;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;

/**
 * Created by kato on 14/11/25.
 */
public class MovieModel {

    @Getter
    private ArrayList<Movie> movies = null;

    private HashMap<Integer, Movie> movieCache = new HashMap<Integer, Movie>();

    public void fetchMovies() {
        movies = new ArrayList<Movie>();
        for (int i=0; i<20; i++) {
            Movie movie = new Movie();
            movie.setId(i);
            movie.setName("動画その" + i);
            movie.setIntroduction("投稿者コメントその" + i);
            movie.setBodyUrl("http://ec2-54-65-87-57.ap-northeast-1.compute.amazonaws.com/gochiusa/low.m3u8");
            movies.add(movie);
            movieCache.put(movie.getId(), movie);
        }

        //更新を通知
        BusHolder.get().post(this);
    }

    public Movie getMovie(int id) {
        if (movieCache.containsKey(id)) {
            return movieCache.get(id);
        } else {
            return null;
        }
    }



}
