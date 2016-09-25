package com.dwkim.android.gomtalk.ui.conversationlist;

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
    private final Context mContext;

    public ConversationListPresenter(Context context
            , ConversationListModel model
            , ConversationListContract.View view) {
        mContext = context;
        mModel = model;
        mView = view;

        mModel.bind(mModelCallback);
    }

    @Override
    public void loadConversationList() {
        mModel.queryConversationList(mContext);
    }

    @Override
    public void onDestroy() {
        mModel.unbind();
    }

    private ModelCallback mModelCallback = new ModelCallback() {
        @Override
        public void onComplete(Object data) {
            mView.showConversationList((Cursor)data);
        }
    };
}
