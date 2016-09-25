package com.example.dwkim.gomtalk.conversationview;

import android.content.Context;
import android.database.Cursor;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import com.example.dwkim.gomtalk.provider.GomTalkProvider;
import com.example.dwkim.gomtalk.provider.GomTalkProviderContract;

/**
* Created by dwkim on 2015-06-07.
*/
class MessageListAdapter extends SimpleCursorAdapter {
    private static final String TAG = "GomTalk/MsgListAdapter";

    private final Context mContext;
    private long mThreadId;
    private MessageListUpdateListener mUpdateListener;

    public void setOnUpdateListener(MessageListUpdateListener updateListener) {
        mUpdateListener = updateListener;
    }

    public void release() {
        changeCursor(null);
        mUpdateListener = null;
    }

    public interface MessageListUpdateListener {

        void beforeRefresh(MessageListAdapter adapter);

        void afterRefresh(MessageListAdapter adapter);
    }


    public MessageListAdapter(Context context, long threadId) {
        super(context,
                android.R.layout.simple_list_item_2,
                null,
                new String[]{GomTalkProviderContract.Messages.CONTENT, GomTalkProviderContract.Messages.DATE},
                new int[]{android.R.id.text1, android.R.id.text2},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mContext = context;
        mThreadId = threadId;
    }

    @Override
    protected void onContentChanged() {
        refresh();
    }

    private void refresh() {
        if(mUpdateListener != null) {
            mUpdateListener.beforeRefresh(this);
        }

        if(mThreadId < 0){
            changeCursor(null);
            if(mUpdateListener != null) {
                mUpdateListener.afterRefresh(this);
            }
            return;
        }

        //content://gomtalk/threads/1/messages
        Cursor cursor = MessageItem.queryMessageList(mContext, mThreadId);
        changeCursor(cursor);

        if(mUpdateListener != null) {
            mUpdateListener.afterRefresh(this);
        }
    }

    public void load(long threadId) {
        if(mThreadId == threadId) {
            return;
        }

        mThreadId = threadId;
        refresh();
    }
}
