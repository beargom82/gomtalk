package com.example.dwkim.gomtalk.conversationview;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dwkim.android.gomtalk.R;
import com.example.dwkim.gomtalk.GomTalk;
import com.example.dwkim.gomtalk.GomTalkFragment;
import com.example.dwkim.gomtalk.data.Contact;
import com.example.dwkim.gomtalk.data.ContactList;
import com.example.dwkim.gomtalk.data.ContactPickResult;
import com.example.dwkim.gomtalk.data.DBUtil;
import com.example.dwkim.gomtalk.data.ThreadData;
import com.example.dwkim.gomtalk.smsmms.SmsMessageSender;
import com.example.dwkim.gomtalk.util.UiUtils;

import java.util.ArrayList;

/**
 * Created by dwkim on 2015-06-08.
 */
public class ConversationViewFragment extends GomTalkFragment {
    private static final String TAG = "Gomtalk/ConvViewFrag";

    private static final int OK = 1;
    private static final int ERROR_NO_MESSAGE = 2;
    private static final int ERROR_NO_RECIPIENT = 3;
    private static final int ERROR_INVALID_RECIPIENT = 4;

    private static final int CONTEXT_MENU_DELETE = 0;
    private static final int REQUEST_CODE_PICK_CONTACT = 1;

    private View mBtnAttach;
    private EditText mMessageEditor;

    private View mBtnSend;
    private ListView mMessageListView;
    private MessageBubbleListAdapter mMessageListAdapter;
    ConversationViewActionListener mConversationViewActionListener;
    private ThreadData mThreadData;
    private RecipientPanel mRecipientPanel;
    private View mContainerDpad;
    private View mBtnDpadLeft;
    private View mBtnDpadRight;

    public interface ComposeEventListener {

        void onPreMessageSend();

        void onMessageDeleted();
    }

    public ContactList getRecipients() {
        return mThreadData.getRecipients();
    }
    public interface ConversationViewActionListener{
    }

