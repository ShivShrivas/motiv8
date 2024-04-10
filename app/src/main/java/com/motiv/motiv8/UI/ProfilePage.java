package com.motiv.motiv8.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.motiv.motiv8.R;

public class ProfilePage extends AppCompatActivity {

    TextView edit_text;
    EditText editTextName;
    EditText editTextGender;
    EditText editTextDOB;
    EditText editTextLocation;
    EditText editTextInterests;
    EditText editTextHeight;
    EditText editTextWeight;
    EditText editTextUnit;
    EditText editTextNumber;
    EditText editTextEmail;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        edit_text=findViewById(R.id.edit_text);

        editTextName = findViewById(R.id.editTextName);
        editTextGender = findViewById(R.id.editTextGender);
        editTextDOB = findViewById(R.id.editTextDOB);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextInterests = findViewById(R.id.editTextInterests);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextUnit = findViewById(R.id.editTextUnit);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        button = findViewById(R.id.button);


        edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enabledPage();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageDiabled();
            }
        });



        pageDiabled();
    }

    private void pageDiabled() {
        editTextName.setEnabled(false);
                editTextGender.setEnabled(false);
        editTextDOB.setEnabled(false);
                editTextLocation.setEnabled(false);
        editTextInterests.setEnabled(false);
                editTextHeight.setEnabled(false);
        editTextWeight.setEnabled(false);
                editTextUnit.setEnabled(false);
        editTextNumber.setEnabled(false);
                editTextEmail.setEnabled(false);
        button.setVisibility(View.GONE);
        edit_text.setVisibility(View.VISIBLE);
    }

    private void enabledPage() {
        edit_text.setVisibility(View.GONE);
        editTextName.setEnabled(true);
        editTextGender.setEnabled(true);
        editTextDOB.setEnabled(true);
        editTextLocation.setEnabled(true);
        editTextInterests.setEnabled(true);
        editTextHeight.setEnabled(true);
        editTextWeight.setEnabled(true);
        editTextUnit.setEnabled(true);
        editTextNumber.setEnabled(true);
        button.setVisibility(View.VISIBLE);
        editTextEmail.setEnabled(true);

    }
}