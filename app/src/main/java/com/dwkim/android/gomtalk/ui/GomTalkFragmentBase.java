package com.dwkim.android.gomtalk.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kdw on 2015-10-11.
 */
public abstract class GomTalkFragmentBase extends Fragment {
    private String mTag;

    public GomTalkFragmentBase() {
        mTag = "GomTalk/GomTalkFragmentBase";
    }

    protected void setLogTag(String tag) {
        mTag = tag;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i(mTag, "onAttach()");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(mTag, "onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(mTag, "onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(mTag, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.i(mTag, "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(mTag, "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(mTag, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(mTag, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.i(mTag, "release()");
        super.onDestroy();
        releasePresenter();
    }

    @Override
    public void onDestroyView() {
        Log.i(mTag, "onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.i(mTag, "onCreateContextMenu()");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i(mTag, "onCreateOptionsMenu()");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(mTag, "onOptionsItemSelected(), " + item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.i(mTag, "onPrepareOptionsMenu()");
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onDetach() {
        Log.i(mTag, "onDetach()");
        super.onDetach();
    }

    /**
     * setup View object that will be used by Presenter of MVP pattern
     */
    protected abstract void setupMvpView();

    /**
     * Creates Presenter instance and connect to Model and Presenter
     * Must be called very first of onCreate() or onActivityCreated()
     */
    public abstract void setupMvpPresenter();

    /**
     * start Presenter of MVP pattern
     */
    protected abstract void startMvpPresenter();

    /**
     * Releases Presenter
     * Must be called in release()
     */
    public abstract void releasePresenter();
}
