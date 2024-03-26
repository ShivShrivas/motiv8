package com.motiv.motiv8.Retrofit;



import android.util.JsonReader;

import com.google.gson.JsonObject;
import com.motiv.motiv8.model.LoginResponse;
import com.motiv.motiv8.model.RegistrationResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public  interface ApiService {


    @Multipart
    @POST("UploadGallery")
    Call<String> uploadImage(
            @Part MultipartBody.Part[] part, // Image file part
            @Part("Gallery_Id") RequestBody Gallery_Id, // Other parts, if any
            @Part("ChannelPartnerReg_Id") RequestBody ChannelPartnerReg_Id, // Other parts, if any
            @Part("Caption") RequestBody Caption // Other parts, if any
    );

    @GET("Motiv8/ValidateReferral")
    Call<JsonObject> validateReferCode(@Query("AssoMobile") String AssoMobile);

    @GET("Motiv8/Authenticate")
    Call<LoginResponse> getLogin(@Query("sLoginID") String sLoginID,@Query("sPassword") String sPassword);


    @PUT("Motiv8/RegisterAssociate")
    Call<RegistrationResponse> doRegister(@Body JsonObject jsonObject);


}