    public ConversationViewFragment() {
        setLogTag(TAG);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//            getActivity().getMenuInflater().inflate(R.menu.menu_gomtalk_activiy, menu);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mConversationViewActionListener = (ConversationViewActionListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString()
                    + " must implement ConversationViewActionListener");
        }
    }

    @Override
    public void onResume() {
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        Log.d(TAG, "set soft input visible, " + WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//                case R.id.action_compose_new:
//                    mOnComposerNewListener.onComposeNew();////todo
//                    break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.conversation_view_fragment, container, false);
        mMessageListView = (ListView) rootView.findViewById(R.id.messageListView);
        mBtnAttach = rootView.findViewById(R.id.btnAttach);
        mMessageEditor = (EditText) rootView.findViewById(R.id.messageEditor);
        mBtnSend = rootView.findViewById(R.id.btnSend);
        mContainerDpad = rootView.findViewById(R.id.containerDpad);
        mBtnDpadLeft = rootView.findViewById(R.id.btnDpadLeft);
        mBtnDpadRight = rootView.findViewById(R.id.btnDpadRight);

        mMessageEditor.addTextChangedListener(mMessageEditorTextWatcher);
        mBtnAttach.setOnClickListener(mOnClickListener);
        mBtnSend.setOnClickListener(mOnClickListener);
        mBtnDpadLeft.setOnClickListener(mOnClickListener);
        mBtnDpadRight.setOnClickListener(mOnClickListener);

        initNumPad(rootView);
        return rootView;
    }

    private void initNumPad(View rootView) {
        ArrayList<View> numButtons = new ArrayList<>();
        numButtons.add(rootView.findViewById(R.id.btnNum0));
        numButtons.add(rootView.findViewById(R.id.btnNum1));
        numButtons.add(rootView.findViewById(R.id.btnNum2));
        numButtons.add(rootView.findViewById(R.id.btnNum3));
        numButtons.add(rootView.findViewById(R.id.btnNum4));
        numButtons.add(rootView.findViewById(R.id.btnNum5));
        numButtons.add(rootView.findViewById(R.id.btnNum6));
        numButtons.add(rootView.findViewById(R.id.btnNum7));
        numButtons.add(rootView.findViewById(R.id.btnNum8));
        numButtons.add(rootView.findViewById(R.id.btnNum9));

        for(View view : numButtons) {
            view.setOnClickListener(mOnNumPadClickListener);
        }
    }

    private View.OnClickListener mOnNumPadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button numButton = (Button)v;
            mRecipientPanel.onNumKey(numButton.getText());
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        initListView();
        initLayout();
        initData();

        //todo listener way instead of ordering
        bindThreadData();
        updateSendButtonState();
        updateDpadContainerState(false);
    }

    private void updateDpadContainer() {

    }

    private void initData() {
        long threadId = getArguments() == null ? -1l
                : getArguments().getLong(GomTalk.EXTRA_THREAD_ID, -1l);
        mThreadData = ThreadData.get(getActivity(), threadId);

        Log.e(TAG, "initData done, id = " + threadId + ", messages = " + mMessageListAdapter.getCount());
    }

    private void bindThreadData() {
        bindRecipientsPanel(mThreadData, getActivity().getActionBar());
        mMessageListAdapter.load(mThreadData.getId());//todo listener way instead of ordering
    }

    private void bindRecipientsPanel(ThreadData threadData, ActionBar actionBar) {
        mRecipientPanel.bindRecipientsPanel(threadData, actionBar);
    }

    private void initLayout() {
        mRecipientPanel = new RecipientPanel(getView(), mOnRecipientEnteredListener);
        mRecipientPanel.setOnPickContactListener(mOnPickContactListener);
    }

    private View.OnClickListener mOnPickContactListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onPickContact();
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if(v == mBtnAttach) {
                onAttachButton();
                return;
            }

            if(v == mBtnSend) {
                onSend();
                return;
            }

            if(v == mBtnDpadLeft) {
                onEditorDpadLeft();
                return;
            }

            if(v == mBtnDpadRight) {
                onEditorDpadRight();
                return;
            }
        }
    };


    private void onEditorDpadRight() {
        if (mMessageEditor.getSelectionStart() == mMessageEditor.getSelectionEnd()) {
            int newSelection = mMessageEditor.getSelectionStart() + 1;
            if (newSelection < 0) {
                return;
            }
            mMessageEditor.setSelection(newSelection);
        }
    }

    private void onEditorDpadLeft() {
        if (mMessageEditor.getSelectionStart() == mMessageEditor.getSelectionEnd()) {
            int newSelection = mMessageEditor.getSelectionStart() - 1;
            if (newSelection > mMessageEditor.length()) {
                return;
            }
            mMessageEditor.setSelection(newSelection);
        }
    }

    private void resetEditor() {
        mMessageEditor.setText("");
    }

    private void onSend() {
        int dataResult = checkData();//check required data (hasRecipient, hasContent)
        Log.d(TAG, "onSend, check result : " + dataResult);
        if(dataResult != OK){
            handleError(dataResult);
            return;
        }

        dataResult = validateRecipients();
        Log.d(TAG,"onSend, validate recipient : " + dataResult);
        if(dataResult != OK){
            handleError(dataResult);
            return;
        }

        ensureThread();
        SmsMessageSender sender = new SmsMessageSender(getActivity(),
                mThreadData.getId(),
                getRecipients().getAddresses(),
                mMessageEditor.getText().toString());
        sender.setComposeEventListener(mComposeEventListener);
        sender.sendMessage();
    }

    private void ensureThread() {
        mThreadData.ensureThreadId(getActivity());
    }

    private void handleError(int errorCode) {
        switch(errorCode){
            case OK:
                throw new IllegalArgumentException("OK code should not be here");

            case ERROR_NO_MESSAGE:
                Toast.makeText(getActivity(), "Enter message", Toast.LENGTH_SHORT).show();
                mMessageEditor.requestFocus();
                break;

            case ERROR_NO_RECIPIENT:
                Toast.makeText(getActivity(), "Enter recipient", Toast.LENGTH_SHORT).show();
                mRecipientPanel.requestFocus();
                break;

            case ERROR_INVALID_RECIPIENT:
                Toast.makeText(getActivity(), "Enter valid recipient", Toast.LENGTH_SHORT).show();
                mRecipientPanel.requestFocus();
                mRecipientPanel.selectAll();
                break;
            default:
                throw new IllegalArgumentException("Unknown error code " + errorCode);
        }
    }

    private int validateRecipients() {
        //todo validate recipients
        for(Contact contact : getRecipients()) {
            if(!PhoneNumberUtils.isWellFormedSmsAddress(contact.getAddress())){
                return ERROR_INVALID_RECIPIENT;
            }
        }
        return OK;
    }

    private int checkData() {
        if(!hasMessageText()){
            return ERROR_NO_MESSAGE;
        }

        ContactList contactList = getRecipients();
        if(contactList.size() < 1) {
            return ERROR_NO_RECIPIENT;
        }

        return OK;
    }

    private boolean hasMessageText() {
        return mMessageEditor.length() > 0;
    }

    private void onAttachButton() {
    }

    private void onPickContact() {
        Intent intent = ContactPickResult.createPickContactIntent();
        startActivityForResult(intent, REQUEST_CODE_PICK_CONTACT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_PICK_CONTACT: {
                ContactPickResult contactProviderData = ContactPickResult.fromPickContact(getActivity(), data.getData());
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMessageListAdapter.release();
    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void initListView() {
        mMessageListAdapter = new MessageBubbleListAdapter(getActivity(), getThreadId());
        mMessageListAdapter.setOnUpdateListener(mMessageListUpdateListener);

        mMessageListView.setAdapter(mMessageListAdapter);
        mMessageListView.setOnCreateContextMenuListener(mOnMessageListCreateContextMenuListener);
    }

    private long getThreadId() {
        if(mThreadData != null) {
            return mThreadData.getId();
        } else {
            return -1l;
        }
    }

    private ComposeEventListener mComposeEventListener = new ComposeEventListener(){
        @Override
        public void onPreMessageSend() {
            resetEditor();
            bindThreadData();
        }

        @Override
        public void onMessageDeleted() {
        }
    };

    private MessageListUpdateListener mMessageListUpdateListener
            = new MessageListUpdateListener() {
        @Override
        public void beforeRefresh() {
        }

        @Override
        public void afterRefresh() {
            if(mMessageListAdapter.getCount() == 0) {
                hideSoftInput();
                getActivity().onBackPressed();
            }
        }
    };

    private RecipientPanel.OnRecipientEnteredListener mOnRecipientEnteredListener = new RecipientPanel.OnRecipientEnteredListener(){

        @Override
        public void onRecipientEntered(ContactList recipients) {
            Log.e("kdw", "onRecipientEntered, " + recipients);
            //assuming thread is not deterenmined yet
            mThreadData.setRecipients(recipients);
        }
    };

    private View.OnCreateContextMenuListener mOnMessageListCreateContextMenuListener = new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            AdapterView.AdapterContextMenuInfo adapterMenuInfo = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
            MessageListMenuItemClickListener menuItemClickListener = new MessageListMenuItemClickListener(adapterMenuInfo, mComposeEventListener);

            contextMenu.add(0, CONTEXT_MENU_DELETE, 0, R.string.menu_delete).setOnMenuItemClickListener(menuItemClickListener);
        }
    };

    private static class MessageListMenuItemClickListener implements MenuItem.OnMenuItemClickListener {
        private final AdapterView.AdapterContextMenuInfo mAdapterMenuInfo;
        private final ComposeEventListener mComposeEventListener;

        public MessageListMenuItemClickListener(AdapterView.AdapterContextMenuInfo adapterMenuInfo, ComposeEventListener composeEventListener) {
            mAdapterMenuInfo = adapterMenuInfo;
            mComposeEventListener = composeEventListener;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case CONTEXT_MENU_DELETE: {
                    int affected = DBUtil.deleteMessage(mAdapterMenuInfo.targetView.getContext().getContentResolver(), mAdapterMenuInfo.id);
                    if (affected > 0) {
                        mComposeEventListener.onMessageDeleted();
                    } else {
                        throw new IllegalStateException("No mesasge deleted ");
                    }

                    return true;
                }
            }
            return false;
        }
    }

    private TextWatcher mMessageEditorTextWatcher = new TextWatcher() {
        int mPreviousLength = -1;
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            int textLenght = editable.length();
            if(mPreviousLength == textLenght) {
                return;
            }
            mPreviousLength = textLenght;

            updateSendButtonState();
            updateDpadContainerState();
        }
    };

    private void updateDpadContainerState() {
        updateDpadContainerState(true);
    }

    private void updateDpadContainerState(boolean anim) {
        int textLenght = mMessageEditor.length();
        if(textLenght == 0) {
            if(anim) {
                UiUtils.fadeOut(mContainerDpad, GomTalk.ANIM_DURATION_SHORT);
            } else {
                UiUtils.gone(mContainerDpad);
            }
        } else {
            if(anim) {
                UiUtils.fadeIn(mContainerDpad, GomTalk.ANIM_DURATION_SHORT);
            } else {
                UiUtils.visible(mContainerDpad);
            }
        }
    }

    private void updateSendButtonState() {
        int textLenght = mMessageEditor.length();
        if(textLenght == 0) {
            mBtnSend.animate().scaleX(1f).scaleY(1f).start();
        } else {
            mBtnSend.setPivotX(mBtnSend.getWidth() / 2f);
            mBtnSend.setPivotY(mBtnSend.getHeight() / 2f);
            mBtnSend.animate().scaleX(1.3f).scaleY(1.3f).start();
        }
    }
}
