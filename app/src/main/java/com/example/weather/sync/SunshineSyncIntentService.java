package com.example.weather.sync;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class SunshineSyncIntentService extends IntentService {

    public SunshineSyncIntentService() {
        super("SunshineSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SunshineSyncTask.syncWeather(this);
    }
}
