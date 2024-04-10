package com.motiv.motiv8.model;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.motiv.motiv8.BackgroundServices.StepCountingService;
import com.motiv.motiv8.Dashboard_Page;
import com.motiv.motiv8.R;
import com.motiv.motiv8.UI.ProfilePage;

public class HomePage extends AppCompatActivity {

    String userName;
    TextView txtUserName;
    Intent intent;
    CardView cvStepCount;
    private Handler handler;
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
    int startStep=0;
    private BroadcastReceiver stepCountReceiver;
    Runnable updateTextRunnable;

    int firstTimeGetData=0;
    TextView txtStepCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        intent=getIntent();
        txtUserName=findViewById(R.id.txtUserName);
        cvStepCount=findViewById(R.id.cvStepCount);
        userName= intent.getStringExtra("username");
        txtUserName.setText(userName);
        txtStepCount=findViewById(R.id.txtStepCount);
        findViewById(R.id.cvProfilePic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, ProfilePage.class));
            }
        });

        handler = new Handler(Looper.getMainLooper());
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(FITNESS_ACTIVITY_WRITE_SCOPE),new Scope(FITNESS_LOCATION_READ_SCOPE),new Scope(FITNESS_LOCATION_WRITE_SCOPE), new Scope(FITNESS_BODY_READ_SCOPE),new Scope(FITNESS_BODY_WRITE_SCOPE))
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1100);
        cvStepCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           //     startActivity(new Intent(HomePage.this, Dashboard_Page.class));
            }
        });
        // Register broadcast receiver
        stepCountReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int totalSteps = intent.getIntExtra("totalSteps", 0);
                if (firstTimeGetData<5){

                    txtStepCount.setText("Total Steps:  "+String.valueOf(startStep));
                    firstTimeGetData++;
                }
                updateTotalSteps(totalSteps);
            }
        };
        IntentFilter filter = new IntentFilter("step_count_broadcast");
        registerReceiver(stepCountReceiver, filter);
    }
    private void updateTotalSteps(int steps) {

        updateTextRunnable = new Runnable() {
            @Override
            public void run() {
                // Increment total steps
                startStep++;

                // Update text view with the current value
                Log.d("TAG", "Step Count: " + totalSteps);
                txtStepCount.setText("Total Steps:  "+String.valueOf(startStep));

                // Check if total steps reaches the maximum value
                if (startStep >= steps) {
                    // Stop the handler
                    handler.removeCallbacks(updateTextRunnable);
                    // Reset total steps to minimum value
                    startStep = steps;
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1100) {
            // Handle sign-in result
            if (resultCode == RESULT_OK && data != null) {

                Intent serviceIntent = new Intent(this, StepCountingService.class);
                startService(serviceIntent);
            } else {
                // Sign-in failed or user canceled the operation
                Log.d("TAG", "Sign-in failed or user canceled");
                Toast.makeText(this, "Sign-in failed or user canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}