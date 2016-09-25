package com.dwkim.android.gomtalk.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;


public class GomTalkProviderContract  {
    public static final String AUTHORITY = "gomtalk";

    public static class Messages {
        public static final String TABLE_NAME = "messages";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String _ID = "_id";
        public static final String THREAD_ID = "thread_id";
        public static final String TRANSPORT_TYPE = "transport_type";
        public static final String CONTENT = "content";
        public static final String CONTENT_TYPE = "content_type";
        public static final String DATE = "date";

        public static final String STATUS = "status";
        public static final String READ = "read";

        public static final int STATUS_RECEIVED = 1;
        public static final int STATUS_SENT = 2;

        //_id, thread_id, content, content_type, date, status, read
        public static final String[] ALL_PROJECTION = new String[]{
                THREAD_ID, TRANSPORT_TYPE, CONTENT, CONTENT_TYPE, DATE, STATUS, READ
        };

        public static final int COL_ID = 0;
        public static final int COL_THREAD_ID = 1;
        public static final int COL_TRANSPORT_TYPE = 2;
        public static final int COL_CONTENT = 3;
        public static final int COL_CONTENT_TYPE = 4;
        public static final int COL_DATE = 5;
        public static final int COL_STATUS = 6;
        public static final int COL_READ = 7;
    }

    public static class Threads implements BaseColumns {
        public static final String TABLE_NAME = "threads";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
        //_id, recipientS, snippet, date, count
        public static final String RECIPIENTS = "recipients";
        public static final String SNIPPET = "snippet";
        public static final String DATE = "date";
        public static final String COUNT = "count";

        public static final int COL_ID = 0;
        public static final int COL_RECIPIENTS = 1;
        public static final int COL_SNIPPET = 2;
        public static final int COL_DATE = 3;
        public static final int COL_COUNT = 4;

        public static final String[] ALL_PROJECTION = new String[]{
                _ID, RECIPIENTS, SNIPPET, DATE, COUNT
        };

        public static final String SQL_CREATE = "create table threads (" +
                "_id INTEGER PRIMARY KEY, " +
                "recipients TEXT," +
                "snippet TEXT," +
                "date INTEGER," +
                "count INTEGER)";
    }
}
