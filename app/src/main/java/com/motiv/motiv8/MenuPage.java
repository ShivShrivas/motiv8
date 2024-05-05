package com.motiv.motiv8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.strictmode.FragmentStrictMode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.motiv.motiv8.Utils.MySharedPreferences;
import com.motiv.motiv8.model.LoginResponse;

public class MenuPage extends AppCompatActivity {

    ImageView ivArrowBack;
    LinearLayout llStepLedger,llPointsLedger;
    String userId,password,username,profilePicUrl;

    TextView txtUsername;
    LinearLayout llLogout;
    ImageView imgVProfile;

    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);
        i=getIntent();
        userId=i.getStringExtra("userId");
        password=i.getStringExtra("password");
        username=i.getStringExtra("username");
        profilePicUrl=i.getStringExtra("profilePicUrl");
        ivArrowBack=findViewById(R.id.ivArrowBack);
        imgVProfile=findViewById(R.id.imgVProfile);
        txtUsername=findViewById(R.id.txtUsername);
        txtUsername.setText(username);
        llLogout=findViewById(R.id.llLogout);
        llPointsLedger=findViewById(R.id.llPointsLedger);
        llStepLedger=findViewById(R.id.llStepLedger);
        if (profilePicUrl.length()>2){
            Glide.with(MenuPage.this).load(profilePicUrl).into(imgVProfile);
        }
        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginResponse loginResponse=new LoginResponse(0,"",null);
                MySharedPreferences.saveLoginObject(MenuPage.this,loginResponse);
                Intent i=new Intent(MenuPage.this,MainActivity.class);
               i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        llPointsLedger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MenuPage.this,LedgerPage.class);
                i.putExtra("type","point");
                i.putExtra("userId",userId);
                i.putExtra("password",password);
                startActivity(i);

            }
        });

        llStepLedger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MenuPage.this,LedgerPage.class);
                i.putExtra("type","step");
                i.putExtra("userId",userId);
                i.putExtra("password",password);
                startActivity(i);
            }
        });
        ivArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}