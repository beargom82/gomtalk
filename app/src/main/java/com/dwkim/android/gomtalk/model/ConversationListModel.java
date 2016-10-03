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
    private final Context mContext;
    private ModelCallback mCallback;
    private QueryConversationListTask mQueryConversationListTask;

    public ConversationListModel(Context context) {
        mContext = context;
    }

    private void cancelQueryConversationListTask() {
        if(mQueryConversationListTask != null) {
            mQueryConversationListTask.cancel(false);
        }
    }

    public void queryConversationList(ModelCallback modelCallback) {
        cancelQueryConversationListTask();
        mQueryConversationListTask = new QueryConversationListTask(modelCallback);
        mQueryConversationListTask.execute(mContext);
    }

    private class QueryConversationListTask extends AsyncTask<Context, Object, Cursor> {
        private ModelCallback mCallback;
        public QueryConversationListTask(ModelCallback modelCallback) {
            mCallback = modelCallback;
        }

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

            mCallback.onComplete(ModelCallback.RESULT_OK, ModelCallback.ERRORCODE_NONE, cursor);
        }
    }
}
