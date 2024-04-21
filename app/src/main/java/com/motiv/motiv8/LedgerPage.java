package com.motiv.motiv8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.motiv.motiv8.Adapter.LedgerAdapter;
import com.motiv.motiv8.Adapter.PointLedgerAdapter;
import com.motiv.motiv8.Retrofit.ApiService;
import com.motiv.motiv8.Retrofit.RestClient;
import com.motiv.motiv8.model.AllLedgerResponse;
import com.motiv.motiv8.model.AllPointLedgerResponse;
import com.motiv.motiv8.model.AllProductResponse;
import com.motiv.motiv8.model.LedgerDetails;
import com.motiv.motiv8.model.PointLedgerDetails;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LedgerPage extends AppCompatActivity {

    private Button btnStartDate, btnEndDate, btnSubmit;
    private RecyclerView recyclerView;

    Intent i;
    String type;

    private Calendar fromDateCalendar, toDateCalendar;
    private SimpleDateFormat dateFormat;

    ImageView ivToDateCal,ivFromDateCal;
    TextView txtToDate,txtFromDate;
    String userId,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_page);
        i=getIntent();
        type=i.getStringExtra("type");
        userId=i.getStringExtra("userId");
        password=i.getStringExtra("password");
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        ivFromDateCal=findViewById(R.id.ivFromDateCal);
        ivToDateCal=findViewById(R.id.ivToDateCal);
        btnSubmit=findViewById(R.id.btnSubmit);
        txtFromDate=findViewById(R.id.txtFromDate);
        txtFromDate.setText(getCurrentDate());
        txtToDate=findViewById(R.id.txtToDate);
        txtToDate.setText(getCurrentDate());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fromDateCalendar = Calendar.getInstance();
        toDateCalendar = Calendar.getInstance();

        ivFromDateCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true );
            }
        });

        ivToDateCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestClient restClient=new RestClient();
                ApiService apiService=restClient.getApiService();
                if (type.equals("point")){

                    Call<AllLedgerResponse>  call=apiService.getAllLedger(userId,password,txtFromDate.getText().toString().trim(),txtToDate.getText().toString().trim());
                    call.enqueue(new Callback<AllLedgerResponse>() {
                        @Override
                        public void onResponse(Call<AllLedgerResponse> call, Response<AllLedgerResponse> response) {
                            if (response.isSuccessful()){
                                AllLedgerResponse allLedgerResponse=response.body();
                                List<LedgerDetails> ledgerDetails=allLedgerResponse.getLedger();
                                recyclerView.setAdapter(new LedgerAdapter(LedgerPage.this,ledgerDetails));


                            }
                        }

                        @Override
                        public void onFailure(Call<AllLedgerResponse> call, Throwable t) {

                        }
                    });

                }else{
                    Call<AllPointLedgerResponse> call=apiService.getAllPointLedger(userId,password,txtFromDate.getText().toString().trim(),txtToDate.getText().toString().trim());
                    call.enqueue(new Callback<AllPointLedgerResponse>() {
                        @Override
                        public void onResponse(Call<AllPointLedgerResponse> call, Response<AllPointLedgerResponse> response) {
                            if (response.isSuccessful()){
                                AllPointLedgerResponse allPointLedgerResponse=response.body();
                                List<PointLedgerDetails> ledgerDetails=allPointLedgerResponse.getLedger();
                                recyclerView.setAdapter(new PointLedgerAdapter(LedgerPage.this,ledgerDetails));


                            }
                        }

                        @Override
                        public void onFailure(Call<AllPointLedgerResponse> call, Throwable t) {

                        }
                    });

                }
            }
        });






    }
    private String getCurrentDate() {
        return dateFormat.format(Calendar.getInstance().getTime());
    }
    private void showDatePickerDialog(final boolean isFromDate) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (isFromDate) {
                    fromDateCalendar.set(Calendar.YEAR, year);
                    fromDateCalendar.set(Calendar.MONTH, monthOfYear);
                    fromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    txtFromDate.setText(dateFormat.format(fromDateCalendar.getTime()));
                } else {
                    toDateCalendar.set(Calendar.YEAR, year);
                    toDateCalendar.set(Calendar.MONTH, monthOfYear);
                    toDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    txtToDate.setText(dateFormat.format(toDateCalendar.getTime()));
                }
            }
        };

        Calendar calendar;
        if (isFromDate) {
            calendar = fromDateCalendar;
        } else {
            calendar = toDateCalendar;
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                LedgerPage.this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}