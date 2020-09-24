package com.grayapps.saccodemo.Demo.Registration;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.grayapps.saccodemo.Demo.Home.Home;
import com.grayapps.saccodemo.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DateFormat;
import java.util.Calendar;

public class Roles extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String getPhone, getRole;
    private Context context;
    Intent intent;
    private TextInputEditText saccoName, adminName, phone;
    private Button signup;
    private String getSaccoName, getAdminName;
    Uri imageUri=null;
    public  static  final  int RC_SETTINGS =101;
    private static final int PICK_IMAGE = 100;
    private static final int MY_CAMERA_PERMISSION_CODE = 102;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private int progress = 0;
    private TextView progessTxt, title;
    private ProgressBar progressBar;
    private LinearLayout upload;
    private ImageView saccoImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roles);
        context = this;
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Sacco_Accounts");
        sharedPreferences = context.getSharedPreferences("preferences",0);
        editor = sharedPreferences.edit();

        getPhone = sharedPreferences.getString("userPhone","");

        intent = getIntent();
        getRole = intent.getStringExtra("role");

        saccoName = findViewById(R.id.saccoName);
        adminName = findViewById(R.id.fName);
        phone = findViewById(R.id.userPhone);

        String userPhone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        phone.setText(userPhone);
        phone.setEnabled(false);

        saccoImage = findViewById(R.id.saccoImage);
        upload = findViewById(R.id.uploadPik);

        upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                getProfileImage();
            }
        });
        signup = findViewById(R.id.bt_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSaccoName = saccoName.getText().toString().trim();
                getAdminName = adminName.getText().toString().trim();
                getPhone = phone.getText().toString().trim();
                if(getSaccoName.equals("") || getAdminName.equals("")){
                    Snackbar.make(v, getString(R.string.missing), Snackbar.LENGTH_SHORT).show();
                }
                else if(imageUri == null){
                    Snackbar.make(v, getString(R.string.upload), Snackbar.LENGTH_SHORT).show();
                }
                else {
                    try {
                        final StorageReference filepath = storageReference.child("SaccoImages").child("" + imageUri.getLastPathSegment());
                        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final DatabaseReference db = databaseReference.push();
                                        Calendar calendar = Calendar.getInstance();
                                        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                                        String userId = firebaseAuth.getCurrentUser().getUid();
                                        String userPhone = firebaseAuth.getCurrentUser().getPhoneNumber();
                                        db.child("CreatedOn").setValue(currentDate);
                                        db.child("userSacco").setValue(getSaccoName);
                                        db.child("userName").setValue(getAdminName);
                                        db.child("saccoImage").setValue(String.valueOf(imageUri));
                                        db.child("userPhone").setValue(userPhone);
                                        db.child("userRole").setValue(getRole);

                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("userSacco", getSaccoName);
                                        editor.putString("userName", getAdminName);
                                        editor.putString("saccoImage", String.valueOf(imageUri));
                                        editor.putString("userPhone", userPhone);
                                        editor.putString("userRole", getRole);
                                        editor.putString("verified", "yes");
                                        editor.apply();
                                        finish();
                                        context.startActivity(new Intent(context, Home.class));

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Roles.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                int progress = (int) (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                showLoading(progress, getString(R.string.settingup));
                            }
                        });

                    } catch (Exception e) {
                        Toast.makeText(Roles.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private final void getProfileImage() {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(Roles.this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK ) {
                Uri resultUri = result.getUri();
                saccoImage.setImageURI(resultUri);
                imageUri = resultUri;
                upload.setVisibility(View.GONE);
            }
        } else if (requestCode == RC_SETTINGS) {
            if (data.getData().toString().equals("yes")) {
                String sent = "yes";
                data.setData(Uri.parse(sent));
                setResult(RESULT_OK, data);
                finish();
            }
        }
        else{

        }

    }
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    public void showLoading(int progress, String tit){
        builder = new AlertDialog.Builder(context,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(signup.getContext()).inflate(R.layout.progress_dialog, viewGroup, false);

        progressBar = dialogView.findViewById(R.id.progress_bar);
        title = dialogView.findViewById(R.id.title_template);
        progessTxt = dialogView.findViewById(R.id.text_view_progress);
        builder.setView(dialogView);
        builder.setView(dialogView);
        builder.setView(dialogView);

        title.setText(tit);
        progressBar.setProgress(progress);
        progessTxt.setText(progress+" %");
        if(progress == 100){
            finish();
            startActivity(new Intent(context, Login.class));
        }

        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }
}
