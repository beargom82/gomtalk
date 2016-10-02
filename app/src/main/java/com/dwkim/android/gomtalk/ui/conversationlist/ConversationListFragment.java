package com.dwkim.android.gomtalk.ui.conversationlist;

import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dwkim.android.gomtalk.R;
import com.dwkim.android.gomtalk.model.ConversationListModel;
import com.dwkim.android.gomtalk.ui.GomTalkFragmentBase;
import com.example.dwkim.gomtalk.GomTalk;
import com.example.dwkim.gomtalk.conversationview.ConversationViewFragment;
import com.example.dwkim.gomtalk.data.DBUtil;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConversationListFragment extends GomTalkFragmentBase {
    private static final String TAG = "GomTalk/ConvListFrag";


    private ConversationListContract.Presenter mMvpPresenter;
    private ConversationListContract.View mMvpView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.conversation_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupMvpView();
        setupMvpPresenter();
        startMvpPresenter();
    }

    @Override
    protected void startMvpPresenter() {
        mMvpPresenter.start(getActivity());
    }

    @Override
    protected void setupMvpView() {
        mMvpView = new ConversationListViewImpl((ListView) getView().findViewById(R.id.list));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class ConversationListViewImpl implements ConversationListContract.View {
        private ListView mListView;
        private ConversationListAdapter mListAdapter;
        private ConversationListContract.Presenter mPresenter;

        public ConversationListViewImpl(ListView listview) {
            mListView = listview;
            mListAdapter = new ConversationListAdapter(getActivity());
            mListView.setAdapter(mListAdapter);
            mListView.setOnItemClickListener(mOnListItemClickListener);
        }

        @Override
        public void showConversationList(Cursor cursor) {
            mListAdapter.changeCursor(cursor);
        }

        @Override
        public void setPresenter(ConversationListContract.Presenter presenter) {
            mPresenter = presenter;
        }

        private final AdapterView.OnItemClickListener mOnListItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openConversationView(id);
            }
        };

        private void openConversationView(long conversationId) {
            ConversationViewFragment fragment = new ConversationViewFragment();
            if(DBUtil.isValid(conversationId)) {
                Bundle args = new Bundle();
                args.putLong(GomTalk.EXTRA_THREAD_ID, conversationId);
                fragment.setArguments(args);
            }

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.animator.fragment_in, R.animator.fragment_out,
                    R.animator.fragment_in, R.animator.fragment_out);
//        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
//                android.R.animator.fade_in, android.R.animator.fade_out);
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    };

    @Override
    public void setupMvpPresenter() {
        mMvpPresenter = new ConversationListPresenter(
                new ConversationListModel()
                , mMvpView);
    }

    @Override
    public void releasePresenter() {
        mMvpPresenter.release();
    }
}
