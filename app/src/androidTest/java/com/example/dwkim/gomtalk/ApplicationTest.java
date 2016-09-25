package com.example.dwkim.gomtalk;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.test.ApplicationTestCase;

import com.example.dwkim.gomtalk.data.Contact;
import com.example.dwkim.gomtalk.data.ContactList;
import com.example.dwkim.gomtalk.data.DBUtil;
import com.example.dwkim.gomtalk.data.SmsData;
import com.example.dwkim.gomtalk.data.ThreadData;
import com.example.dwkim.gomtalk.provider.GomTalkProvider;
import com.example.dwkim.gomtalk.util.MessageUtils;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testContact(){
        Contact.get("123-456-789");
        Contact.get("123456789");

        assertEquals(1, Contact.getCacheCount());
        assertEquals("123456789", Contact.get("123-456-789").getAddress());
        assertEquals("010-1234-5678", Contact.get("010 1234 5678").getFormattedAddress());
    }

    public void testGomTalkProvider() {
        long id = DBUtil.getOrCreateThreadId(getContext(), new String[]{"01012345678"});
        assertTrue(id == 1l);

        id = DBUtil.getOrCreateThreadId(getContext(), new String[]{"0102223333"});
        assertEquals(2l, id);
    }

    public void temptestGomTalkProviderMessage() {
        Uri uri = Uri.parse("content://gomtalk/threads/1/messages");
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        assertEquals(0, cursor.getCount());
        cursor.close();

        String msg = "test message 1";
        SmsData smsData = new SmsData(1l, msg, SmsData.STATUS_QUEUED);
        smsData.persist(getContext());

        assertEquals(true, smsData.isPersist());

        SmsData smsData2 = new SmsData(getContext(), smsData.getUri());
        assertEquals(msg, smsData2.getMessageText());
    }

    public void testMessageUtils(){
        assertEquals(true, DBUtil.isValid((long) 1));
        assertEquals(false, DBUtil.isValid((long) 0));
        long threadId = -1;
        assertEquals(false, DBUtil.isValid(threadId));
    }

    public void testThreadCache() {
        assertEquals(0, ThreadData.getCacheCount());

        ThreadData.get(getContext(), 1l);
        assertEquals(1, ThreadData.getCacheCount());
    }
}