package com.motiv.motiv8.UI;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.motiv.motiv8.R;
import com.motiv.motiv8.RegistrationActivity;
import com.motiv.motiv8.Retrofit.ApiService;
import com.motiv.motiv8.Retrofit.RestClient;
import com.motiv.motiv8.Utils.CustomProgress;
import com.motiv.motiv8.model.Pincode;
import com.motiv.motiv8.model.PincodeResponse;
import com.motiv.motiv8.model.Profile;
import com.motiv.motiv8.model.ProfilePageDetails;
import com.soundcloud.android.crop.Crop;
import com.soundcloud.android.crop.CropImageActivity;
import com.soundcloud.android.crop.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfilePage extends AppCompatActivity {

    private static String[] PERMISSIONS = {
            Manifest.permission.CAMERA
    };
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    String userName,userId,password;
    TextView edit_text;
    EditText editTextName;
    EditText editTextSolustion;
    EditText editTextAddress;
    EditText editTextPIN;
    ProfilePageDetails profilePageDetails;
    EditText editTextCity;
    EditText editTextState;
    EditText editTextCountry;
    EditText editTextPANNum;
    EditText editTextNumber;
    EditText editTextEmail;
    ImageView imgProfile;
    Button button;
    ImageView ivArrowBack;
    Intent intent;

    TextView txtProfileChange;
    CustomProgress customProgress,customProgressGetProfile,customProgressUpdateProfile;


    private String currentPhotoPath;
    Uri destinationUri;
    private Uri mCameraImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        intent=getIntent();



        customProgress=new CustomProgress();
        customProgressGetProfile=new CustomProgress();
        customProgressUpdateProfile=new CustomProgress();

        userName= intent.getStringExtra("username");
        userId= intent.getStringExtra("userId");
        password= intent.getStringExtra("password");
        edit_text=findViewById(R.id.edit_text);

        editTextName = findViewById(R.id.editTextName);
        imgProfile = findViewById(R.id.imgProfile);
        editTextSolustion = findViewById(R.id.editTextSolustion);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPIN = findViewById(R.id.editTextPIN);
        editTextCity = findViewById(R.id.editTextCity);
        editTextState = findViewById(R.id.editTextState);
        editTextCountry = findViewById(R.id.editTextCountry);
        editTextPANNum = findViewById(R.id.editTextPANNum);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        button = findViewById(R.id.button);
        txtProfileChange = findViewById(R.id.txtProfileChange);
        ivArrowBack = findViewById(R.id.ivArrowBack);
        getProfileDetails();
        txtProfileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfilePic();
            }
        });
        ivArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfilePage.this, "Now you can edit your profile details...", Toast.LENGTH_SHORT).show();
                enabledPage();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToServer();
                pageDiabled();
            }
        });

        editTextPIN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editTextPIN.getText().toString().trim().length()==6){
                    customProgress.showProgress(ProfilePage.this,"Data Fetching from PINCODE",false);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://api.tantrash.com/API/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();


                    ApiService apiService=retrofit.create(ApiService.class);
                    Call<PincodeResponse> call=apiService.getPincodeDetails(editTextPIN.getText().toString().trim());
                    call.enqueue(new Callback<PincodeResponse>() {
                        @Override
                        public void onResponse(Call<PincodeResponse> call, Response<PincodeResponse> response) {
                            if (response.isSuccessful()){
                                PincodeResponse pincodeResponse=response.body();
                                if (pincodeResponse.getPincodeList().size()>=1){

                                    Pincode pincode=pincodeResponse.getPincodeList().get(0);
                                    editTextCity.setText(pincode.getDistrictName());
                                    editTextState.setText(pincode.getStateName());
                                    editTextCountry.setText(pincode.getCountryName());
                                }
                            }else{
                                editTextCity.setText("");
                                editTextState.setText("");
                                editTextCountry.setText("");
                                Toast.makeText(ProfilePage.this, "Please check and enter correct PINCODE", Toast.LENGTH_SHORT).show();
                            }
                            customProgress.hideProgress();
                        }

                        @Override
                        public void onFailure(Call<PincodeResponse> call, Throwable t) {
                            editTextCity.setText("");
                            editTextState.setText("");
                            editTextCountry.setText("");
                            Toast.makeText(ProfilePage.this, "Sorry! we did not fetch the details", Toast.LENGTH_SHORT).show();
                            customProgress.hideProgress();


                        }
                    });
                }else{
                    editTextCity.setText("");
                    editTextState.setText("");
                    editTextCountry.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pageDiabled();
    }

    private void uploadProfilePic() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {

            String[] PERMISSIONS_Storage = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            PERMISSIONS=PERMISSIONS_Storage;
        }
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Image");
            builder.setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            dispatchTakePictureIntent();
                            break;
                        case 1:
                            openGallery();
                            break;
                    }
                }
            });
            builder.show();
        }


    }

    // Method to open the camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.motiv.motiv8.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // Create a file to save the image
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    // Method to open the gallery
    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , REQUEST_PICK_IMAGE);
    }

    private void saveDataToServer() {
        customProgressUpdateProfile.showProgress(ProfilePage.this,"Sending data to server...",false);

        RestClient restClient=new RestClient();
        ApiService apiService=restClient.getApiService();
        Call<ProfilePageDetails> call= apiService.updateProfileDetails(getProfileJsonObject());
        Log.d("TAG", "saveDataToServer: "+getProfileJsonObject());
        call.enqueue(new Callback<ProfilePageDetails>() {
            @Override
            public void onResponse(Call<ProfilePageDetails> call, Response<ProfilePageDetails> response) {
                if (response.isSuccessful()){
                    if (response.isSuccessful()){
                        profilePageDetails=response.body();
                        if (response.body().getProfile().getLoginID().length()>1){
                            Toast.makeText(ProfilePage.this, "Data Saved Successfully...", Toast.LENGTH_SHORT).show();
                            setDataToView(response.body().getProfile());
                        }else{
                            AlertDialog.Builder alertDialog=new AlertDialog.Builder(ProfilePage.this);
                            alertDialog.setTitle("Server Response");
                            alertDialog.setMessage(response.message());
                            alertDialog.setPositiveButton("Rerty", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    getProfileDetails();
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

                    }
                }
                customProgressUpdateProfile.hideProgress();
            }

            @Override
            public void onFailure(Call<ProfilePageDetails> call, Throwable t) {
                customProgressUpdateProfile.hideProgress();
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(ProfilePage.this);
                alertDialog.setTitle("Server Response");
                alertDialog.setMessage(t.getMessage());
                alertDialog.setPositiveButton("Rerty", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getProfileDetails();
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

    private JsonObject getProfileJsonObject() {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("solutation",editTextSolustion.getText().toString().trim());
        jsonObject.addProperty("fullName",editTextName.getText().toString().trim());
        jsonObject.addProperty("mobile",editTextNumber.getText().toString().trim());
        jsonObject.addProperty("email",editTextEmail.getText().toString().trim());
        jsonObject.addProperty("address",editTextAddress.getText().toString().trim());
        jsonObject.addProperty("pincode",editTextPIN.getText().toString().trim());
        jsonObject.addProperty("city",editTextCity.getText().toString().trim());
        jsonObject.addProperty("state",editTextState.getText().toString().trim());
        jsonObject.addProperty("country",editTextCountry.getText().toString().trim());
        jsonObject.addProperty("pwd",profilePageDetails.getProfile().getPwd());
        jsonObject.addProperty("pancard",editTextPANNum.getText().toString().trim());
        jsonObject.addProperty("loginID",profilePageDetails.getProfile().getLoginID());
        jsonObject.addProperty("panno",editTextPANNum.getText().toString().trim());
        jsonObject.addProperty("associateCode",profilePageDetails.getProfile().getAssociateCode());

        return jsonObject;
    }

    private void getProfileDetails() {
        customProgressGetProfile.showProgress(ProfilePage.this,"Fetching data from server...",false);
        RestClient restClient=new RestClient();
        ApiService apiService=restClient.getApiService();
        Call<ProfilePageDetails> call=apiService.getProfileDetails(userId,password);
        call.enqueue(new Callback<ProfilePageDetails>() {
            @Override
            public void onResponse(Call<ProfilePageDetails> call, Response<ProfilePageDetails> response) {
                if (response.isSuccessful()){
                    profilePageDetails=response.body();
                    if (response.body().getProfile().getLoginID().length()>1){

                        setDataToView(response.body().getProfile());
                        customProgressGetProfile.hideProgress();
                    }
                }else{
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(ProfilePage.this);
                    alertDialog.setTitle("Server Response");
                    alertDialog.setMessage(response.message());
                    alertDialog.setPositiveButton("Rerty", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getProfileDetails();
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
                customProgressGetProfile.hideProgress();
            }

            @Override
            public void onFailure(Call<ProfilePageDetails> call, Throwable t) {
                customProgressGetProfile.hideProgress();

                AlertDialog.Builder alertDialog=new AlertDialog.Builder(ProfilePage.this);
                alertDialog.setTitle("Server Response");
                alertDialog.setMessage(t.getMessage());
                alertDialog.setPositiveButton("Rerty", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getProfileDetails();
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

    private void setDataToView(Profile profile) {
        editTextName.setText(profile.getFullName());
        editTextSolustion.setText(profile.getSolutation());
        editTextAddress.setText(profile.getAddress());
        editTextPIN.setText(profile.getPincode());
        editTextCity.setText(profile.getCity());
        editTextState.setText(profile.getState());
        editTextCountry.setText(profile.getCountry());
        editTextPANNum.setText(profile.getPancard());
        editTextNumber.setText(profile.getMobile());
        editTextEmail.setText(profile.getEmail());
        pageDiabled();
    }

    private void pageDiabled() {
        editTextName.setEnabled(false);
        editTextSolustion.setEnabled(false);
        editTextAddress.setEnabled(false);
                editTextPIN.setEnabled(false);
        editTextCity.setEnabled(false);
                editTextState.setEnabled(false);
        editTextCountry.setEnabled(false);
                editTextPANNum.setEnabled(false);
        editTextNumber.setEnabled(false);
                editTextEmail.setEnabled(false);
        button.setVisibility(View.GONE);
        edit_text.setVisibility(View.VISIBLE);
    }

    private void enabledPage() {
        edit_text.setVisibility(View.GONE);
        editTextName.setEnabled(true);
        editTextSolustion.setEnabled(true);
        editTextAddress.setEnabled(true);
        editTextPIN.setEnabled(true);
        editTextCity.setEnabled(true);
        editTextState.setEnabled(true);
        editTextCountry.setEnabled(true);
        editTextPANNum.setEnabled(true);
        editTextNumber.setEnabled(true);
        button.setVisibility(View.VISIBLE);
        editTextEmail.setEnabled(true);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Log.d("TAG", "onActivityResult: "+currentPhotoPath);

                    Uri capturedUri = Uri.fromFile(new File(currentPhotoPath));
                    startCropActivity(capturedUri);
                    break;
                case REQUEST_PICK_IMAGE:
                    Uri capturedUri1 = data.getData();
                    startCropActivity(capturedUri1);
                    break;
                case  Crop.REQUEST_CROP:

                    Glide.with(this)
                            .load(destinationUri)
                            .into(imgProfile);
                    break;
                
            }
        }
    }

    private void startCropActivity(Uri capturedUri) {
        // Create a destination file for the cropped image
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //File destinationFile = new File(getCacheDir(), "cropped_image.jpg");
         destinationUri = Uri.fromFile(image);

        // Check if the destination directory exists and create it if necessary
        File destinationDirectory = new File(destinationUri.getPath()).getParentFile();
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdirs();
        }
      //  Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(capturedUri, destinationUri).asSquare().start(this);
    }

    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
        boolean allPermissionsGranted = true;
        for (Boolean isGranted : permissions.values()) {
            if (!isGranted) {
                allPermissionsGranted = false;
                break;
            }
        }
        if (allPermissionsGranted) {
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Image");
            builder.setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            dispatchTakePictureIntent();
                            break;
                        case 1:
                            openGallery();
                            break;
                    }
                }
            });
            builder.show();
        } else {
            // If any permission is not granted, close the app
            showPermissionAlertDialog();
        }
    });
    private boolean checkPermissions() {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    private void showPermissionAlertDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("This app requires all permissions to function properly. Please grant all permissions to continue.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close the app
                        finish();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }
    private void requestPermissions() {
        requestPermissionLauncher.launch(PERMISSIONS);
    }
}