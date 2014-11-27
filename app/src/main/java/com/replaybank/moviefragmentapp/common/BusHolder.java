package com.replaybank.moviefragmentapp.common;

import com.squareup.otto.Bus;

/**
 * Created by kato on 14/11/09.
 */
public class BusHolder {
    private static Bus instance = new Bus();
    public static Bus get() {
        return instance;
    }
    private BusHolder(){}
}
