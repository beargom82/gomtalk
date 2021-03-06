package com.dwkim.android.gomtalk.ui.conversationlist;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConversationListContract {
    public interface View {
        void showConversationList(Cursor cursor);

        void setPresenter(Presenter presenter);

        void openConversation(long conversationId);
    }

    public interface Presenter {
        void loadConversationList();

        void release();

        void start();

        void openConversation(long conversationId);
    }
}
