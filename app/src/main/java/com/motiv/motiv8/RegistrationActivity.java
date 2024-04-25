package com.motiv.motiv8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.motiv.motiv8.Utils.CustomProgress;
import com.motiv.motiv8.model.Pincode;
import com.motiv.motiv8.model.PincodeResponse;
import com.motiv.motiv8.model.RegistrationResponse;

import javax.security.auth.login.LoginException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity {
EditText etPassword,etEmail,etName,etState,etCity,etCounry,etPINCODE,etMobile,etReferCode;
TextView loginText;
Button loginButton;
CheckBox cbRef;

LinearLayout refLayout;
Intent intent;
int refValid=0;
String referCode;

    CustomProgress customProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        customProgress=new CustomProgress();
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
        etPINCODE.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etPINCODE.getText().toString().trim().length()==6){
                    customProgress.showProgress(RegistrationActivity.this,"Data Fetching from PINCODE",false);
                 Retrofit   retrofit = new Retrofit.Builder()
                            .baseUrl("https://api.tantrash.com/API/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();


                    ApiService apiService=retrofit.create(ApiService.class);
                    Call<PincodeResponse> call=apiService.getPincodeDetails(etPINCODE.getText().toString().trim());
                    call.enqueue(new Callback<PincodeResponse>() {
                        @Override
                        public void onResponse(Call<PincodeResponse> call, Response<PincodeResponse> response) {
                            if (response.isSuccessful()){
                                PincodeResponse pincodeResponse=response.body();
                                if (pincodeResponse.getPincodeList().size()>=1){

                                    Pincode pincode=pincodeResponse.getPincodeList().get(0);
                                    etCity.setText(pincode.getDistrictName());
                                    etState.setText(pincode.getStateName());
                                    etCounry.setText(pincode.getCountryName());
                                }
                            }else{
                                etCity.setText("");
                                etState.setText("");
                                etCounry.setText("");
                                Toast.makeText(RegistrationActivity.this, "Please check and enter correct PINCODE", Toast.LENGTH_SHORT).show();
                            }
                            customProgress.hideProgress();
                        }

                        @Override
                        public void onFailure(Call<PincodeResponse> call, Throwable t) {
                            etCity.setText("");
                            etState.setText("");
                            etCounry.setText("");
                            Toast.makeText(RegistrationActivity.this, "Sorry! we did not fetch the details", Toast.LENGTH_SHORT).show();
                            customProgress.hideProgress();


                        }
                    });
                }else{
                    etCity.setText("");
                    etState.setText("");
                    etCounry.setText("");
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
                    customProgress.showProgress(RegistrationActivity.this,"Registration in progress...",false);
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
                                    AlertDialog.Builder builder=new AlertDialog.Builder(RegistrationActivity.this);
                                    builder.setCancelable(false);
                                    builder.setTitle("Registration Response");
                                    builder.setMessage(registrationResponse.getStatusMessage());
                                    builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(RegistrationActivity.this, "Please login with your mobile number and password", Toast.LENGTH_SHORT).show();
                                            dialogInterface.dismiss();
                                            startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                                            finish();

                                        }
                                    });
                                    AlertDialog alertDialog=builder.create();
                                    alertDialog.show();


                                }else{
                                    Toast.makeText(RegistrationActivity.this, registrationResponse.getStatusMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            customProgress.hideProgress();
                        }

                        @Override
                        public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                            Toast.makeText(RegistrationActivity.this, t.getCause().getMessage(), Toast.LENGTH_SHORT).show();
                            customProgress.hideProgress();


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
        customProgress.showProgress(RegistrationActivity.this,"Validating Details",false);
        if (etName.getText().toString().trim().length()<1){
            etName.setError("Please Enter Name");
            customProgress.hideProgress();
            return false;
        }else     if (etMobile.getText().toString().trim().length()<1){
            etMobile.setError("Please Enter Phone Number");
            customProgress.hideProgress();

            return false;
        }else     if (etEmail.getText().toString().trim().length()<1){
            etEmail.setError("Please Enter Email");
            customProgress.hideProgress();

            return false;
        }else     if (etPassword.getText().toString().trim().length()<1){
            etPassword.setError("Please Enter Password");
            customProgress.hideProgress();

            return false;
        }else     if (etCity.getText().toString().trim().length()<1){
            etCity.setError("Please Enter City");
            customProgress.hideProgress();

            return false;
        }else     if (etState.getText().toString().trim().length()<1){
            etState.setError("Please Enter State Name");
            customProgress.hideProgress();

            return false;
        }else     if (etCounry.getText().toString().trim().length()<1){
            etCounry.setError("Please Enter Country Name");
            customProgress.hideProgress();

            return false;
        }else     if (etPINCODE.getText().toString().trim().length()<1){
            etPINCODE.setError("Please Enter PIN code");
            customProgress.hideProgress();

            return false;
        }else if (refLayout.getVisibility()==View.VISIBLE && refValid==0){
            etReferCode.setError("Please Enter Valid Referral code");
            Toast.makeText(this, "Invalid Referral code", Toast.LENGTH_SHORT).show();
            customProgress.hideProgress();

            return  false;
        }
        else {
            customProgress.hideProgress();

            return true;
        }
    }
}