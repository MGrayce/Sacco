package com.grayapps.saccodemo.Demo.Main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grayapps.saccodemo.Demo.Splash;
import com.grayapps.saccodemo.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private TextView userName, saccoName, totalSavings;
    private ImageView saccoImage;
    private String getUserName, getSaccoName, getImageUrl, getUserRole;
    private LinearLayout svLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = context.getSharedPreferences("preferences", 0);
        editor = sharedPreferences.edit();

        getUserName = sharedPreferences.getString("userName", "");
        getSaccoName = sharedPreferences.getString("userSacco", "");
        getImageUrl = sharedPreferences.getString("saccoImage", "");
        getUserRole = sharedPreferences.getString("userRole", "");

        if(getUserRole.equals("Member")){
            setContentView(R.layout.activity_home_member);
            userName = findViewById(R.id.userName);
            saccoName = findViewById(R.id.saccoName);
            totalSavings = findViewById(R.id.saccoSavings);
            saccoImage = findViewById(R.id.saccoImage);
            svLayout = findViewById(R.id.svLayout);

            userName.setText(getString(R.string.hellothere)+""+getUserName);
            saccoName.setText(""+getSaccoName);
            svLayout.setVisibility(View.GONE);

        }
        else {
            setContentView(R.layout.activity_home_admin);
            userName = findViewById(R.id.userName);
            saccoName = findViewById(R.id.saccoName);
            totalSavings = findViewById(R.id.saccoSavings);
            saccoImage = findViewById(R.id.saccoImage);
            svLayout = findViewById(R.id.svLayout);

            userName.setText(getString(R.string.hellothere)+""+getUserName);
            saccoName.setText(""+getSaccoName);
            svLayout.setVisibility(View.VISIBLE);
        }

    }

    public void savings(View view) {
        context.startActivity(new Intent(context, Savings.class));
    }

    public void group(View view) {
        context.startActivity(new Intent(context, Contributions.class));
    }

    public void logout(View view) {
        final AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
        alertDialog.setTitle(getString(R.string.saccoLogout));
        alertDialog.setMessage(getString(R.string.confirmLogout));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Log Out",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putString("verified","no").apply();
                        finish();
                        startActivity(new Intent(context, Splash.class));
                    }
                });
        alertDialog.show();

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
