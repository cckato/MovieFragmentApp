package com.replaybank.moviefragmentapp.util;

/**
 * Created by kato on 14/11/25.
 */
public class TimerUtils {
    public static String parseTime(int msec) {
        int m = msec / (60 * 1000);
        int s = (int) ((msec - m * 60 * 1000) / 1000.0);
        return String.format("%02d", m) + ":" + String.format("%02d", s);
    }

    public static int getPercentage(int current, int total) {
        return (int) ((current) * 100 / (float) total);
    }
}
