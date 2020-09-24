package com.grayapps.saccodemo.Demo.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.grayapps.saccodemo.R;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
    }
}
