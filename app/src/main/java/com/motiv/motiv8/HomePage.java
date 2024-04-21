package com.motiv.motiv8;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.data.FileDescriptorLocalUriFetcher;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.motiv.motiv8.Adapter.AllProductsAdapter;
import com.motiv.motiv8.BackgroundServices.CountReciver;
import com.motiv.motiv8.BackgroundServices.StepCountingService;
import com.motiv.motiv8.Dashboard_Page;
import com.motiv.motiv8.MenuPage;
import com.motiv.motiv8.R;
import com.motiv.motiv8.Retrofit.ApiService;
import com.motiv.motiv8.Retrofit.RestClient;
import com.motiv.motiv8.UI.ProfilePage;
import com.motiv.motiv8.model.AllProductResponse;
import com.motiv.motiv8.model.PincodeResponse;
import com.motiv.motiv8.model.PushStepResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePage extends AppCompatActivity implements CountReciver.StepCountUpdateListener{

    String userName,userId,password;
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
    ImageView ic_menubar;
    RecyclerView recViewProducts;
    CardView cvClaimStep;
    Intent serviceIntent;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        intent=getIntent();
        txtUserName=findViewById(R.id.txtUserName);
        ic_menubar=findViewById(R.id.ic_menubar);
      //  cvStepCount=findViewById(R.id.cvStepCount);
        userName= intent.getStringExtra("username");
        userId= intent.getStringExtra("userId");
        password= intent.getStringExtra("password");
        txtUserName.setText(userName);
        txtStepCount=findViewById(R.id.txtStepCount);
        recViewProducts=findViewById(R.id.recViewProducts);
        cvClaimStep=findViewById(R.id.cvClaimStep);
        cvClaimStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startStep==0){
                    Toast.makeText(HomePage.this, "you should have at least one step", Toast.LENGTH_SHORT).show();
                }else{
                    Date currentDate = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String currentDateFormatted = dateFormat.format(currentDate);
                    RestClient restClient=new RestClient();
                    ApiService apiService=restClient.getApiService();
                    Call<PushStepResponse> call=apiService.claimStepsWithDate(userId,password,startStep,currentDateFormatted);
                    call.enqueue(new Callback<PushStepResponse>() {
                        @Override
                        public void onResponse(Call<PushStepResponse> call, Response<PushStepResponse> response) {
                            if (response.isSuccessful()){
                                PushStepResponse pushStepResponse=response.body();
                                if (pushStepResponse.getStatusCode()==200){
                                    Toast.makeText(HomePage.this, "Steps claimed successfully!!", Toast.LENGTH_SHORT).show();
                                }else if(pushStepResponse.getStatusCode()==100){
                                    Toast.makeText(HomePage.this, pushStepResponse.getStatusMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }else {

                                Toast.makeText(HomePage.this, "something went wrong please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<PushStepResponse> call, Throwable t) {
                            Toast.makeText(HomePage.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    Intent intent = new Intent(HomePage.this, StepCountingService.class);
                    stopService(intent);

                }
             
            }
        });
        recViewProducts.setLayoutManager(new LinearLayoutManager(this));
        getAllProducts();
        ic_menubar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HomePage.this, MenuPage.class);
                i.putExtra("userId",userId);
                i.putExtra("password",password);
                startActivity(i);
            }
        });
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
//        cvStepCount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//           //     startActivity(new Intent(HomePage.this, Dashboard_Page.class));
//            }
//        });
        // Register broadcast receiver
        stepCountReceiver = new CountReciver(this);
        IntentFilter filter = new IntentFilter("step_count_broadcast");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(stepCountReceiver, filter, RECEIVER_EXPORTED);
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                registerReceiver(stepCountReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
            }else {
                registerReceiver(stepCountReceiver, filter);
            }
        }
    }

    private void getAllProducts() {
        RestClient restClient=new RestClient();
        ApiService apiService= restClient.getApiService();
        Call<AllProductResponse> call=apiService.getAllProductList(userId,password);
        call.enqueue(new Callback<AllProductResponse>() {
            @Override
            public void onResponse(Call<AllProductResponse> call, Response<AllProductResponse> response) {
                if (response.isSuccessful()){
                    AllProductResponse allProductResponse=response.body();
                    recViewProducts.setAdapter(new AllProductsAdapter(HomePage.this,allProductResponse.getProducts()));
                }
            }

            @Override
            public void onFailure(Call<AllProductResponse> call, Throwable t) {

            }
        });

    }

//    private void updateTotalSteps(int steps) {
//
//        updateTextRunnable = new Runnable() {
//            @Override
//            public void run() {
//                // Increment total steps
//                startStep++;
//
//                // Update text view with the current value
//                Log.d("TAG", "Step Count: " + totalSteps);
//                txtStepCount.setText("Total Steps:  "+String.valueOf(startStep));
//
//                // Check if total steps reaches the maximum value
//                if (startStep >= steps) {
//                    // Stop the handler
//                    handler.removeCallbacks(updateTextRunnable);
//                    // Reset total steps to minimum value
//                    startStep = steps;
//                    // Pause for the next event
//                    // You can trigger the next event or action here
//                } else {
//                    // Schedule the next update after one second
//                    handler.postDelayed(this, 1000);
//                }
//            }
//        };
//
//
//        handler.post(updateTextRunnable);
//
//
//
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1100) {
            // Handle sign-in result
            if (resultCode == RESULT_OK && data != null) {

                serviceIntent = new Intent(this, StepCountingService.class);
                startService(serviceIntent);

            } else {
                // Sign-in failed or user canceled the operation
                Log.d("TAG", "Sign-in failed or user canceled");
                Toast.makeText(this, "Sign-in failed or user canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStepCountUpdate(int totalSteps) {
        if (firstTimeGetData == 0) {
            startStep = totalSteps;
            firstTimeGetData++;
            txtStepCount.setText("Total Steps: " + startStep);
        }

        final Handler handler = new Handler();

        final int finalTotalSteps = totalSteps; // Declare as final

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Increment total steps
                startStep++;

                txtStepCount.setText("Total Steps: " + startStep);

                // Check if total steps reaches the maximum value
                if (finalTotalSteps <= startStep) {
                    // Stop the handler
                    handler.removeCallbacks(this);
                    // Reset total steps to maximum value
                    startStep = finalTotalSteps;
                    // Pause for the next event
                    // You can trigger the next event or action here
                } else {
                    // Schedule the next update after one second
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.post(runnable);
    }
}