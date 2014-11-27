package com.replaybank.moviefragmentapp.ui.helper;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import com.replaybank.moviefragmentapp.util.DisplayUtils;

/**
 * Created by kato on 14/11/26.
 */
public class MovieFragmentHelper {

    /** 画面の横幅から16:9の割合で動画のサイズを決める */
    public static void changeMovieSize(Context context, ViewGroup container) {
        // 画面の横幅
        int displayWidth = DisplayUtils.getDisplaySize(context).x;
        ViewGroup.LayoutParams params = container.getLayoutParams();
        params.width = displayWidth;
        params.height = displayWidth * 9/16;
        container.setLayoutParams(params);
    }

    /** Landscapeモードのときは動画をフルスクリーンにする */
    public static void adjustWindowFlagsToOrientation(Activity activity, int orientation) {
        Window window = activity.getWindow();
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
}
