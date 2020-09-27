package com.grayapps.saccodemo.Demo.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.grayapps.saccodemo.Demo.Registration.Login;
import com.grayapps.saccodemo.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private Context context;
    private TextView userName, saccoName, totalSavings;
    private ImageView saccoImage;
    private String getUserName, getSaccoName, getImageUrl, getUserRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = context.getSharedPreferences("preferences", 0);

        getUserName = sharedPreferences.getString("userName", "");
        getSaccoName = sharedPreferences.getString("userSacco", "");
        getImageUrl = sharedPreferences.getString("saccoImage", "");
        getUserRole = sharedPreferences.getString("userRole", "");

        userName = findViewById(R.id.userName);
        saccoName = findViewById(R.id.saccoName);
        totalSavings = findViewById(R.id.saccoSavings);
        saccoImage = findViewById(R.id.saccoImage);

        userName.setText(getString(R.string.hellothere)+""+getUserName);
        saccoName.setText(""+getSaccoName);

    }

    public void savings(View view) {
        context.startActivity(new Intent(context, Savings.class));
    }

    public void group(View view) {
        context.startActivity(new Intent(context, Contributions.class));
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            loadData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
    private void loadData(){

        try{
            Picasso.with(context).load(""+getImageUrl).fetch(new Callback() {
                @Override
                public void onSuccess() {
                    Picasso.with(context).load(""+getImageUrl).into(saccoImage);
                    saccoImage.animate().setDuration(0).alpha(1f).start();
                }
                @Override
                public void onError() {
                    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        new AsyncCaller().execute();
    }
}
