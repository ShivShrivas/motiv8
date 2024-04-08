package com.motiv.motiv8;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.motiv.motiv8.BackgroundServices.StepCountingService;

import java.text.DecimalFormat;
import java.util.List;
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

TextView txtStartStopCount;
    private final int minValue = 0;
    private int maxValue = 0; // Change this to your desired max value
    private Handler handler;
    private Runnable updateTextRunnable;
    CardView cvStepStopCount;
    private long activityOpenTimeMillis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_page);
        activityOpenTimeMillis = System.currentTimeMillis();
        tvBalance=findViewById(R.id.tvBalance);
        tvTotalSteps=findViewById(R.id.tvTotalSteps);
        txtStartStopCount=findViewById(R.id.txtStartStopCount);
        cvStepStopCount=findViewById(R.id.cvStepStopCount);
        tvBalance.setText(Html.fromHtml(getString(R.string.balance_132_2)));

        handler = new Handler(Looper.getMainLooper());
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(FITNESS_ACTIVITY_WRITE_SCOPE),new Scope(FITNESS_LOCATION_READ_SCOPE),new Scope(FITNESS_LOCATION_WRITE_SCOPE), new Scope(FITNESS_BODY_READ_SCOPE),new Scope(FITNESS_BODY_WRITE_SCOPE))
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1100);
        cvStepStopCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long currentTimeMillis = System.currentTimeMillis();
                long timeDifferenceMillis = currentTimeMillis - activityOpenTimeMillis;

                // Convert milliseconds to minutes
                long timeDifferenceMinutes = TimeUnit.MILLISECONDS.toMinutes(timeDifferenceMillis);


                    if (txtStartStopCount.getText().toString().trim().equals("Stop Step Counting")){
                        if (Integer.parseInt(tvTotalSteps.getText().toString().trim())<10){
                            Toast.makeText(Dashboard_Page.this, "You are not walking!!", Toast.LENGTH_SHORT).show();
                        }else{
                            if (mClient != null && mClient.isConnected()) {
                                Fitness.SensorsApi.remove(mClient, onDataPointListener);
                                mClient.disconnect();
                            }
                            showFitnessInfoDialog(Integer.parseInt(tvTotalSteps.getText().toString().trim()),timeDifferenceMinutes);
                        }

                    }else {
                        txtStartStopCount.setText("Stop Step Counting");

                        handler = new Handler(Looper.getMainLooper());
                        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .requestScopes(new Scope(FITNESS_ACTIVITY_WRITE_SCOPE),new Scope(FITNESS_LOCATION_READ_SCOPE),new Scope(FITNESS_LOCATION_WRITE_SCOPE), new Scope(FITNESS_BODY_READ_SCOPE),new Scope(FITNESS_BODY_WRITE_SCOPE))
                                .build();

                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(Dashboard_Page.this, signInOptions);
                        Intent signInIntent = googleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, 1100);

                    }

                }



        });
    }
    private void showFitnessInfoDialog( double steps, double timeMinutes) {
        Log.d("TAG", "timeMinutes: "+timeMinutes);
        double AVERAGE_STEP_LENGTH_IN_METERS = 0.6 + Math.random() * 0.2;
        double weightKg = 70; // Weight in kilograms

        double stepLengthMeters = 1; // Average step length in meters

// Convert steps to distance traveled in meters
        double distanceMeters = steps * AVERAGE_STEP_LENGTH_IN_METERS;

// MET value for walking
        double MET = 3.5;
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
// Calculate calories burnt
        double caloriesBurnt = MET * weightKg * timeMinutes / 60.0; // Calories per hour
        Log.d("TAG", "caloriesBurnt: "+caloriesBurnt);
        caloriesBurnt *= (timeMinutes / 60.0);
        Log.d("TAG", "caloriesBurnt1: "+caloriesBurnt);

        // Calories for the specific time spent walking
        // Create dialog builder
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fitness_info_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        // Find views in the dialog layout
        TextView textSteps = dialogView.findViewById(R.id.text_steps);
        TextView textDistance = dialogView.findViewById(R.id.text_distance);
        TextView textCalories = dialogView.findViewById(R.id.text_calories);
        Button buttonClose = dialogView.findViewById(R.id.button_close);

        // Set text for steps, distance, and calories
        textSteps.setText("Total Steps: " + totalSteps);
        textDistance.setText("Total Distance: " + decimalFormat.format(distanceMeters) + " meters");
        textCalories.setText("Estimated Calories Burnt: " + decimalFormat.format(caloriesBurnt));

        // Create and show the dialog
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        // Set listener for the close button
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              tvTotalSteps.setText("0");
                txtStartStopCount.setText("Start Step Counting");

                dialog.dismiss();
            }
        });
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
                            //    getBurnedCalories();
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
                Intent serviceIntent = new Intent(this, StepCountingService.class);
                startService(serviceIntent);
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

    private void getBurnedCalories() {
        long startTime = 5000; // Set your start time in milliseconds
        long endTime = 20000;
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .build();
        // Get the GoogleSignInAccount
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getAccountForExtension(this, fitnessOptions);

        // Create a DataReadRequest to query for calories burned
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.MILLISECONDS) // You can adjust the time period as needed
                .build();

        // Use the HistoryClient to read the data
        Task<DataReadResponse> response = Fitness.getHistoryClient(this, googleSignInAccount)
                .readData(readRequest);

        // Handle the response
        response.addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
            @Override
            public void onSuccess(DataReadResponse dataReadResponse) {
                // Parse the result
                for (Bucket bucket : dataReadResponse.getBuckets()) {
                    List<DataSet> dataSets = bucket.getDataSets();
                    for (DataSet dataSet : dataSets) {
                        for (DataPoint dataPoint : dataSet.getDataPoints()) {
                            // Get the burned calories
                            float burnedCalories = dataPoint.getValue(Field.FIELD_CALORIES).asFloat();
                            Log.d("TAG", "onSuccess: "+burnedCalories);
                            // Do something with the burned calories data
                        }
                    }
                }
            }
        });
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