package com.example.dwkim.gomtalk;

import android.database.Cursor;
import android.nfc.Tag;

import com.example.dwkim.gomtalk.util.MessageUtils;

/**
 * Created by kdw on 2016-03-10.
 */
public class Log {
    public static void d(String tag, Cursor cursor) {
        if(cursor == null) {
            d(tag, "========= dump cursor is NULL =========");
            return;
        }

        d(tag, "=========dump cursor start : count : " + cursor.getCount() + " =========");
        String[] columnNameArray = cursor.getColumnNames();
        String columnNames = MessageUtils.join(columnNameArray, " | ");
        d(tag, "== " + columnNames + " ==");


        int previousPosition = cursor.getPosition();
        cursor.moveToPosition(-1);
        int columnCount = cursor.getColumnCount();
        StringBuilder sb = new StringBuilder();

        int num = 0;
        while(cursor.moveToNext()) {
            for(int i=0; i<columnCount; i++) {
                sb.append(cursor.getString(i));
                if(i != 0) {
                    sb.append(", ");
                }
            }
            d(tag,  num + " : " + sb.toString());
            num++;
        }

        cursor.moveToPosition(previousPosition);
        d(tag, "=========dump cursor end =========");
    }

    public static void d(String tag, String message) {
        android.util.Log.d(tag, message);
    }

    public static void v(String tag, String msg) {
        android.util.Log.v(tag, msg);
    }
}
