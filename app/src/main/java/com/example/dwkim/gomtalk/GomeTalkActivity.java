package com.example.dwkim.gomtalk;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dwkim.android.gomtalk.R;
import com.example.dwkim.gomtalk.conversationlist.ConversationListFragment;
import com.example.dwkim.gomtalk.conversationview.ConversationViewFragment;
import com.example.dwkim.gomtalk.data.Contact;
import com.example.dwkim.gomtalk.data.DBUtil;


public class GomeTalkActivity extends Activity implements
        ConversationListFragment.ConversationListActionListener,
        ConversationViewFragment.ConversationViewActionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_activiy);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ConversationListFragment())
                    .commit();
        }

//        getFragmentManager().addOnBackStackChangedListener(mOnBackStackChangedListener);
        runTest();
    }

//    private FragmentManager.OnBackStackChangedListener mOnBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
//        @Override
//        public void onBackStackChanged() {
//
//        }
//    };

    private void runTest() {
        testContact();
        testMessagesProvider();

    }

    private void testMessagesProvider() {
        Uri[] uris = new Uri[] {
                        Uri.parse("content://gomtalkmessages/")
                        , Uri.parse("content://gomtalkmessages/1")
                        , Uri.parse("content://gomtalkmessages/1/threads")
                        , Uri.parse("content://gomtalkmessages/inbox")
                        , Uri.parse("content://gomtalkmessages/outbox")
                        , Uri.parse("content://gomtalkmessages/queued")
                        , Uri.parse("content://gomtalkmessages/sending")
                        , Uri.parse("content://gomtalkmessages/sent")
                        , Uri.parse("content://gomtalkmessages/1/draft")
        };

        for(Uri uri : uris) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    private void testContact() {
        //todo
        Contact contact = Contact.get("123456789");
        Log.e("kdw", "format 123456789 = " + contact.getFormattedAddress());

        contact = Contact.get("01093592782");
        Log.e("kdw", "format 01093592782 = " + contact.getFormattedAddress());

        contact = Contact.get("949 278 6539");
        Log.e("kdw", "format 949 278 6539 = " + contact.getFormattedAddress());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gomtalk_activiy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComposeNew() {
        openConverstionView(-1l);
    }

    @Override
    public void onConversationSelected(long threadId) {
        openConverstionView(threadId);
    }

    private void openConverstionView(long threadId) {
        ConversationViewFragment fragment = new ConversationViewFragment();

        if(DBUtil.isValid(threadId)) {
            Bundle args = new Bundle();
            args.putLong(GomTalk.EXTRA_THREAD_ID, threadId);
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
}
