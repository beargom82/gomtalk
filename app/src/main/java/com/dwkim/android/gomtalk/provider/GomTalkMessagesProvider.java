package com.dwkim.android.gomtalk.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class GomTalkMessagesProvider extends ContentProvider {
    private static final String TAG = "GomTalk/MessagesProvider";
    private static final String AUTHORITY = "gomtalkmessages";
    private static final String TABLE_MESSAGES =  GomTalkProviderContract.Messages.TABLE_NAME;

    private static final int MATCH_MESSAGES_ALL = 1;
    private static final int MATCH_MESSAGES_ALL_ID = 2;
    private static final int MATCH_MESSAGES_ALL_THREAD = 3;
    private static final int MATCH_MESSAGES_INBOX = 4;
    private static final int MATCH_MESSAGES_OUTBOX = 5;//queued, sending, sent
    private static final int MATCH_MESSAGES_QUEUED = 6;
    private static final int MATCH_MESSAGES_SENDING = 7;
    private static final int MATCH_MESSAGES_SENT = 8;
    private static final int MATCH_MESSAGES_DRAFT_THREAD = 9;

    private GomTalkDatabaseHelper mDatabase;
    private UriMatcher mUriMatcher;

    public GomTalkMessagesProvider() {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, null, MATCH_MESSAGES_ALL);//content://gomtalkmessages
        mUriMatcher.addURI(AUTHORITY, "#", MATCH_MESSAGES_ALL_ID);//content://gomtalkmessages/1
        mUriMatcher.addURI(AUTHORITY, "#/threads", MATCH_MESSAGES_ALL_THREAD);//content://gomtalkmessages/1/threads
        mUriMatcher.addURI(AUTHORITY, "inbox", MATCH_MESSAGES_INBOX);//content://gomtalkmessages/inbox
        mUriMatcher.addURI(AUTHORITY, "outbox", MATCH_MESSAGES_OUTBOX);//content://gomtalkmessages/outbox
        mUriMatcher.addURI(AUTHORITY, "queued", MATCH_MESSAGES_QUEUED);//content://gomtalkmessages/queued
        mUriMatcher.addURI(AUTHORITY, "sending", MATCH_MESSAGES_SENDING);//content://gomtalkmessages/sending
        mUriMatcher.addURI(AUTHORITY, "sent", MATCH_MESSAGES_SENT);//content://gomtalkmessages/sent
        mUriMatcher.addURI(AUTHORITY, "#/draft", MATCH_MESSAGES_DRAFT_THREAD);//content://gomtalkmessages/1/draft
    }

    @Override
    public boolean onCreate() {
        mDatabase = new GomTalkDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String orderBy) {
        int match = mUriMatcher.match(uri);
        String table;

        Log.d(TAG, "uri = " + uri + ", match = " + match);
        switch (match) {
            case MATCH_MESSAGES_ALL:
                table = GomTalkProviderContract.Threads.TABLE_NAME;
                break;
            case MATCH_MESSAGES_ALL_ID:
//                table = GomTalkProviderContract.Threads.TABLE_NAME;
//                selection = "(" + GomTalkProviderContract.Threads._ID + " = ?)";
//                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                break;
            case MATCH_MESSAGES_ALL_THREAD:
                break;
            case MATCH_MESSAGES_INBOX:
                break;
            case MATCH_MESSAGES_OUTBOX:
                break;
            case MATCH_MESSAGES_QUEUED:
                break;
            case MATCH_MESSAGES_SENDING:
                break;
            case MATCH_MESSAGES_SENT:
                break;
            case MATCH_MESSAGES_DRAFT_THREAD:
                break;
            default:
                throw new UnsupportedOperationException("unsupported uri : " + uri + ", match : " + match);
        }

        Cursor cursor = null;
//        Cursor cursor = mDatabase.getReadableDatabase().query(table, columns, selection, selectionArgs, null, null, orderBy);
//        cursor.setNotificationUri(getContext().getContentResolver(), GomTalkProviderContract.Threads.CONTENT_URI);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = mUriMatcher.match(uri);
        String table;
        Uri baseUri;

        switch (match) {
            case MATCH_MESSAGES_ALL:
                table = GomTalkProviderContract.Threads.TABLE_NAME;
                break;
        }
//        switch (match) {
//            case MATCH_THREADS:
//                table = GomTalkProviderContract.Threads.TABLE_NAME;
//                baseUri = GomTalkProviderContract.Threads.CONTENT_URI;
//                break;
//            case MATCH_MESSAGES:
//                table = GomTalkProviderContract.Messages.TABLE_NAME;
//                baseUri = GomTalkProviderContract.Messages.CONTENT_URI;
//                break;
//            default:
//                throw new UnsupportedOperationException("unsupported uri : " + uri + ", match : " + match);
//        }
//
//        if (!contentValues.containsKey("date")) {
//            contentValues.put("date", System.currentTimeMillis());
//        }
//
//        long insertedId = mDatabase.getWritableDatabase().insert(table, null, contentValues);
////        getContext().getContentResolver().notifyChange(baseUri, null);
//        getContext().getContentResolver().notifyChange(GomTalkProviderContract.Threads.CONTENT_URI, null);
//        getContext().getContentResolver().notifyChange(GomTalkProviderContract.Messages.CONTENT_URI, null);


//        return ContentUris.withAppendedId(baseUri, insertedId);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        String table;

        int affected = 0;
        switch (match) {
            case MATCH_MESSAGES_ALL:
                table = GomTalkProviderContract.Threads.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("unsupported uri : " + uri + ", match : " + match);
        }


        if (affected > 0) {
//            getContext().getContentResolver().notifyChange(GomTalkProviderContract.Threads.CONTENT_URI, null);
        }
        return affected;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        String table;
        Uri baseUri = null;

        switch (match) {
            case MATCH_MESSAGES_ALL:
                table = GomTalkProviderContract.Threads.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("unsupported uri : " + uri + ", match : " + match);
        }

        int affected = 0;//mDatabase.getWritableDatabase().update(table, contentValues, selection, selectionArgs);

        if (affected > 0) {
            getContext().getContentResolver().notifyChange(baseUri, null);
        }
        return affected;
    }
}
