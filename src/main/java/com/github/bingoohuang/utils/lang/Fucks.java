package com.github.bingoohuang.utils.lang;

public class Fucks {
    public static class FuckException<T extends Exception> {
        private void pleaseThrow(final Throwable t) throws T {
            throw (T) t;
        }
    }

    public static void fuck(Throwable t) {
        new FuckException<RuntimeException>().pleaseThrow(t);
    }
}
