package com.example.dwkim.gomtalk.provider;

import android.net.Uri;
import android.provider.BaseColumns;


public class GomTalkProviderContract  {
    public static final String AUTHORITY = "gomtalk";

    public final static class Constants {

        public static final int DB_FALSE = 0;
        public static final int DB_TRUE = 1;

        public static final String TRANSPORT_TYPE_SMS = "sms";
        public static final String TRANSPORT_TYPE_MMS = "mms";

        public static final int MESSAGE_TYPE_OUT = 1;
        public static final int MESSAGE_TYPE_IN = 2;
        public static final int MESSAGE_TYPE_DRAFT = 3;

        public static final int STATUS_NONE = 0;
        public static final int STATUS_SENDING_QUEUED = 1;
        public static final int STATUS_SENDING = 2;
        public static final int STATUS_SENT = 3;
        public static final int STATUS_RECEIVING = 4;
        public static final int STATUS_RECEIVED = 5;
    }

    public static class Messages {
        public static final String TABLE_NAME = "messages";
        public static final Uri CONTENT_URI = Uri.parse("content://" + GomTalkProviderContract.AUTHORITY + "/" + TABLE_NAME);

        public static final String _ID = "_id";
        public static final String THREAD_ID = "thread_id";
        public static final String TRANSPORT_TYPE = "transport_type";
        public static final String CONTENT = "content";
        public static final String CONTENT_TYPE = "content_type";
        public static final String DATE = "date";

        public static final String MESSAGE_TYPE = "status";
        public static final String STATUS = "status";
        public static final String READ = "read";

        public static final String CREATE_SQL = "create table messages (" +
                _ID + " INTEGER PRIMARY KEY," +
                THREAD_ID + " INTEGER," +
                TRANSPORT_TYPE + " TEXT," +
                CONTENT + " TEXT," +
                CONTENT_TYPE + " TEXT," +
                DATE + " INTEGER NOT NULL," +
                MESSAGE_TYPE + " INTEGER NOT NULL," +
                STATUS + " INTEGER DEFAULT " + Constants.STATUS_NONE +"," +
                READ + " INTEGER DEFAULT " + Constants.DB_FALSE + ")";

        //_id, thread_id, content, content_type, date, status, read
        public static final String[] ALL_PROJECTION = new String[]{
                THREAD_ID, TRANSPORT_TYPE, CONTENT, CONTENT_TYPE, DATE, MESSAGE_TYPE, STATUS, READ
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
        //_id, recipients, snippet, date, count
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

        public static final String CREATE_SQL = "create table threads (" +
                "_id INTEGER PRIMARY KEY, " +
                "recipients TEXT," +
                "snippet TEXT," +
                "date INTEGER," +
                "count INTEGER DEFAULT 0)";
    }
}
