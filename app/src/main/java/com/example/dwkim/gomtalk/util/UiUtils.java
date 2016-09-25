package com.example.dwkim.gomtalk.util;

import android.view.View;

/**
 * Created by dwkim on 2015-06-21.
 */
public class UiUtils {

    public static void fadeOut(final View view, int duration) {
        if(view.getVisibility() == View.GONE
                || view.getAnimation() != null) {
            return;
        }
        view.animate().alpha(0f).setDuration(duration).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        }).start();
    }

    public static void fadeIn(View view, int duration) {
        if(view.getVisibility() == View.VISIBLE
                || view.getAnimation() != null) {
            return;
        }
        view.setAlpha(0);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setDuration(duration).start();
    }

    public static void visible(View view) {
        if(view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void gone(View view) {
        if(view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
        }
    }
}
