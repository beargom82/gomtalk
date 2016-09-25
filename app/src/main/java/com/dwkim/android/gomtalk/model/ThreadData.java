package com.dwkim.android.gomtalk.model;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.dwkim.gomtalk.data.ContactList;
import com.example.dwkim.gomtalk.data.DBUtil;
import com.example.dwkim.gomtalk.provider.GomTalkProviderContract;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by dwkim on 2015-06-21.
 */
public class ThreadData {
    private static Cache sCache;
    private long mId;
    private ContactList mRecipients;
    private final String mSnippet;
    private final long mDate;
    private final int mCount;
    private HashSet<ThreadDataObserver> mThreadDataObservers;

    public static ThreadData get(Context context, long threadId) {
        if(DBUtil.isValid(threadId) ) {
            return getCache().get(context, threadId);
        } else {
            return new ThreadData();//empty
        }
    }

    private static Cache getCache() {
        if (sCache == null) {
            sCache = new Cache();
        }

        return sCache;
    }

    private ThreadData() {
        mId = -1l;
        mRecipients = new ContactList();
        mSnippet = "";
        mDate = -1l;
        mCount = 0;
    }

    private ThreadData(Context context, long threadId) {
        Uri uri = ContentUris.withAppendedId(GomTalkProviderContract.Threads.CONTENT_URI, threadId);
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(uri, GomTalkProviderContract.Threads.ALL_PROJECTION, null, null, null);

            if(!cursor.moveToFirst()){
                throw new IllegalStateException("No thread data for " + uri);
            }

            mId = threadId;
            mRecipients = new ContactList(cursor.getString(GomTalkProviderContract.Threads.COL_RECIPIENTS));
            mSnippet = cursor.getString(GomTalkProviderContract.Threads.COL_SNIPPET);
            mDate = cursor.getLong(GomTalkProviderContract.Threads.COL_DATE);
            mCount = cursor.getInt(GomTalkProviderContract.Threads.COL_COUNT);
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }

    }

    public boolean hasThreadId() {
        return DBUtil.isValid(mId);
    }

    public long getId() {
        return mId;
    }

    public ContactList getRecipients() {
        return mRecipients;
    }

    public long getDate() {
        return mDate;
    }

    public int getCount() {
        return mCount;
    }

    //only for debug
    public static int getCacheCount() {
        return getCache().getCount();
    }

    public void setRecipients(ContactList recipients) {
        mRecipients = recipients;
    }

    public synchronized void ensureThreadId(Context context) {
        if(!hasThreadId()) {
            mId = DBUtil.getOrCreateThreadId(context, getRecipients().getAddresses());
            getCache().get(context, mId);
            notifyOnThreadIdChanged();
        }
    }

    public void addThreadDataObserver(ThreadDataObserver observer) {//todo use this
        mThreadDataObservers.add(observer);
    }

    public void removeThreadDataObserver(ThreadDataObserver observer) {//todo use this for recipients panel
        mThreadDataObservers.remove(observer);
    }

    private void notifyOnThreadIdChanged() {
        for(ThreadDataObserver observer : mThreadDataObservers) {
            observer.onThreadIdChanged(mId);
        }
    }

    public interface ThreadDataObserver {

        void onThreadIdChanged(long id);
    }
    private static class Cache{
        private HashMap<Long, ThreadData> mMap = new HashMap<Long, ThreadData>();
        private Cache(){

        }

        public ThreadData get(Context context, long threadId){
            ThreadData data = mMap.get(threadId);
            if(data == null) {
                data = new ThreadData(context, threadId);
                mMap.put(threadId, data);
            }
            return data;
        }

        public int getCount() {
            return mMap.size();
        }
    }
}
