package com.motiv.motiv8;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;

import java.util.concurrent.TimeUnit;

public class Dashboard_Page extends AppCompatActivity {

    TextView tvBalance,tvTotalSteps;

    int totalSteps=0;
    String FITNESS_ACTIVITY_WRITE_SCOPE = "https://www.googleapis.com/auth/fitness.activity.write";

    // Read access to body-related data
    String FITNESS_BODY_READ_SCOPE = "https://www.googleapis.com/auth/fitness.body.read";

    // Write access to body-related data
    String FITNESS_BODY_WRITE_SCOPE = "https://www.googleapis.com/auth/fitness.body.write";

    // Read access to location-related data
    String FITNESS_LOCATION_READ_SCOPE = "https://www.googleapis.com/auth/fitness.location.read";

    // Write access to location-related data
    String FITNESS_LOCATION_WRITE_SCOPE = "https://www.googleapis.com/auth/fitness.location.write";
    private GoogleApiClient mClient;
    private OnDataPointListener onDataPointListener;


    private final int minValue = 0;
    private int maxValue = 0; // Change this to your desired max value
    private Handler handler;
    private Runnable updateTextRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_page);
        tvBalance=findViewById(R.id.tvBalance);
        tvTotalSteps=findViewById(R.id.tvTotalSteps);
        tvBalance.setText(Html.fromHtml(getString(R.string.balance_132_2)));

        handler = new Handler(Looper.getMainLooper());
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(FITNESS_ACTIVITY_WRITE_SCOPE),new Scope(FITNESS_LOCATION_READ_SCOPE),new Scope(FITNESS_LOCATION_WRITE_SCOPE), new Scope(FITNESS_BODY_READ_SCOPE),new Scope(FITNESS_BODY_WRITE_SCOPE))
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1100) {
            // Handle sign-in result
            if (resultCode == RESULT_OK && data != null) {
                // Sign-in was successful, build the GoogleApiClient for Fitness API
                mClient = new GoogleApiClient.Builder(this)
                        .addApi(Fitness.SENSORS_API)
                        .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(Bundle bundle) {
                                Log.d("TAG", "Connected to Google Fit API");

                                // Subscribe to step count updates
                                subscribeToStepCountUpdates();
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                // Handle suspended connection
                                Log.d("TAG", "Connection suspended");
                            }
                        })
                        .build();
                mClient.connect();
            } else {
                // Sign-in failed or user canceled the operation
                Log.d("TAG", "Sign-in failed or user canceled");
                Toast.makeText(this, "Sign-in failed or user canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Subscribe to step count updates
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

                    Log.d("TAG", "Step Count: " + value);


                    maxValue=totalSteps+value;
                    updateTextView();

                }
            }
        };

        Fitness.SensorsApi.add(
                mClient,
                new SensorRequest.Builder()
                        .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .setSamplingRate(2, TimeUnit.MILLISECONDS) // Sample every 5 seconds
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
    private void updateTextView() {
        // Create a runnable to update the text view
        updateTextRunnable = new Runnable() {
            @Override
            public void run() {
                // Increment total steps
                totalSteps++;

                // Update text view with the current value
                Log.d("TAG", "Step Count: " + totalSteps);
                tvTotalSteps.setText(String.valueOf(totalSteps));

                // Check if total steps reaches the maximum value
                if (totalSteps >= maxValue) {
                    // Stop the handler
                    handler.removeCallbacks(updateTextRunnable);
                    // Reset total steps to minimum value
                    totalSteps = maxValue;
                    // Pause for the next event
                    // You can trigger the next event or action here
                } else {
                    // Schedule the next update after one second
                    handler.postDelayed(this, 1000);
                }
            }
        };


        handler.post(updateTextRunnable);
    }


    private void restartUpdating() {
        totalSteps = minValue;
        updateTextView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mClient != null && mClient.isConnected()) {
            Fitness.SensorsApi.remove(mClient, onDataPointListener);
            mClient.disconnect();
        }
    }
}