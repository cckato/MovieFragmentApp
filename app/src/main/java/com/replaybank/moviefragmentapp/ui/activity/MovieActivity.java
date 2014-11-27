package com.replaybank.moviefragmentapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.replaybank.moviefragmentapp.R;
import com.replaybank.moviefragmentapp.entity.Movie;
import com.replaybank.moviefragmentapp.model.ModelManager;
import com.replaybank.moviefragmentapp.model.MovieModel;
import com.replaybank.moviefragmentapp.ui.fragment.MovieFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MovieActivity extends ActionBarActivity {
    private static final int INVALID_MOVIE_ID = -1;

    Movie movie = new Movie();
    MovieModel movieModel;

    @InjectView(R.id.textView_name)
    TextView textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.inject(this);
        movieModel = ModelManager.getInstance().getMovieModel();

        Intent intent = getIntent();
        int movieId = intent.getIntExtra(MainActivity.EXTRA_MOVIE_ID, INVALID_MOVIE_ID);
        if (movieId != INVALID_MOVIE_ID) {
            movie = movieModel.getMovie(movieId);
            textViewName.setText(movie.getName());
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.movie_container, new MovieFragment(movie))
                .commit();
    }

}
