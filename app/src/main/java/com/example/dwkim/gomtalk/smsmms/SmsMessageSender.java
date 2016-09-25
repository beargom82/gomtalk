package com.example.dwkim.gomtalk.smsmms;

import android.content.Context;

import com.example.dwkim.gomtalk.conversationview.ConversationViewFragment;
import com.example.dwkim.gomtalk.data.SmsData;

/**
 * Created by dwkim on 2015-06-20.
 */
public class SmsMessageSender {
    private final long mThreadId;
    private final String[] mAddresses;
    private final String mMessageText;
    private final Context mContext;
    private final SmsData mSmsData;
    private ConversationViewFragment.ComposeEventListener mComposeEventListener;

    public SmsMessageSender(Context context, long threadId, String[] addresses, String messageText) {
        mContext = context;
        mThreadId = threadId;
        mAddresses = addresses;
        mMessageText = messageText;

        mSmsData = new SmsData(mThreadId, mMessageText, SmsData.STATUS_QUEUED);
    }

    public void sendMessage() {
        //save message
        mSmsData.persist(mContext);
        if(mComposeEventListener != null) {
            mComposeEventListener.onPreMessageSend();
        }

        //start send service or call ril
    }

    public void setComposeEventListener(ConversationViewFragment.ComposeEventListener listener){
        mComposeEventListener = listener;
    }
}
