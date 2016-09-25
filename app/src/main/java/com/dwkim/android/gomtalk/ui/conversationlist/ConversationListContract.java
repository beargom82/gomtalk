package com.dwkim.android.gomtalk.ui.conversationlist;

import android.database.Cursor;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConversationListContract {
    public interface View {
        void showConversationList(Cursor cursor);
    }

    public interface Presenter {
        void loadConversationList();

        void onDestroy();
    }
}
