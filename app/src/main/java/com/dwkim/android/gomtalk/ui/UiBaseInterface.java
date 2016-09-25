package com.dwkim.android.gomtalk.ui;

/**
 * Created by kdw on 2016-09-12.
 */
public interface UiBaseInterface {

    /**
     * Creates Presenter instance and connect to Model and Presenter
     * Must be called very first of onCreate() or onActivityCreated()
     */
    void setupPresenter();

    /**
     * Releases Presenter
     * Must be called in onDestroy()
     */
    void releasePresenter();
}
