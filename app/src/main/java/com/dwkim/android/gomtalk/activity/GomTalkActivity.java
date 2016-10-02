package com.dwkim.android.gomtalk.activity;

import android.app.Activity;
import android.os.Bundle;

import com.dwkim.android.gomtalk.R;
import com.example.dwkim.gomtalk.conversationview.ConversationViewFragment;

public class GomTalkActivity extends Activity implements ConversationViewFragment.ConversationViewActionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gom_talk_activity);
    }
}
