package com.codepath.com.sffoodtruck.ui.common;

import java.util.concurrent.atomic.AtomicInteger;

public class ActivityRequestCodeGenerator {
    private static final AtomicInteger seed = new AtomicInteger(100);
    public static int getFreshInt() {
        return seed.incrementAndGet();
    }
}