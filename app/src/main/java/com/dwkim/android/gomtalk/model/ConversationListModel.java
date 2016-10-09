package com.dwkim.android.gomtalk.model;

import android.database.Cursor;
import android.os.AsyncTask;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by dwkim on 2015-06-21.
 */
public class ConversationListModel {
    private final MessageStorage mMessageStorage;
    private ModelCallback mCallback;
    private AsyncTask mQueryConversationListTask;

    public ConversationListModel(MessageStorage storage) {
        mMessageStorage = storage;
    }

    public void release() {
    }

    public void loadConversationList(final ConversationListLoadCallback callback) {
        assertNotNull(callback);

        if(mQueryConversationListTask != null) {
            mQueryConversationListTask.cancel(false);
        }

        mQueryConversationListTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                return mMessageStorage.queryConversationList();
            }

            @Override
            protected void onPostExecute(Object o) {
                callback.onConversationListLoaded((Cursor)o);
            }
        };

        System.out.println("loadConversationList");
    }

    public interface ConversationListLoadCallback {
        void onConversationListLoaded(Cursor cursor);
    }
}
