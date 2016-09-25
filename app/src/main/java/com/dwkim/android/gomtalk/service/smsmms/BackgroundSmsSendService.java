package com.dwkim.android.gomtalk.service.smsmms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by dwkim on 2015-06-20.
 */
public class BackgroundSmsSendService extends Service{
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
