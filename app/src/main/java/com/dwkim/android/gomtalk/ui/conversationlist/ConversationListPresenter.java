package com.dwkim.android.gomtalk.ui.conversationlist;

import android.database.Cursor;

import com.dwkim.android.gomtalk.model.ConversationListModel;

/**
 * Created by kdw on 2016-09-12.
 */
public class ConversationListPresenter implements ConversationListContract.Presenter, ConversationListModel.ConversationListLoadCallback {
    private final ConversationListModel mModel;
    private final ConversationListContract.View mView;
    public ConversationListPresenter(ConversationListModel model
            , ConversationListContract.View view) {
        mModel = model;
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void loadConversationList() {
        mModel.loadConversationList(this);
    }

    @Override
    public void release() {
        mModel.release();
    }

    @Override
    public void start() {
        loadConversationList();
    }

    @Override
    public void openConversation(long conversationId) {
        mView.openConversation(conversationId);
    }

    @Override
    public void onConversationListLoaded(Cursor cursor) {
        mView.showConversationList(cursor);
    }
}
