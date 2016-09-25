package com.example.dwkim.gomtalk.conversationview;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.dwkim.gomtalk.provider.GomTalkProviderContract;

/**
 * Created by kdw on 2016-02-14.
 */
public class MessageItem {
    private static final String TAG = "GomTalk/MessageItem";
    private static final String DEFAULT_SORT_ORDER = GomTalkProviderContract.Threads.DATE + " ASC";
    public final long id;
    public final String content;
    public final String contentType;
    public final long date;
    public final int status;
    public final int read;
    public final long threadId;
    public final String transportType;

    public MessageItem(long _id, String _content, String _contentType, long _date, int _status, int _read, long _threadId, String _transportType) {
        id = _id;
        content = _content;
        contentType = _contentType;
        date = _date;
        status = _status;
        read = _read;
        threadId = _threadId;
        transportType = _transportType;
    }

    public static boolean isSentMessage(int status) {
        return status != GomTalkProviderContract.Constants.STATUS_RECEIVED;
    }

    public static MessageItem from(Cursor cursor) {
        return new MessageItem(
                cursor.getLong(cursor.getColumnIndexOrThrow(GomTalkProviderContract.Messages._ID)) ,
                cursor.getString(cursor.getColumnIndexOrThrow(GomTalkProviderContract.Messages.CONTENT)) ,
                cursor.getString(cursor.getColumnIndexOrThrow(GomTalkProviderContract.Messages.CONTENT_TYPE)) ,
                cursor.getLong(cursor.getColumnIndexOrThrow(GomTalkProviderContract.Messages.DATE)) ,
                cursor.getInt(cursor.getColumnIndexOrThrow(GomTalkProviderContract.Messages.STATUS)) ,
                cursor.getInt(cursor.getColumnIndexOrThrow(GomTalkProviderContract.Messages.READ)) ,
                cursor.getLong(cursor.getColumnIndexOrThrow(GomTalkProviderContract.Messages.THREAD_ID)) ,
                cursor.getString(cursor.getColumnIndexOrThrow(GomTalkProviderContract.Messages.TRANSPORT_TYPE))
                );
    }

    static Cursor queryMessageList(Context context, long threadId) {
        //content://gomtalk/threads/1/messages
        Uri uri = Uri.withAppendedPath(ContentUris.withAppendedId(GomTalkProviderContract.Threads.CONTENT_URI, threadId),
                GomTalkProviderContract.Messages.TABLE_NAME);
        Log.d(TAG, "query " + uri);
        return context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                DEFAULT_SORT_ORDER);
    }
}
