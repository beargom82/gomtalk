package com.dwkim.android.gomtalk.ui.conversationlist;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dwkim.android.gomtalk.R;
import com.dwkim.android.gomtalk.model.ConversationListModel;
import com.dwkim.android.gomtalk.ui.GomTalkFragmentBase;
import com.dwkim.android.gomtalk.ui.UiBaseInterface;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConversationListFragment extends GomTalkFragmentBase implements UiBaseInterface {
    private static final String TAG = "GomTalk/ConvListFrag";
    private ListView mListView;
    private ConversationListAdapter mListAdapter;
    private ConversationListContract.Presenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.conversation_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupPresenter();

        mListView = (ListView) getView().findViewById(R.id.list);
        setupListView(mListView);

        mPresenter.loadConversationList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePresenter();
    }

    private void setupListView(ListView listView) {
        mListAdapter = new ConversationListAdapter(getActivity());
        listView.setAdapter(mListAdapter);
    }

    private ConversationListContract.View mConversationListViewImpl = new ConversationListContract.View(){

        @Override
        public void showConversationList(Cursor cursor) {
            mListAdapter.changeCursor(cursor);
        }
    };

    @Override
    public void setupPresenter() {
        mPresenter = new ConversationListPresenter(getActivity()
                , new ConversationListModel()
                , mConversationListViewImpl);
    }

    @Override
    public void releasePresenter() {
        mPresenter.onDestroy();
    }
}
