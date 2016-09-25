package com.example.dwkim.gomtalk.conversationview;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.dwkim.android.gomtalk.R;
import com.example.dwkim.gomtalk.provider.GomTalkProviderContract;

import java.util.HashMap;

/**
* Created by dwkim on 2015-06-07.
*/
class MessageBubbleListAdapter extends CursorAdapter {
    private static final String TAG = "GomTalk/MessageBubbleListAdapter";

    private static final String DEFAULT_SORT_ORDER = GomTalkProviderContract.Threads.DATE + " ASC";
    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_SENT = 0;
    private static final int VIEW_TYPE_RECEIVED = 1;
    private final Context mContext;
    private final HashMap<Integer, MessageItem> mMessageItemCache;
    private long mThreadId;
    private MessageListUpdateListener mUpdateListener;

    public void setOnUpdateListener(MessageListUpdateListener updateListener) {
        mUpdateListener = updateListener;
    }

    public void release() {
        changeCursor(null);
        mUpdateListener = null;
    }


    public MessageBubbleListAdapter(Context context, long threadId) {
        super(context, null, false);
        mContext = context;
        mThreadId = threadId;
        mMessageItemCache = new HashMap<>();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        switch(getItemViewType(cursor.getPosition())) {
            case VIEW_TYPE_SENT:
                return LayoutInflater.from(context).inflate(R.layout.bubble_list_item_sent, parent, false);
            case VIEW_TYPE_RECEIVED:
            default:
                return LayoutInflater.from(context).inflate(R.layout.bubble_list_item_received, parent, false);
        }
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtBody = (TextView) view.findViewById(R.id.txt_body);
        TextView txtDate = (TextView) view.findViewById(R.id.txt_date);

        MessageItem item = getCachedMessageItem(cursor.getPosition());
        txtBody.setText(item.content);
        txtDate.setText(MessageBubbleListRenderer.renderDate(item.date));
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        Cursor cursor = (Cursor) getItem(position);

        if(MessageItem.isSentMessage(cursor.getInt(cursor.getColumnIndexOrThrow(GomTalkProviderContract.Messages.STATUS)))) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    private MessageItem getCachedMessageItem(int position) {
        Cursor cursor = (Cursor) getItem(position);
        MessageItem item = mMessageItemCache.get(position);
        if(item == null) {
            item = MessageItem.from(cursor);
            mMessageItemCache.put(position, item);
        }
        return item;
    }

    @Override
    protected void onContentChanged() {
        refresh();
    }

    private void refresh() {
        if(mUpdateListener != null) {
            mUpdateListener.beforeRefresh();
        }

        if(mThreadId < 0){
            changeCursor(null);
            if(mUpdateListener != null) {
                mUpdateListener.afterRefresh();
            }
            return;
        }

        Cursor cursor = MessageItem.queryMessageList(mContext, mThreadId);
        changeCursor(cursor);

        if(mUpdateListener != null) {
            mUpdateListener.afterRefresh();
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
