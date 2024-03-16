package com.motiv.motiv8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Dashboard_Page extends AppCompatActivity {

    TextView tvBalance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_page);
        tvBalance=findViewById(R.id.tvBalance);

        tvBalance.setText(Html.fromHtml(getString(R.string.balance_132_2)));
    }
}