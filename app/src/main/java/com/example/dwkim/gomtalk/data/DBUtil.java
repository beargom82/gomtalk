package com.example.dwkim.gomtalk.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.example.dwkim.gomtalk.provider.GomTalkProviderContract;

/**
 * Created by dwkim on 2015-06-14.
 */

public class DBUtil {
    public static final String[] ID_PROJECTION = new String[]{BaseColumns._ID};
    public static final int FALSE = 0;
    public static final int TRUE = 1;

    public static long getOrCreateThreadId(Context context, String[] addresses){
        String semiseperatedAddresses = TextUtils.join(";", addresses);
        Cursor threadsCursor = null;
        int threadsCount = 0;
        long threadId = -1l;

        try {
            threadsCursor = context.getContentResolver().query(GomTalkProviderContract.Threads.CONTENT_URI,
                    ID_PROJECTION,
                    GomTalkProviderContract.Threads.RECIPIENTS + " = ?",
                    new String[]{semiseperatedAddresses},
                    null);

            threadsCount = threadsCursor.getCount();
            if(threadsCursor.moveToFirst()){
                threadId = threadsCursor.getLong(0);
            }

        }finally {
            if(threadsCursor != null){
                threadsCursor.close();
            }
        }

        if (threadsCount == 0) {
            ContentValues values = new ContentValues();
            values.put(GomTalkProviderContract.Threads.RECIPIENTS, semiseperatedAddresses);
            values.put(GomTalkProviderContract.Threads.DATE, System.currentTimeMillis());
            Uri insertedUri = context.getContentResolver().insert(GomTalkProviderContract.Threads.CONTENT_URI, values);
            threadId = ContentUris.parseId(insertedUri);

        } else if (threadsCursor.getCount() == 1) {
            //NOOP
        } else {
            throw new IllegalStateException("threas count cant be " + threadsCursor.getCount());
        }

        return threadId;
    }

    public static boolean isValid(long id) {
        return id > 0l;
    }

    public static int deleteMessage(ContentResolver contentResolver, long id) {
        Uri uri = ContentUris.withAppendedId(GomTalkProviderContract.Messages.CONTENT_URI, id);
        return contentResolver.delete(uri, null, null);
    }

    public static int deleteThread(ContentResolver contentResolver, long id) {
        Uri uri = ContentUris.withAppendedId(GomTalkProviderContract.Threads.CONTENT_URI, id);
        return contentResolver.delete(uri, null, null);
    }
}
