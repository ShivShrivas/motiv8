package com.motiv.motiv8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MenuPage extends AppCompatActivity {

    ImageView ivArrowBack;
    LinearLayout llStepLedger,llPointsLedger;
    String userId,password;

    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);
        i=getIntent();
        userId=i.getStringExtra("userId");
        password=i.getStringExtra("password");
        ivArrowBack=findViewById(R.id.ivArrowBack);
        llPointsLedger=findViewById(R.id.llPointsLedger);
        llStepLedger=findViewById(R.id.llStepLedger);
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