package com.replaybank.moviefragmentapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.replaybank.moviefragmentapp.R;
import com.replaybank.moviefragmentapp.common.BusHolder;
import com.replaybank.moviefragmentapp.model.ModelManager;
import com.replaybank.moviefragmentapp.entity.Movie;
import com.replaybank.moviefragmentapp.model.MovieModel;
import com.replaybank.moviefragmentapp.ui.adapter.MovieArrayAdapter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    public static String EXTRA_MOVIE_ID = "com.replaybank.moviefragmentapp.EXTRA_MOVIE_ID";

    @InjectView(R.id.listView)
    ListView listView;

    MovieArrayAdapter adapter;
    MovieModel movieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        movieModel = ModelManager.getInstance().getMovieModel();

        adapter = new MovieArrayAdapter(this, R.layout.row_movies);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = (Movie) adapterView.getAdapter().getItem(position);
                Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                intent.putExtra(EXTRA_MOVIE_ID, movie.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusHolder.get().register(this);

        refreshMovies();
    }

    @Override
    protected void onPause() {
        BusHolder.get().unregister(this);
        super.onPause();
    }

    private void refreshMovies() {
        ArrayList<Movie> movies = movieModel.getMovies();
        if (movies == null) {
            movieModel.fetchMovies(this);
        } else {
            Log.i("COCOABU", "セットするよ");
            adapter.clear();
            adapter.addAll(movies);
        }
    }

    @Subscribe
    public void onResponseMovies(MovieModel model) {
        if (model == movieModel) {
            refreshMovies();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
