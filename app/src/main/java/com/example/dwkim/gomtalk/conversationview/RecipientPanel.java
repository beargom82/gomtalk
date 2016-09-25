package com.example.dwkim.gomtalk.conversationview;

import android.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dwkim.android.gomtalk.R;
import com.example.dwkim.gomtalk.ViewHelper;
import com.example.dwkim.gomtalk.data.Contact;
import com.example.dwkim.gomtalk.data.ContactList;
import com.example.dwkim.gomtalk.data.ThreadData;

/**
 * Created by kdw on 2015-10-11.
 */
public class RecipientPanel implements ViewHelper, View.OnFocusChangeListener {
    private final View mRootView;
    private final View mRecipientsEditorPanel;
    private final EditText mRecipientsEditor;
    private final View mBtnPickContact;
    private final ViewGroup mRecipientChipsView;
    private OnRecipientEnteredListener mOnRecipientEnteredListener;
    private String mPreviousRecipientText;
    private ContactList mRecipients = new ContactList();
    private View.OnClickListener mOnPickContactListener;

    public RecipientPanel(View rootView, OnRecipientEnteredListener onRecipientEnteredListener) {
        mRootView = rootView;
        mRecipientChipsView = (ViewGroup) findViewById(R.id.recipient_chips_view);
        mRecipientsEditorPanel = findViewById(R.id.recipientsEditorPanel);
        mRecipientsEditor = (EditText) findViewById(R.id.recipientsEditor);
        mRecipientsEditor.setOnFocusChangeListener(this);
        mBtnPickContact = findViewById(R.id.btnPickContact);

        mBtnPickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnPickContactListener != null) {
                    mOnPickContactListener.onClick(v);
                }
            }
        });

        mOnRecipientEnteredListener = onRecipientEnteredListener;
    }

    @Override
    public View findViewById(int resId) {
        if(mRootView == null) {
            throw new IllegalStateException("Root View must be initialized");
        }
        return mRootView.findViewById(resId);
    }

    public void setOnPickContactListener(View.OnClickListener onClickListener) {
        mOnPickContactListener = onClickListener;
    }

    private void updateRecipients() {
        String currentRecipientText = mRecipientsEditor.getText().toString();
        if(currentRecipientText.equals(mPreviousRecipientText)){
            //nothing changed
            return;
        } else {
            mPreviousRecipientText = currentRecipientText;
        }

        if(mRecipientsEditor.length() == 0){
            mRecipients.clear();
        } else {
            Contact contact = Contact.get(mRecipientsEditor.getText().toString());
            mRecipients.clear();
            mRecipients.add(contact);
        }

        if(mOnRecipientEnteredListener != null) {
            mOnRecipientEnteredListener.onRecipientEntered(mRecipients);
        }
    }

    void showRecipientsEditor() {
        mRecipientsEditorPanel.setVisibility(View.VISIBLE);
    }

    void hideRecipientsEditor() {
        mRecipientsEditorPanel.setVisibility(View.GONE);
    }

    public void requestFocus() {
        if(isEditMode()) {
            mRecipientsEditor.requestFocus();
        }
    }

    public void selectAll() {
        if(isEditMode()) {
            mRecipientsEditor.setSelection(0, mRecipientsEditor.length());
        }
    }

    public boolean isEditMode() {
        return View.VISIBLE == mRecipientsEditorPanel.getVisibility();
    }

    public void setRecipientText(String semiSeperatedAddresses) {
        mRecipientsEditor.setText(semiSeperatedAddresses);
    }

    public void clearRecipientText() {
        mRecipientsEditor.setText("");
    }

    public void bindRecipientsPanel(ThreadData threadData, ActionBar actionBar) {
        if(threadData.hasThreadId()) {
            String formattedAddress = threadData.getRecipients().getFormattedAddress();
            actionBar.setTitle(formattedAddress);
            hideRecipientsEditor();
            clearRecipientText();
        } else {
            showRecipientsEditor();
            setRecipientText(threadData.getRecipients().getSemiSeparatedAddresses());
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!isEditMode()) {
            return;
        }

        if(!hasFocus) {//Refresh recipients array when losing focus
            updateRecipients();
        }
    }

    public void onNumKey(CharSequence num) {
        mRecipientsEditor.getText().insert(mRecipientsEditor.getSelectionStart(), num);
    }

    public interface OnRecipientEnteredListener {
        void onRecipientEntered(ContactList mRecipients);
    }
}
