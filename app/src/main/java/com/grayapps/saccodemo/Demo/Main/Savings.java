package com.grayapps.saccodemo.Demo.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.grayapps.saccodemo.Demo.Adapters.ContibutionsAdapter;
import com.grayapps.saccodemo.Demo.Adapters.HistoryAdapter;
import com.grayapps.saccodemo.Demo.Utils.DatabaseClient;
import com.grayapps.saccodemo.Demo.Utils.Entities;
import com.grayapps.saccodemo.R;

import java.util.List;

public class Savings extends AppCompatActivity {
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private Context context;
    private RecyclerView recyclerView, savingsRec;
    private TextView totalSavings, totalDeposits;
    private String a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings);
        context = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        savingsRec = findViewById(R.id.savingsRecycler);
        savingsRec.setLayoutManager(new LinearLayoutManager(this));
        getHistory();

        totalDeposits = findViewById(R.id.saccoDeposits);
        totalSavings = findViewById(R.id.saccoSavings);

//        LiveData<Integer> entitiesList = DatabaseClient
//                .getInstance(context)
//                .getAppDatabase()
//                .entitiesDao()
//                .getDeposits();
//         totalSavings.setText(""+entitiesList);

        Toast.makeText(context, ""+a, Toast.LENGTH_SHORT).show();

    }
    private void getHistory() {
        class GetHistory extends AsyncTask<Void, Void, List<Entities>> {

            @Override
            protected List<Entities> doInBackground(Void... voids) {

                a = String.valueOf(DatabaseClient.getInstance(context).getAppDatabase()
                        .entitiesDao().loadDeposits("Deposits"));


                List<Entities> entitiesList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .entitiesDao()
                        .getAll();
                return entitiesList;
            }

            @Override
            protected void onPostExecute(List<Entities> entities) {
                super.onPostExecute(entities);
                HistoryAdapter adapter = new HistoryAdapter(Savings.this, entities);
                savingsRec.setAdapter(adapter);
            }
        }

        GetHistory gh = new GetHistory();
        gh.execute();
    }

    private void back() {
        Savings.super.onBackPressed();
    }
}
