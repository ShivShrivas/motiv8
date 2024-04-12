package com.motiv.motiv8.BackgroundServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CountReciver extends BroadcastReceiver {


    private StepCountUpdateListener listener;

    public CountReciver(StepCountUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int totalSteps = intent.getIntExtra("totalSteps", 0);
        if (listener != null) {
            listener.onStepCountUpdate(totalSteps);
        }
    }

    public interface StepCountUpdateListener {
        void onStepCountUpdate(int totalSteps);
    }
}
