package com.example.dwkim.gomtalk.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.dwkim.gomtalk.MimeTypes;
import com.example.dwkim.gomtalk.provider.GomTalkProviderContract;

/**
 * Created by dwkim on 2015-06-20.
 */
public class SmsData {

    public static final int STATUS_RECEIVED = 0;
    public static final int STATUS_DRAFT = 1;
    public static final int STATUS_QUEUED = 2;
    public static final int STATUS_SENDING = 3;
    public static final int STATUS_FAILED = 4;
    public static final int STATUS_SENT = 5;

    public static final String TRANSPORT_TYPE = "sms";

    private final long mThreadId;
    private final String mMessageText;
    private final int mStatus;
    private Uri mUri;

    public SmsData(long threadId, String messageText, int status) {
        mThreadId = threadId;
        mMessageText = messageText;
        mStatus = status;
    }

    public SmsData(Context context, Uri uri) {
        mUri = uri;

        Cursor cursor = null;
        try {
            context.getContentResolver().query(uri, GomTalkProviderContract.Messages.ALL_PROJECTION, null, null, null);
            if (!cursor.moveToFirst()) {
                throw new IllegalStateException("No message data for " + uri);
            }

            mThreadId = cursor.getLong(GomTalkProviderContract.Messages.COL_THREAD_ID);
            mMessageText = cursor.getString(GomTalkProviderContract.Messages.COL_CONTENT);
            mStatus = cursor.getInt(GomTalkProviderContract.Messages.COL_STATUS);
        } finally {
            if( cursor != null) {
                cursor.close();
            }
        }
    }

    public void persist(Context context) {
        if(isPersist()){
            throw new IllegalStateException("Already persisted");
        }

        Uri uri = GomTalkProviderContract.Messages.CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(GomTalkProviderContract.Messages.THREAD_ID, mThreadId);
        values.put(GomTalkProviderContract.Messages.TRANSPORT_TYPE, TRANSPORT_TYPE);
        values.put(GomTalkProviderContract.Messages.CONTENT, mMessageText);
        values.put(GomTalkProviderContract.Messages.CONTENT_TYPE, MimeTypes.TEXT_PLAIN);
        values.put(GomTalkProviderContract.Messages.STATUS, mStatus);
        values.put(GomTalkProviderContract.Messages.READ, getInitialRead(mStatus));

        mUri = context.getContentResolver().insert(uri, values);
    }

    private int getInitialRead(int read) {
        switch(mStatus) {
            case STATUS_RECEIVED :
            case STATUS_FAILED ://for notifying user, set failed message unread
                read = DBUtil.FALSE;
            case STATUS_QUEUED :
            case STATUS_SENDING :
            case STATUS_SENT :
                read = DBUtil.TRUE;
        }
        return read;
    }

    public boolean isPersist(){
        return mUri != null;
    }

    public long getThreadId(){
        return mThreadId;
    }

    public String getMessageText() {
        return mMessageText;
    }

    public Uri getUri() {
        return mUri;
    }
}
