package com.replaybank.moviefragmentapp.model;

/**
 * Created by kato on 14/11/25.
 */
public class ModelManager {
    MovieModel movieModel;

    private static ModelManager ourInstance = new ModelManager();

    public static ModelManager getInstance() {
        return ourInstance;
    }

    private ModelManager() {
        movieModel = new MovieModel();
    }

    public MovieModel getMovieModel() {
        return movieModel;
    }
}
