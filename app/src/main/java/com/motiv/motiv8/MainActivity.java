package com.motiv.motiv8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.motiv.motiv8.Retrofit.ApiService;
import com.motiv.motiv8.Retrofit.RestClient;

import com.motiv.motiv8.Utils.CustomProgress;
import com.motiv.motiv8.Utils.MySharedPreferences;
import com.motiv.motiv8.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
Button loginButton;
TextView signupText;

EditText etpassword,etusername;
CustomProgress customProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customProgress=new CustomProgress();
        loginButton=findViewById(R.id.loginButton);
        signupText=findViewById(R.id.signupText);
        etusername=findViewById(R.id.etusername);
        etpassword=findViewById(R.id.etpassword);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (etusername.getText().toString().trim().length()<1){
                   etusername.setError("Please Enter User Name");
                   Toast.makeText(MainActivity.this, "Username Invalid!!", Toast.LENGTH_SHORT).show();
               }else  if (etpassword.getText().toString().trim().length()<1){
                   etpassword.setError("Please Enter Password");
                   Toast.makeText(MainActivity.this, "Password Invalid!!", Toast.LENGTH_SHORT).show();
               }else {
                   customProgress.showProgress(MainActivity.this,"Login in progress..",false);

                   RestClient restClient=new RestClient();
                   ApiService apiService=restClient.getApiService();
                   Call<LoginResponse> call=apiService.getLogin(etusername.getText().toString().trim(),etpassword.getText().toString().trim());
                   call.enqueue(new Callback<LoginResponse>() {
                       @Override
                       public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                           if (response.isSuccessful()){
                               LoginResponse loginResponse=response.body();
                               if (loginResponse.getUserDetail()!=null){
                                   MySharedPreferences.saveLoginObject(getApplicationContext(), loginResponse);
                                   Intent i=new Intent(MainActivity.this, HomePage.class);
                                   i.putExtra("username",loginResponse.getUserDetail().getStrFullName().isEmpty()?"Unknown":loginResponse.getUserDetail().getStrFullName());
                                   i.putExtra("userId",etusername.getText().toString().trim());
                                   i.putExtra("password",etpassword.getText().toString().trim());
                                                                    startActivity(i);
                                                                    finish();
                               }else{
                                   Toast.makeText(MainActivity.this, loginResponse.getStatusMessage(), Toast.LENGTH_SHORT).show();
                               }

                           }else{
                               Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                           }
                           customProgress.hideProgress();

                       }

                       @Override
                       public void onFailure(Call<LoginResponse> call, Throwable t) {
                           Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                           customProgress.hideProgress();

                       }
                   });
               }
            }
        });
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,RegistrationActivity.class);
                i.putExtra("referCode","");
                startActivity(i);
            }
        });
    }
}