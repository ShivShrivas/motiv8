package com.motiv.motiv8.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.motiv.motiv8.HomePage;
import com.motiv.motiv8.R;
import com.motiv.motiv8.Retrofit.ApiService;
import com.motiv.motiv8.Retrofit.RestClient;
import com.motiv.motiv8.Utils.CustomProgress;
import com.motiv.motiv8.Utils.MySharedPreferences;
import com.motiv.motiv8.model.LoginResponse;
import com.motiv.motiv8.model.ProductDetail;
import com.motiv.motiv8.model.SingleProducDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsPage extends AppCompatActivity {

    String producId;
    CardView cardView;
    TextView tvBalance;
    TextView textView2;
    ImageView imgVProductImage;
    TextView txtProductName;
    TextView txtProductComp_Brand;
    TextView txtProductDesc;
    TextView txtMRP;
    TextView txtMotiv8Price;
    TextView txtReqMotiv8PNT;
    TextView txtMotiv8AvalPNT;
    TextView txtDiliveryCharg;
    Button btnBuyNow;
    Intent intent;
    ImageView imgHomeBtn;
    CustomProgress customProgress;
    LoginResponse loginResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details_page);
        loginResponse= MySharedPreferences.getLoginObject(ProductDetailsPage.this, LoginResponse.class);
        intent=getIntent();
        producId=intent.getStringExtra("productID");
        customProgress=new CustomProgress();
        cardView = findViewById(R.id.cardView);
        imgHomeBtn = findViewById(R.id.imgHomeBtn);

        tvBalance = findViewById(R.id.tvBalance);
        textView2 = findViewById(R.id.textView2);
        imgVProductImage = findViewById(R.id.imgVProductImage);
        txtProductName = findViewById(R.id.txtProductName);
        txtProductComp_Brand = findViewById(R.id.txtProductComp_Brand);
        txtProductDesc = findViewById(R.id.txtProductDesc);
        txtMRP = findViewById(R.id.txtMRP);
        txtMotiv8Price = findViewById(R.id.txtMotiv8Price);
        txtReqMotiv8PNT = findViewById(R.id.txtReqMotiv8PNT);
        txtMotiv8AvalPNT = findViewById(R.id.txtMotiv8AvalPNT);
        txtDiliveryCharg = findViewById(R.id.txtDiliveryCharg);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        imgHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getProductDetails();

    }

    private void getProductDetails() {
        customProgress.showProgress(ProductDetailsPage.this,"Fetching data from server...",false);

        RestClient restClient=new RestClient();
        ApiService apiService= restClient.getApiService();
        Log.d("TAG", "getProductDetails: "+loginResponse.getUserDetail().getStrLoginID()+"///"+loginResponse.getUserDetail().getStrPWD()+"///"+producId);
        Call<SingleProducDetails> call=apiService.getSingleProducDetails(loginResponse.getUserDetail().getStrLoginID(),loginResponse.getUserDetail().getStrPWD(),producId);
        call.enqueue(new Callback<SingleProducDetails>() {
            @Override
            public void onResponse(Call<SingleProducDetails> call, Response<SingleProducDetails> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatusCode()==200){
                        SingleProducDetails singleProducDetails=response.body();
                        setDatatoView(singleProducDetails.getProduct());
                    }
                }else{
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(ProductDetailsPage.this);
                    alertDialog.setTitle("Server Response");
                    alertDialog.setMessage(response.message());
                    alertDialog.setPositiveButton("Rerty", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getProductDetails();
                        }
                    });
                    alertDialog.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });
                }
                customProgress.hideProgress();

            }

            @Override
            public void onFailure(Call<SingleProducDetails> call, Throwable t) {
                customProgress.hideProgress();
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(ProductDetailsPage.this);
                alertDialog.setTitle("Server Response");
                alertDialog.setMessage(t.getMessage());
                alertDialog.setPositiveButton("Rerty", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getProductDetails();
                    }
                });
                alertDialog.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });
            }
        });

    }

    private void setDatatoView(ProductDetail product) {
        Log.d("TAG", "setDatatoView: "+product.getThumbnail());
        Glide.with(ProductDetailsPage.this).load(product.getThumbnail().toString().trim()).into(imgVProductImage);

        txtProductName.setText(product.getProductName());
                txtProductComp_Brand.setText(product.getCompanyName()+" | "+product.getBrandName());
        txtProductDesc.setText(product.getDescr());
                txtMRP.setText("M.R.P. : ₹"+product.getMrp());
        txtMotiv8Price.setText("Motiv8 Price : ₹"+product.getMotivePrice());
                txtReqMotiv8PNT.setText("Motiv8 Cash Required : "+product.getMotivPoint());
        txtMotiv8AvalPNT.setText("Available Cash point : "+loginResponse.getUserDetail().getMotiv8Point());
                txtDiliveryCharg.setText("Delivery Charges : ₹"+product.getDeliveryCharge());
        tvBalance.setText("Balance: ₹"+loginResponse.getUserDetail().getMotiv8Point());
    }
}