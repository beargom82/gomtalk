package com.example.dwkim.gomtalk.conversationlist;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.dwkim.android.gomtalk.R;
import com.example.dwkim.gomtalk.GomTalkFragment;
import com.example.dwkim.gomtalk.data.DBUtil;
import com.example.dwkim.gomtalk.provider.GomTalkProviderContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConversationListFragment extends GomTalkFragment {
    private static final String TAG = "GomTalk/ConvListFrag";
    private static final int MENU_DELETE = 1;
    private static final int MENU_DELETE_ORDER = 1;
//    private View mBtnCompose;

    public interface ConversationListActionListener {
        public void onComposeNew();
        public void onConversationSelected(long threadId);
    }

    private ListView mConversationListView;
    private ConversationListAdapter mConversationListAdapter;
    private ConversationListActionListener mConversationListActionListener;

    public ConversationListFragment() {
        Log.d(TAG, "ConversationListFragment()");
        setLogTag(TAG);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mConversationListActionListener = (ConversationListActionListener)activity;
        } catch(ClassCastException ex){
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_conversation_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);//just for log

        switch(item.getItemId()){
            case R.id.menu_compose_new:
                mConversationListActionListener.onComposeNew();
                break;
//            case R.id.action_compose_new:
//                mConversationListActionListener.onComposeNew();
//                break;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.conversation_list_fragment, container, false);
        mConversationListView = (ListView) rootView.findViewById(R.id.conversationListView);
        //mBtnCompose = rootView.findViewById(R.id.btnActionCompose);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        initListView();
        mConversationListAdapter.refresh();

//        mBtnCompose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mConversationListActionListener.onComposeNew();
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getActionBar().setTitle(R.string.app_name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mConversationListAdapter.changeCursor(null);
    }

    @Override
    public void onResume() {
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        Log.d(TAG, "set soft input hidden, " + WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    private void initListView() {
        mConversationListAdapter = new ConversationListAdapter(getActivity(), android.R.layout.simple_list_item_2,
                null,
                new String[]{GomTalkProviderContract.Threads.RECIPIENTS, GomTalkProviderContract.Threads.SNIPPET},
                new int[]{android.R.id.text1, android.R.id.text2},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mConversationListView.setAdapter(mConversationListAdapter);
        mConversationListView.setOnItemClickListener(mConversationListItemClickListener);
        mConversationListView.setOnCreateContextMenuListener(mListViewContextMenuListener);
    }

    private AdapterView.OnItemClickListener mConversationListItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            mConversationListActionListener.onConversationSelected(id);
        }
    };

    private ListViewContextMenuListener mListViewContextMenuListener = new ListViewContextMenuListener();
    private class ListViewContextMenuListener implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            AdapterView.AdapterContextMenuInfo adapterMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.add(0, MENU_DELETE, MENU_DELETE_ORDER, "Delete").setOnMenuItemClickListener(this);
            Log.e("kdw","menu clicked : " + adapterMenuInfo.position);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            AdapterView.AdapterContextMenuInfo adapterMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch( item.getItemId() ) {
                case MENU_DELETE:
                    int affected = DBUtil.deleteThread(adapterMenuInfo.targetView.getContext().getContentResolver(), adapterMenuInfo.id);
                    Log.d(TAG, "MENU_DELETE id = " +  adapterMenuInfo.id + ", deleted = " + affected);
                    return true;
            }

            return false;
        }
    }
}
