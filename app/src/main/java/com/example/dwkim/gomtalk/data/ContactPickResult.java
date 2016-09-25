package com.example.dwkim.gomtalk.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;

import com.example.dwkim.gomtalk.Log;

/**
 * Created by kdw on 2016-03-10.
 */
public class ContactPickResult {
    private static final String TAG = "GomTalk/ContactPickResult";

    private static final String[] PROJECTION =
            new String[]{
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            };
    private static final int COL_DISPLAY_NAME = 0;
    private static final int COL_NUMBER = 1;

    private final String mName;
    private final String mNumber;

    public String getName() { return mName;}
    public String getNumber() { return mNumber;}

    public static ContactPickResult fromPickContact(Context context, Uri uri) {
        if(uri == null) {
            return null;
        }

        ContactPickResult result = null;

        try(Cursor cursor = context.getContentResolver().query(uri, PROJECTION, null, null, null)) {
            Log.d(TAG, cursor);

            if(cursor.moveToFirst()) {
                result = new ContactPickResult(
                        cursor.getString(COL_DISPLAY_NAME)
                        , cursor.getString(COL_NUMBER));
                Log.v(TAG, "name / number = " + result.mName + " / " + result.mNumber);
            }
        }

        return result;
    }

    private ContactPickResult(String name, String number) {
        mName = name;
        mNumber = number;
    }

    public static Intent createPickContactIntent() {
//        Intent result = new Intent( Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI );
        Intent result = new Intent( Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI );
        //intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        return result;
    }
}
