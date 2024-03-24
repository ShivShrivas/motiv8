package com.motiv.motiv8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.motiv.motiv8.Retrofit.ApiService;
import com.motiv.motiv8.Retrofit.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferPage extends AppCompatActivity {
EditText etReferCode;
Button referButton;
   TextView signupText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_page);
        signupText=findViewById(R.id.signupText);
        referButton=findViewById(R.id.referButton);
        etReferCode=findViewById(R.id.etReferCode);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ReferPage.this,RegistrationActivity.class);
                i.putExtra("referCode","");
                startActivity(i);
            }
        });

        referButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etReferCode.getText().toString().trim().length()<3){
                    etReferCode.setError("Please Enter valid Referral code.");
                    Toast.makeText(ReferPage.this, "Please Enter valid Referral code.", Toast.LENGTH_SHORT).show();
                }else{
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
                                      Intent i=new Intent(ReferPage.this,RegistrationActivity.class);
                                      i.putExtra("referCode",etReferCode.getText().toString());
                                      startActivity(i);
                                  }else{
                                      etReferCode.setError("Please Enter valid Referral code.");
                                      Toast.makeText(ReferPage.this, "Please Enter valid Referral code.", Toast.LENGTH_SHORT).show();

                                  }
                              }catch (Exception e){
                                  etReferCode.setError("Please Enter valid Referral code.");
                                  Toast.makeText(ReferPage.this, "Please Enter valid Referral code.", Toast.LENGTH_SHORT).show();

                              }
                            }else{
                                etReferCode.setError("Please Enter valid Referral code.");
                                Toast.makeText(ReferPage.this, "Please Enter valid Referral code.", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            etReferCode.setError("Please Enter valid Referral code.");
                            Toast.makeText(ReferPage.this, "Please Enter valid Referral code.", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }
}