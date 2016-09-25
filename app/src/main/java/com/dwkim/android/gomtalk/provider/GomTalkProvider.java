package com.dwkim.android.gomtalk.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.dwkim.gomtalk.provider.*;
import com.example.dwkim.gomtalk.provider.GomTalkProviderContract;


public class GomTalkProvider extends ContentProvider {
    private static final String AUTHORITY = "gomtalk";
    private static final int MATCH_THREADS = 1;

    private static final int MATCH_THREAD_ID = 2;
    private static final int MATCH_THREAD_RECIPIENTS = 3;
    private static final int MATCH_THREAD_MESSAGES = 4;
    private static final int MATCH_MESSAGE = 5;
    private static final int MATCH_MESSAGES = 6;

    public static final String SQL_CREATE_MESSAGES = "create table messages (" +
            com.dwkim.android.gomtalk.provider.GomTalkProviderContract.Messages._ID + " INTEGER PRIMARY KEY," +
            com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.THREAD_ID + " INTEGER," +
            com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.TRANSPORT_TYPE + " TEXT," +
            com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.CONTENT + " TEXT," +
            com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.CONTENT_TYPE + " TEXT," +
            com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.DATE + " INTEGER," +
            com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.STATUS + " INTEGER," +
            com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.READ + " INTEGER)";


    private GomTalkDatabaseHelper mDatabase;
    private UriMatcher mUriMatcher;

    public GomTalkProvider() {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.TABLE_NAME, MATCH_THREADS);//content://gomtalk/threads
        mUriMatcher.addURI(AUTHORITY, com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.TABLE_NAME + "/#", MATCH_THREAD_ID);//content://gomtalk/threads/1
        mUriMatcher.addURI(AUTHORITY, com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.TABLE_NAME + "/recipients/*", MATCH_THREAD_RECIPIENTS);//content://gomtalk/threads/recipients/123456789
        mUriMatcher.addURI(AUTHORITY, com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.TABLE_NAME + "/#/" + com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.TABLE_NAME, MATCH_THREAD_MESSAGES);//content://gomtalk/threads/1/messages
        mUriMatcher.addURI(AUTHORITY, com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.TABLE_NAME + "/#", MATCH_MESSAGE);//content://gomtalk/messages/1
        mUriMatcher.addURI(AUTHORITY, com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.TABLE_NAME, MATCH_MESSAGES);//content://gomtalk/messages
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

        switch (match) {
            case MATCH_THREADS:
                table = com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.TABLE_NAME;
                break;
            case MATCH_THREAD_ID:
                table = com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.TABLE_NAME;
                selection = "(" + com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads._ID + " = ?)";
                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                break;
            case MATCH_THREAD_RECIPIENTS:
                table = com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.TABLE_NAME;
                selection = "(" + com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.RECIPIENTS + " = ?)";
                selectionArgs = new String[]{uri.getPathSegments().get(2)};
                break;
            case MATCH_THREAD_MESSAGES:
                table = com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.TABLE_NAME;
                selection = "(" + com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.THREAD_ID + " = ?)";
                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                break;
            case MATCH_MESSAGE:
                table = com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.TABLE_NAME;
                selection = "(" + Messages._ID + " = ?)";
                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                break;
            default:
                throw new UnsupportedOperationException("unsupported uri : " + uri + ", match : " + match);
        }

        Cursor cursor = mDatabase.getReadableDatabase().query(table, columns, selection, selectionArgs, null, null, orderBy);
        cursor.setNotificationUri(getContext().getContentResolver(), com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.CONTENT_URI);
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
            case MATCH_THREADS:
                table = com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.TABLE_NAME;
                baseUri = com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.CONTENT_URI;
                break;
            case MATCH_MESSAGES:
                table = com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.TABLE_NAME;
                baseUri = com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.CONTENT_URI;
                break;
            default:
                throw new UnsupportedOperationException("unsupported uri : " + uri + ", match : " + match);
        }

        if (!contentValues.containsKey("date")) {
            contentValues.put("date", System.currentTimeMillis());
        }

        long insertedId = mDatabase.getWritableDatabase().insert(table, null, contentValues);
//        getContext().getContentResolver().notifyChange(baseUri, null);
        getContext().getContentResolver().notifyChange(com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.CONTENT_URI, null);
        getContext().getContentResolver().notifyChange(com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.CONTENT_URI, null);


        return ContentUris.withAppendedId(baseUri, insertedId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        String table;

        int affected = 0;
        switch (match) {
            case MATCH_THREADS:
                table = com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.TABLE_NAME;
                break;
            case MATCH_THREAD_ID: {
                String[] threadIdArgs = new String[]{uri.getPathSegments().get(1)};
                mDatabase.getWritableDatabase().delete(com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.TABLE_NAME
                        , "(" + com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.THREAD_ID + " = ?)"
                        , threadIdArgs);

                affected = mDatabase.getWritableDatabase().delete(com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.TABLE_NAME
                        , "(" + com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads._ID + " = ?)"
                        , threadIdArgs);
                break;
            }
            case MATCH_THREAD_MESSAGES:
                table = com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.TABLE_NAME;
                selection = "(" + com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.THREAD_ID + " = ?)";
                selectionArgs = new String[]{};
                affected = mDatabase.getWritableDatabase().delete(table, selection, selectionArgs);
                break;
            case MATCH_MESSAGE:
                table = com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.TABLE_NAME;
                selection = "(" + Messages._ID + " = ?)";
                selectionArgs = new String[]{uri.getPathSegments().get(1)};

                affected = mDatabase.getWritableDatabase().delete(table, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("unsupported uri : " + uri + ", match : " + match);
        }


        if (affected > 0) {
            getContext().getContentResolver().notifyChange(com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.CONTENT_URI, null);
        }
        return affected;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        String table;
        Uri baseUri;

        switch (match) {
            case MATCH_THREAD_ID:
                table = com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.TABLE_NAME;
                baseUri = com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads.CONTENT_URI;
                selection = "(" + com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Threads._ID + " = ?)";
                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                break;
            case MATCH_MESSAGE:
                table = com.example.dwkim.gomtalk.provider.GomTalkProviderContract.Messages.TABLE_NAME;
                baseUri = GomTalkProviderContract.Messages.CONTENT_URI;
                selection = "(" + Messages._ID + " = ?)";
                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                break;
            default:
                throw new UnsupportedOperationException("unsupported uri : " + uri + ", match : " + match);
        }

        int affected = mDatabase.getWritableDatabase().update(table, contentValues, selection, selectionArgs);

        if (affected > 0) {
            getContext().getContentResolver().notifyChange(baseUri, null);
        }
        return affected;
    }

    public static class Messages implements BaseColumns {

    }

}
