package com.motiv.motiv8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.motiv.motiv8.Retrofit.ApiService;
import com.motiv.motiv8.Retrofit.RestClient;
import com.motiv.motiv8.model.RegistrationResponse;

import javax.security.auth.login.LoginException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
EditText etPassword,etEmail,etName,etState,etCity,etCounry,etPINCODE,etMobile,etReferCode;
TextView loginText;
Button loginButton;
CheckBox cbRef;

LinearLayout refLayout;
Intent intent;
int refValid=0;
String referCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        etPINCODE=findViewById(R.id.etPINCODE);
        etPassword=findViewById(R.id.etPassword);
        etEmail=findViewById(R.id.etEmail);
        etName=findViewById(R.id.etName);
        etState=findViewById(R.id.etState);
        etCity=findViewById(R.id.etCity);
        etCounry=findViewById(R.id.etCounry);
        loginText=findViewById(R.id.loginText);
        etReferCode=findViewById(R.id.etReferCode);
        loginButton=findViewById(R.id.loginButton);
        etMobile=findViewById(R.id.etMobile);
        refLayout=findViewById(R.id.refLayout);
        refLayout.setVisibility(View.GONE);
        cbRef=findViewById(R.id.cbRef);
        etReferCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etReferCode.getText().toString().length()<4){
                    etReferCode.setError("Please enter valid referral code");
                }else{
                        checkReferralCode();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cbRef.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    refLayout.setVisibility(View.VISIBLE);
                }else{
                    refLayout.setVisibility(View.GONE);
                }            }
        });

        intent=getIntent();
        referCode=intent.getStringExtra("referCode");

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()){
                    RestClient restClient=new RestClient();
                    ApiService apiService=restClient.getApiService();
                    Call<RegistrationResponse> call=apiService.doRegister(getJsonObjeReg());
                    call.enqueue(new Callback<RegistrationResponse>() {
                        @Override
                        public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                            Log.d("TAG", "onResponse: "+response.body().getStatusMessage());

                            if (response.isSuccessful()){
                                RegistrationResponse registrationResponse=response.body();
                                if (registrationResponse.getAssocaite()!=null){
                                    Toast.makeText(RegistrationActivity.this, registrationResponse.getStatusMessage(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(RegistrationActivity.this, "Please login with your mobile number and password", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegistrationActivity.this,Dashboard_Page.class));
                                }else{
                                    Toast.makeText(RegistrationActivity.this, registrationResponse.getStatusMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                            Toast.makeText(RegistrationActivity.this, t.getCause().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });



                }
            }
        });


    }

    private void checkReferralCode() {
        RestClient restClient=new RestClient();
        ApiService apiService=restClient.getApiService();
        Call<JsonObject> call=apiService.validateReferCode(etReferCode.getText().toString().trim());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    JsonObject jsonObject=response.body();
                    Log.d("TAG", "onResponse: "+response.body());

                    try {
                        if (jsonObject.get("assocaite").getAsJsonObject()!=null){
                            Toast.makeText(RegistrationActivity.this, "Referral code is valid", Toast.LENGTH_SHORT).show();
                            refValid=1;
                        }else{
                            etReferCode.setError("Please Enter valid Referral code.");
                            Toast.makeText(RegistrationActivity.this, "Please Enter valid Referral code (01)", Toast.LENGTH_SHORT).show();
                            refValid=0;
                        }
                    }catch (Exception e){
                        etReferCode.setError("Please Enter valid Referral code.");
                        Toast.makeText(RegistrationActivity.this, "Please Enter valid Referral code (02)", Toast.LENGTH_SHORT).show();
                        refValid=0;
                    }
                }else{
                    etReferCode.setError("Please Enter valid Referral code.");
                    Toast.makeText(RegistrationActivity.this, "Please Enter valid Referral code (03)", Toast.LENGTH_SHORT).show();
                    refValid=0;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                etReferCode.setError("Please Enter valid Referral code.");
                Toast.makeText(RegistrationActivity.this, "Please Enter valid Referral code (04)", Toast.LENGTH_SHORT).show();
                refValid=0;
            }
        });
    }

    private JsonObject getJsonObjeReg() {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("strPWD", etPassword.getText().toString().trim());
        jsonObject.addProperty("strSolution", "Test");
        jsonObject.addProperty("strFullName", etName.getText().toString().trim());
        jsonObject.addProperty("strMobile", etMobile.getText().toString().trim());
        jsonObject.addProperty("strEmail", etEmail.getText().toString().trim());
        jsonObject.addProperty("pincode", etPINCODE.getText().toString().trim());
        jsonObject.addProperty("city", etCity.getText().toString().trim());
        jsonObject.addProperty("state", etState.getText().toString().trim());
        jsonObject.addProperty("country", etCounry.getText().toString().trim());
        jsonObject.addProperty("strReferBy", referCode);


        return  jsonObject;
    }

    private boolean validateForm() {
        if (etName.getText().toString().trim().length()<1){
            etName.setError("Please Enter Name");
            return false;
        }else     if (etMobile.getText().toString().trim().length()<1){
            etMobile.setError("Please Enter Phone Number");
            return false;
        }else     if (etEmail.getText().toString().trim().length()<1){
            etEmail.setError("Please Enter Email");
            return false;
        }else     if (etPassword.getText().toString().trim().length()<1){
            etPassword.setError("Please Enter Password");
            return false;
        }else     if (etCity.getText().toString().trim().length()<1){
            etCity.setError("Please Enter City");
            return false;
        }else     if (etState.getText().toString().trim().length()<1){
            etState.setError("Please Enter State Name");
            return false;
        }else     if (etCounry.getText().toString().trim().length()<1){
            etCounry.setError("Please Enter Country Name");
            return false;
        }else     if (etPINCODE.getText().toString().trim().length()<1){
            etPINCODE.setError("Please Enter PIN code");
            return false;
        }else if (refLayout.getVisibility()==View.VISIBLE && refValid==0){
            etReferCode.setError("Please Enter Valid Referral code");
            Toast.makeText(this, "Invalid Referral code", Toast.LENGTH_SHORT).show();
            return  false;
        }
        else {
            return true;
        }
    }
}