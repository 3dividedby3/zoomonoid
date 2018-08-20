package com.gh.game.platformer.simpletracker;

import android.graphics.drawable.Drawable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DrawableCacheManager {

    private final Map<String, Drawable> drawableCache;

    public DrawableCacheManager() {
        drawableCache = Collections.synchronizedMap(new HashMap<String, Drawable>());
    }

    public boolean contains(String key) {
        return drawableCache.containsKey(key);
    }

    public void put(String key, Drawable drawable) {
        if (drawableCache.put(key, drawable) != null) {
            System.out.println("WARNING: replacing value for key(this should not happen, check code): " + key);
        }
    }

    public Drawable get(String key) {
        return drawableCache.get(key);
    }
}
