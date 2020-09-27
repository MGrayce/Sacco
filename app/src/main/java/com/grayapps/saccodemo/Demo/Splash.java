package com.grayapps.saccodemo.Demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.grayapps.saccodemo.Demo.Main.Home;
import com.grayapps.saccodemo.Demo.Registration.Login;
import com.grayapps.saccodemo.R;

public class Splash extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    private Context context;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        context = this;
        sharedPreferences = context.getSharedPreferences("preferences",0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferences.getString("verified","").equals("yes")){
                    finish();
                    context.startActivity(new Intent(context, Home.class));
                }
                else {
                    finish();
                    context.startActivity(new Intent(context, Login.class));
                }
            }
        }, SPLASH_SCREEN_TIME_OUT);

    }
}
