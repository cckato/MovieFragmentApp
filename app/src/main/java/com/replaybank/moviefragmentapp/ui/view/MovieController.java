package com.replaybank.moviefragmentapp.ui.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.replaybank.moviefragmentapp.R;
import com.replaybank.moviefragmentapp.util.TimerUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by kato on 14/11/26.
 */
public class MovieController extends RelativeLayout {

    /**
     * 動画コントロール用インターフェース
     * 利用側でこれを実装後、MovieControllerインスタンスに埋め込まないといけない
     * */
    public interface MovieControl {
        public void start();
        public void pause();
        public void seekTo(int i);
        public boolean isPlaying();
        public void setRequestOrientation(int orientation);
    }

    @InjectView(R.id.imageView_play) ImageView imageViewPlay;
    @InjectView(R.id.imageView_fullscreen) ImageView imageViewFullscreen;
    @InjectView(R.id.textView_current_time) TextView textViewCurrentTime;
    @InjectView(R.id.textView_total_time) TextView textViewTotalTime;
    @InjectView(R.id.seekBar) SeekBar seekBar;

    /** 動画タイマーのインターバル */
    private static final int TIMER_INTERVAL_ms = 100;

    Context context;
    MovieControl control;
    Timer timer;
    Handler handler = new Handler();
    int time_ms = 0;
    int total_time_ms;


    public MovieController(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.layout_movie_controller, this);
        ButterKnife.inject(this, view);

        setTotalTime((8 * 60 + 21) * 1000);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    public void setMovieControl(MovieControl control) {
        this.control = control;
    }

    public void setTotalTime(int msec) {
        total_time_ms = msec;
        textViewTotalTime.setText(TimerUtils.parseTime(msec));
    }

    @OnClick(R.id.imageView_play)
    public void onClickPlay() {
        if (control.isPlaying()) {
            pause();
        } else {
            start();
        }
    }

    @OnClick(R.id.imageView_fullscreen)
    public void onClickFullscreen() {
        Configuration config = context.getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            control.setRequestOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            control.setRequestOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    public void pause() {
        control.pause();
        stopTimer();
        imageViewPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
    }

    public void start() {
        control.start();
        startTimer();
        imageViewPlay.setImageResource(R.drawable.ic_pause_white_48dp);
    }

    /** フルスクリーンのトグルボタンを、画面状態に合わせて変更 */
    public void changeFullscreenButton(int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            imageViewFullscreen.setImageResource(R.drawable.ic_fullscreen_white_48dp);
        } else {
            imageViewFullscreen.setImageResource(R.drawable.ic_fullscreen_exit_white_48dp);
        }
    }

    /** シークバーのリスナ */
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser)
                return;
            time_ms = (int) ((progress * total_time_ms) / 100.0);
            textViewCurrentTime.setText(TimerUtils.parseTime(time_ms));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (control.isPlaying()) {
                pause();
            }
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            start();
            control.seekTo(time_ms);
        }
    };

    //-----------------------------------------------------------
    // タイマー関連
    //-----------------------------------------------------------
    /** インターバル毎に呼ばれる */
    private void updateTimer() {
        textViewCurrentTime.setText(TimerUtils.parseTime(time_ms));
        seekBar.setProgress(TimerUtils.getPercentage(time_ms, total_time_ms));
    }

    /** 動画のタイマースタート */
    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        time_ms += TIMER_INTERVAL_ms;
                        updateTimer();
                    }
                });
            }
        }, TIMER_INTERVAL_ms, TIMER_INTERVAL_ms);
    }

    /** 動画のタイマー廃棄 */
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }



}
