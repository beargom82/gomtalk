package com.dwkim.android.gomtalk.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dwkim.gomtalk.provider.GomTalkProviderContract;

/**
 * Created by kdw on 2016-03-13.
 */
class GomTalkDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gomtalk.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "GomTalk/GomTalkDatabaseHelper";

    GomTalkDatabaseHelper(Context context) {
        // calls the super constructor, requesting the default cursor factory.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates the underlying database with table name and column names taken from the
     * NotePad class.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GomTalkProviderContract.Threads.CREATE_SQL);
        db.execSQL(GomTalkProviderContract.Messages.CREATE_SQL);
    }

    /**
     * Demonstrates that the provider must consider what happens when the
     * underlying datastore is changed. In this sample, the database is upgraded the database
     * by destroying the existing data.
     * A real application should upgrade the database in place.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Logs that the database is being upgraded
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        // Kills the table and existing data
        switch (oldVersion) {
            case 1:
                break;
            default:
                break;
        }

        // Recreates the database with a new version
        onCreate(db);
    }
}
