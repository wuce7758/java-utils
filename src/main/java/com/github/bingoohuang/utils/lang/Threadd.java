package com.github.bingoohuang.utils.lang;

import java.util.concurrent.TimeUnit;

public class Threadd {
    public static void sleepMilis(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
        }
    }

    public static void sleep(long time, TimeUnit timeUnit) {
        sleepMilis(timeUnit.toMillis(time));
    }
}
