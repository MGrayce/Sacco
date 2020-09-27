package com.grayapps.saccodemo.Demo.Registration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.grayapps.saccodemo.Demo.Utils.ShowDialog;
import com.grayapps.saccodemo.R;

public class Login extends AppCompatActivity {
    private ShowDialog showDialog;
    private LinearLayout noAccount;
    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String getPhone;
    private Button login;
    private TextInputEditText phone, ugCode;
    private Toolbar toolbar;
    private String mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        sharedPreferences = context.getSharedPreferences("preferences",0);
        editor = sharedPreferences.edit();
        getPhone = sharedPreferences.getString("userPhone","");
            setContentView(R.layout.activity_login);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        whiteNotificationBar(toolbar);
            noAccount = findViewById(R.id.noAccount);
            phone = findViewById(R.id.userPhone);
            ugCode = findViewById(R.id.ugCode);

            showDialog = new ShowDialog(Login.this);

            login = findViewById(R.id.bt_login);
            login.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    mobile = ugCode.getText().toString() + "" + phone.getText().toString().trim();

                    if (mobile.length() > 13 || mobile.length() < 12) {
                        phone.setError("Enter a valid mobile");
                        phone.requestFocus();
                        return;
                    } else {
                        login.setEnabled(false);
                        login.setText(getString(R.string.pleasewait));
                        login.setTextColor(getColor(R.color.colorAccent));
                        checkProfile(mobile);
                    }
                }
            });

            noAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getPhone.equals(""))
                        loadClass(Login.this, new PhoneRegister(), "Admin", "");
                    else
                        loadClass(Login.this, new Roles(), "Admin", getPhone);

                }
            });

    }

    public void signUp(View view) {

    }
    private void loadClass(Context context, Activity page, String extra, String phone){
        context.startActivity(new Intent(context, page.getClass())
                .putExtra("phone",phone)
                .putExtra("role",extra));
    }
    public void checkProfile(final String phone){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Sacco_Accounts");
        databaseReference.keepSynced(true);
        final Query query=databaseReference.orderByChild("userPhone").equalTo(mobile);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int getCount = (int) dataSnapshot.getChildrenCount();
                if(getCount >0){
                    query.addChildEventListener(new ChildEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            if(dataSnapshot.exists()) {
                                loadDefaults();
                                final String getUserRole = dataSnapshot.child("userRole").getValue(String.class);
                                final String getSacco = dataSnapshot.child("userSacco").getValue(String.class);
                                final String getUserName = dataSnapshot.child("userName").getValue(String.class);
                                final String getUserPhone = dataSnapshot.child("userPhone").getValue(String.class);
                                final String getSaccoImage = dataSnapshot.child("saccoImage").getValue(String.class);
                                Toast.makeText(Login.this, "" + getUserPhone, Toast.LENGTH_LONG).show();
                                showDialog(getSacco, getSaccoImage, getUserRole, getUserPhone, getUserName);
                            }

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    final String getUserPhone = dataSnapshot.child("userPhone").getValue(String.class);
                    loadDefaults();
                    showErrorDialog(mobile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void loadDefaults() {
        login.setEnabled(true);
        login.setText(getString(R.string.common_signin_button_text));
        login.setTextColor(getColor(R.color.white));
    }

    private void showErrorDialog(String getUserPhone) {
        final AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
        alertDialog.setTitle(getString(R.string.saccoLogin));
        alertDialog.setMessage(getUserPhone+" "+getString(R.string.notregistered));
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void showDialog(final String getSacco, final String getSaccoImage, final String getUserRole, final String getUserPhone, final String getUserName) {
        final AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
        alertDialog.setTitle(getString(R.string.saccoLogin));
        alertDialog.setMessage(getUserPhone+" is registered as "+getUserRole+" in "+getSacco);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recreate();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Continue",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userSacco", getSacco);
                        editor.putString("userName", getUserName);
                        editor.putString("saccoImage", getSaccoImage);
                        editor.putString("userPhone", getUserPhone);
                        editor.putString("userRole", getUserRole);
                        editor.putString("verified", "yes");
                        editor.apply();
                        alertDialog.dismiss();
                        Toast.makeText(context, "Verifying "+phone, Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, VerifyPhone.class)
                                .putExtra("role","Verify")
                                .putExtra("mobile",getUserPhone));
                    }
                });
        alertDialog.show();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }
}
