package com.motiv.motiv8.Retrofit;



import android.util.JsonReader;

import com.google.gson.JsonObject;
import com.motiv.motiv8.model.AllLedgerResponse;
import com.motiv.motiv8.model.AllPointLedgerResponse;
import com.motiv.motiv8.model.AllProductResponse;
import com.motiv.motiv8.model.LoginResponse;
import com.motiv.motiv8.model.PincodeResponse;
import com.motiv.motiv8.model.ProfilePageDetails;
import com.motiv.motiv8.model.PushStepResponse;
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


    @GET("Pincode/TTPLPincode")
    Call<PincodeResponse> getPincodeDetails(@Query("pincode") String pincode);

    @GET("motiv8/productList")
    Call<AllProductResponse> getAllProductList(@Query("sLoginID") String sLoginID,@Query("sPassword") String sPassword);

    @GET("motiv8/PushStep")
    Call<PushStepResponse> claimStepsWithDate(@Query("sLoginID") String sLoginID, @Query("sPassword") String sPassword,@Query("stepCnt") int stepCnt,@Query("forDateDDMMYYYY") String forDateDDMMYYYY);

    @GET("motiv8/StepLedger")
    Call<AllLedgerResponse> getAllLedger(@Query("sLoginID") String sLoginID, @Query("sPassword") String sPassword, @Query("fromDateDDMMYYYY") String fromDateDDMMYYYY,@Query("toDateDDMMYYYY") String toDateDDMMYYYY);


    @GET("motiv8/PointLedger")
    Call<AllPointLedgerResponse> getAllPointLedger(@Query("sLoginID") String sLoginID, @Query("sPassword") String sPassword, @Query("fromDateDDMMYYYY") String fromDateDDMMYYYY, @Query("toDateDDMMYYYY") String toDateDDMMYYYY);




    @GET("Motiv8/GetProfile")
    Call<ProfilePageDetails> getProfileDetails(@Query("sLoginID") String sLoginID, @Query("sPassword") String sPassword);


    @PUT("Motiv8/UpdateProfile")
    Call<ProfilePageDetails> updateProfileDetails(@Body JsonObject jsonObject);




}