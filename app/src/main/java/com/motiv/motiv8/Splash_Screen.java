package com.motiv.motiv8;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import android.os.Handler;

import com.motiv.motiv8.Utils.MySharedPreferences;
import com.motiv.motiv8.model.LoginResponse;

public class Splash_Screen extends AppCompatActivity {
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.FOREGROUND_SERVICE

    };

    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
        boolean allPermissionsGranted = true;
        for (Boolean isGranted : permissions.values()) {
            if (!isGranted) {
                allPermissionsGranted = false;
                break;
            }
        }
        if (allPermissionsGranted) {
            LoginResponse loginResponse=MySharedPreferences.getLoginObject(Splash_Screen.this, LoginResponse.class);

            if(loginResponse!=null &&loginResponse.getStatusCode()==200){
                Intent i=new Intent(Splash_Screen.this, HomePage.class);
                i.putExtra("username",loginResponse.getUserDetail().getStrFullName().isEmpty()?"Unknown":loginResponse.getUserDetail().getStrFullName());
                i.putExtra("userId",loginResponse.getUserDetail().getStrLoginID());
                i.putExtra("password",loginResponse.getUserDetail().getStrPWD());
                startActivity(i);
                finish();
            }else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Splash_Screen.this,MainActivity.class));
                        finish();
                    }
                },4000);
            }


        } else {
            // If any permission is not granted, close the app
            showPermissionAlertDialog();
        }
    });
    private boolean checkPermissions() {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        requestPermissionLauncher.launch(PERMISSIONS);
    }

    private void showPermissionAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This app requires all permissions to function properly. Please grant all permissions to continue.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close the app
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (!checkPermissions()) {
            requestPermissions();
        } else {

            LoginResponse loginResponse=MySharedPreferences.getLoginObject(Splash_Screen.this, LoginResponse.class);

            if(loginResponse!=null &&loginResponse.getStatusCode()==200){
                Intent i=new Intent(Splash_Screen.this, HomePage.class);
                i.putExtra("username",loginResponse.getUserDetail().getStrFullName().isEmpty()?"Unknown":loginResponse.getUserDetail().getStrFullName());
                i.putExtra("userId",loginResponse.getUserDetail().getStrLoginID());
                i.putExtra("password",loginResponse.getUserDetail().getStrPWD());
                startActivity(i);
                finish();
            }else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Splash_Screen.this,MainActivity.class));
                        finish();
                    }
                },4000);
            }



        }

    }
}