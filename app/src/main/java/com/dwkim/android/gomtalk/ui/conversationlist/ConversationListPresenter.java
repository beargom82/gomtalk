package com.dwkim.android.gomtalk.ui.conversationlist;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import com.dwkim.android.gomtalk.model.ConversationListModel;
import com.dwkim.android.gomtalk.model.ModelCallback;

/**
 * Created by kdw on 2016-09-12.
 */
public class ConversationListPresenter implements ConversationListContract.Presenter {
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
        mModel.queryConversationList(new ModelCallback(){
            @Override
            public void onComplete(int resultCode, int errorCode, Object data) {
                mView.showConversationList((Cursor) data);
            }
        });
    }

    @Override
    public void release() {
    }

    @Override
    public void start(Context context) {
        loadConversationList();
    }

    @Override
    public void onConversationItemClick(Activity activity, long conversationId) {

    }
}
