package com.replaybank.moviefragmentapp.model;

import android.content.Context;

import com.android.volley.RequestQueue;
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

//        JsonArrayRequest request = new JsonArrayRequest(HttpRoutes.MOVIES_PATH,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        movies = new ArrayList<Movie>();
//                        try {
//                            for (int i=0; i<response.length(); i++) {
//                                JSONObject obj = response.getJSONObject(i);
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//        requestQueue.add(request);


        movies = new ArrayList<Movie>();
        for (int i = 0; i < 20; i++) {
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
