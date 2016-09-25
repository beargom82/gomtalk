package com.example.dwkim.gomtalk.conversationlist;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;

import com.example.dwkim.gomtalk.provider.GomTalkProviderContract;

/**
* Created by dwkim on 2015-06-07.
*/
class ConversationListAdapter extends SimpleCursorAdapter {
    private static final String DEFAULT_SORT_ORDER = GomTalkProviderContract.Threads.DATE + " DESC";
    private final Context mContext;

    public ConversationListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mContext = context;
    }

    @Override
    protected void onContentChanged() {
        refresh();
    }

    public void refresh() {
        Cursor cursor = mContext.getContentResolver().query(
                GomTalkProviderContract.Threads.CONTENT_URI,
                null,
                null,
                null,
                DEFAULT_SORT_ORDER);
        changeCursor(cursor);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ConversationListItem listItem = ConversationListItem.from(view);
        listItem.bind(cursor);
    }
}
