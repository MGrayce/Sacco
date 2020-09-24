package com.grayapps.saccodemo.Demo.Registration;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.grayapps.saccodemo.R;

public class PhoneRegister extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String getPhone, getRole;
    private Context context;
    Intent intent;
    private Button next;
    private TextInputEditText phone, ugCode;
    private TextView role;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_register);
        context = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        whiteNotificationBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        sharedPreferences = context.getSharedPreferences("preferences",0);
        editor = sharedPreferences.edit();

        getPhone = sharedPreferences.getString("userPhone","");

        intent = getIntent();
        getRole = intent.getStringExtra("role");

//        nextActivity = new ShowDialog(RegisterPhone.this);

        next = findViewById(R.id.bt_next);
        phone = findViewById(R.id.userPhone);
        ugCode = findViewById(R.id.ugCode);
        role = findViewById(R.id.role);

        role.setText(getRole);


        getPhone = intent.getStringExtra("phone");
        if (!getPhone.isEmpty()) {
            phone.setText("" + getPhone);
        }
        else {
            phone.setText("7");
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mobile = ugCode.getText().toString() +""+phone.getText().toString().trim();

                if(mobile.length() > 13 || mobile.length() < 12){
                    phone.setError("Enter a valid mobile");
                    phone.requestFocus();
                    return;
                }
                AlertDialog alertDialog = new AlertDialog.Builder(PhoneRegister.this).create();
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("Is '"+mobile+"' the correct number?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                context.startActivity(new Intent(context, VerifyPhone.class)
                                        .putExtra("role",getRole)
                                        .putExtra("mobile",mobile));
                            }
                        });
                alertDialog.show();
//                nextActivity.viewNextActivity(context, mobile, getString(R.string.verifythisnumber),true);
            }
        });
    }

    private void back() {
        PhoneRegister.super.onBackPressed();
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
