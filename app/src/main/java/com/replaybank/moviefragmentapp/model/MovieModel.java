package com.replaybank.moviefragmentapp.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.replaybank.moviefragmentapp.common.BusHolder;
import com.replaybank.moviefragmentapp.entity.Movie;
import com.replaybank.moviefragmentapp.util.HttpRoutes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    RequestQueue requestQueue;

    public MovieModel() {
    }

    public void fetchMovies(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        } else {
            requestQueue.start();
        }

        JsonArrayRequest request = new JsonArrayRequest(HttpRoutes.MOVIES_PATH,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        movies = new ArrayList<Movie>();
                        try {
                            for (int i=0; i<response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                Movie movie = new Movie();
                                movie.setId(obj.getInt("id"));
                                movie.setName(obj.getString("name"));
                                movie.setIntroduction(obj.getString("introduction"));
                                movie.setBodyUrl(obj.getString("body_url"));
                                movie.setThumbUrl(obj.getString("thumb_url"));
                                movies.add(movie);
                                movieCache.put(movie.getId(), movie);
                            }
                            BusHolder.get().post(this);
                        } catch (JSONException e) {
                            Log.e("COCOABU", "JSONのパースに失敗したよ");
                            e.printStackTrace();
                            BusHolder.get().post(this);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("COCOABU", "Httpリクエストに失敗したよ");
                        movies = new ArrayList<Movie>();
                        BusHolder.get().post(this);
                    }
                });
        requestQueue.add(request);

    }

    public Movie getMovie(int id) {
        if (movieCache.containsKey(id)) {
            return movieCache.get(id);
        } else {
            return null;
        }
    }


}
