package com.dwkim.android.gomtalk.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.dwkim.gomtalk.provider.GomTalkProviderContract;

/**
 * Created by dwkim on 2015-06-21.
 */
public class ConversationListModel {
    private static final Uri URI_CONVERSATION_LIST = GomTalkProviderContract.Threads.CONTENT_URI;
    private static final String DEFAULT_SORT_ORDER = GomTalkProviderContract.Threads.DATE + " DESC";
    private ModelCallback mCallback;
    private QueryConversationListTask mQueryConversationListTask;

    public void bind(ModelCallback callback) {
        mCallback = callback;
    }

    public void unbind() {
        mCallback = null;
        cancelQueryConversationListTask();
    }

    private void cancelQueryConversationListTask() {
        if(mQueryConversationListTask != null) {
            mQueryConversationListTask.cancel(false);
        }
    }

    public void queryConversationList(Context context) {
        cancelQueryConversationListTask();
        mQueryConversationListTask = new QueryConversationListTask();
        mQueryConversationListTask.execute(context);
    }

    private class QueryConversationListTask extends AsyncTask<Context, Object, Cursor> {

        @Override
        protected Cursor doInBackground(Context... params) {
            Context context = params[0];
            return context.getContentResolver().query(
                    URI_CONVERSATION_LIST
                    , null, null, null, DEFAULT_SORT_ORDER);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            if(mCallback != null) {
                mCallback.onComplete(cursor);
            }
        }
    }
}
