package com.motiv.motiv8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.strictmode.FragmentStrictMode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.motiv.motiv8.Utils.MySharedPreferences;
import com.motiv.motiv8.model.LoginResponse;

public class MenuPage extends AppCompatActivity {

    ImageView ivArrowBack;
    LinearLayout llStepLedger,llPointsLedger;
    String userId,password;
    LinearLayout llLogout;

    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);
        i=getIntent();
        userId=i.getStringExtra("userId");
        password=i.getStringExtra("password");
        ivArrowBack=findViewById(R.id.ivArrowBack);
        llLogout=findViewById(R.id.llLogout);
        llPointsLedger=findViewById(R.id.llPointsLedger);
        llStepLedger=findViewById(R.id.llStepLedger);
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