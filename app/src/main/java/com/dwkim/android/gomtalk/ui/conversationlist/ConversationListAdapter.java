package com.dwkim.android.gomtalk.ui.conversationlist;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;

import com.dwkim.android.gomtalk.data.ContactList;
import com.example.dwkim.gomtalk.provider.GomTalkProviderContract;

import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
class ConversationListAdapter extends SimpleCursorAdapter {
    private HashMap<Integer, ConversationListItemData> mConversationListItemDataCache
            = new HashMap<>();

    public ConversationListAdapter(Context context) {
        super(context
                ,android.R.layout.simple_list_item_2
                ,null
                ,new String[]{GomTalkProviderContract.Threads.RECIPIENTS, GomTalkProviderContract.Threads.SNIPPET}
                ,new int[]{android.R.id.text1, android.R.id.text2}
                , 0);
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        mConversationListItemDataCache.clear();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ConversationListItemData data  = mConversationListItemDataCache.get(cursor.getPosition());
        if(data == null) {
            data = ConversationListItemData.from(cursor);
            mConversationListItemDataCache.put(cursor.getPosition(), data);
        }

        ConversationListItemHolder viewHolder = ConversationListItemHolder.from(view);
        viewHolder.recipientView.setText(data.getFormattedAddress());
        viewHolder.snippetView.setText(data.getSnippet());
    }

    private static class ConversationListItemData {
        private String mFormattedAddress;
        private String mSnippet;

        private static  ConversationListItemData from(Cursor cursor) {
            ConversationListItemData data = new ConversationListItemData();

            String addresses = cursor.getString(cursor.getColumnIndex(GomTalkProviderContract.Threads.RECIPIENTS));
            ContactList contactList = new ContactList(addresses);
            data.mFormattedAddress = contactList.getFormattedAddress();

            data.mSnippet = cursor.getString(cursor.getColumnIndex(GomTalkProviderContract.Threads.SNIPPET));
            return data;
        }

        public String getFormattedAddress() {
            return mFormattedAddress;
        }

        public String getSnippet() {
            return mSnippet;
        }
    }
}
