package com.replaybank.moviefragmentapp.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.replaybank.moviefragmentapp.R;
import com.replaybank.moviefragmentapp.entity.Movie;
import com.replaybank.moviefragmentapp.ui.helper.MovieFragmentHelper;
import com.replaybank.moviefragmentapp.ui.view.MovieController;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MovieFragment extends Fragment implements SurfaceHolder.Callback {

    Movie movie;
    MediaPlayer mediaPlayer;
    SurfaceHolder surfaceHolder;

    @InjectView(R.id.surfaceView) SurfaceView surfaceView;
    @InjectView(R.id.container_movie_fragment) RelativeLayout container;
    @InjectView(R.id.progressBar) ProgressBar progressBar;
    @InjectView(R.id.movie_controller) MovieController movieController;

    boolean canPlay = false;

    public MovieFragment() {
    }

    public MovieFragment(Movie movie) {
        this.movie = movie;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.inject(this, rootView);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        movieController.setMovieControl(control);
        movieController.setVisibility(View.INVISIBLE);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        MovieFragmentHelper.changeMovieSize(getActivity(), container);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        MovieFragmentHelper.adjustWindowFlagsToOrientation(getActivity(), newConfig.orientation);
        MovieFragmentHelper.changeMovieSize(getActivity(), container);
        movieController.changeFullscreenButton(newConfig.orientation);
    }

    @Override
    public void onPause() {
        super.onPause();
        movieController.pause();
    }

    /** 動画再生開始 */
    public void startMovie() {
        canPlay = true;
        progressBar.setVisibility(View.GONE);
        movieController.start();

    }

    @OnClick(R.id.surfaceView)
    public void onClickControllers() {
        if (canPlay == false) return;

        if (movieController.getVisibility() == View.VISIBLE) {
            movieController.setVisibility(View.INVISIBLE);
        } else {
            movieController.setVisibility(View.VISIBLE);
        }
    }

    //-----------------------------------------------------------
    // コントロール関連
    //-----------------------------------------------------------
    /** 動画のコントロールインターフェース。MovieControllerにsetして使用 */
    MovieController.MovieControl control = new MovieController.MovieControl() {
        @Override
        public void start() {
            mediaPlayer.start();
        }
        @Override
        public void pause() {
            mediaPlayer.pause();
        }
        @Override
        public void seekTo(int i) {
            mediaPlayer.seekTo(i);
        }
        @Override
        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }
        @Override
        public void setRequestOrientation(int orientation) {
            getActivity().setRequestedOrientation(orientation);
        }
    };

    //-----------------------------------------------------------
    // SurfaceHolderのコールバック
    //-----------------------------------------------------------
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getActivity(), Uri.parse(movie.getBodyUrl()));
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                    Log.i("COCOABU", "エラー、what=" + what + ", extra=" + extra);
                    return false;
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Log.i("COCOABU", "準備完了");
                    startMovie();
                }
            });
        } catch (IOException e) {
            Log.e("COCOABU", "URIのパースに失敗");
            e.printStackTrace();
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}

