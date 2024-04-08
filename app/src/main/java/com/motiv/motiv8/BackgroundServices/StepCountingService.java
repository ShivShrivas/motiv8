package com.motiv.motiv8.BackgroundServices;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.motiv.motiv8.R;

import java.util.concurrent.TimeUnit;

public class StepCountingService extends Service {
    private GoogleApiClient mClient;
    private OnDataPointListener onDataPointListener;
    private int totalSteps = 0;
    private boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        initializeProcess();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            initializeProcess();
        }
        return START_STICKY;
    }

    @SuppressLint("ForegroundServiceType")
    private void initializeProcess() {
        mClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.SENSORS_API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d("TAG", "Connected to Google Fit API");
                        subscribeToStepCountUpdates();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d("TAG", "Connection suspended");
                    }
                })
                .build();
        mClient.connect();

        // Create a notification channel (for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("step_counting_channel",
                    "Step Counting Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Create a notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "step_counting_channel")
                .setContentTitle("Step Counting Service")
                .setContentText("Counting steps...")
                .setSmallIcon(R.drawable.logo); // Replace ic_notification_icon with your own icon

        Notification notification = notificationBuilder.build();

        // Start the service in foreground
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        // Unsubscribe from step count updates and disconnect Google Fit API client
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void subscribeToStepCountUpdates() {
        DataSource dataSource = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .build();

        onDataPointListener = new OnDataPointListener() {
            @Override
            public void onDataPoint(DataPoint dataPoint) {
                for (final Field field : dataPoint.getDataType().getFields()) {
                    final int value = dataPoint.getValue(field).asInt();
                    totalSteps += value;

                    Log.d("TAG", "onDataPoint: "+totalSteps);
                    // Broadcast the updated total step count to the activity
                    Intent intent = new Intent("step_count_broadcast");
                    intent.putExtra("totalSteps", totalSteps);
                    sendBroadcast(intent);
                }
            }
        };

        Fitness.SensorsApi.add(
                mClient,
                new SensorRequest.Builder()
                        .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .setSamplingRate(2, TimeUnit.MILLISECONDS)
                        .build(),
                onDataPointListener
        ).setResultCallback(status -> {
            if (status.isSuccess()) {
                Log.d("TAG", "Listener registered!");
            } else {
                Log.d("TAG", "Listener registration failed: " + status.getStatusMessage());
            }
        });
    }
}
