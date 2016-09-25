package com.example.dwkim.gomtalk.conversationview;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by kdw on 2016-03-10.
 */
public class MessageBubbleListRenderer {
    private static final String TAG = "GomTalk/BubbleListRenderer";
    private static final DateFormat DATETIME_FORMAT = SimpleDateFormat.getDateTimeInstance();
    private static final DateFormat TIME_FORMAT = SimpleDateFormat.getTimeInstance();
    private static HashMap<Long, String> sFormattedDateCache = new HashMap<>();
    private static Calendar sCalendarBuffer = Calendar.getInstance();

    public static String renderDate(long datetimeMillis) {
        String result = getCached(datetimeMillis);
        if(result != null) {
            Log.d(TAG,"formatted date cache hit");
            return result;
        }
        Log.d(TAG, "formatted date cache not hit");

        sCalendarBuffer.setTimeInMillis(datetimeMillis);
        DateFormat formatter = isToday(sCalendarBuffer) ? TIME_FORMAT : DATETIME_FORMAT;
        result = formatter.format(datetimeMillis);
        caching(datetimeMillis, result);
        return result;
    }

    private static String getCached(long datetimeMillis) {
        return sFormattedDateCache.get(datetimeMillis);
    }

    private static void caching(long datetimeMillis, String formattedDate) {
        sFormattedDateCache.put(datetimeMillis, formattedDate);
    }

    private static boolean isToday(Calendar datetime) {
        Calendar now = getToday();

        return now.get(Calendar.YEAR) == datetime.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == datetime.get(Calendar.MONTH)
                && now.get(Calendar.DAY_OF_MONTH) == datetime.get(Calendar.DAY_OF_MONTH);
    }

    private static Calendar getToday() {
        return Calendar.getInstance();
    }
}
