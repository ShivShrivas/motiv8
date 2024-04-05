package com.motiv.motiv8.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.motiv.motiv8.Dashboard_Page;
import com.motiv.motiv8.R;

public class HomePage extends AppCompatActivity {

    String userName;
    TextView txtUserName;
    Intent intent;
    CardView cvStepCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        intent=getIntent();
        txtUserName=findViewById(R.id.txtUserName);
        cvStepCount=findViewById(R.id.cvStepCount);
        userName= intent.getStringExtra("username");
        txtUserName.setText(userName);
        cvStepCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, Dashboard_Page.class));
            }
        });
    }
}