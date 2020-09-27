package com.grayapps.saccodemo.Demo.Main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.grayapps.saccodemo.Demo.Adapters.ContibutionsAdapter;
import com.grayapps.saccodemo.Demo.Adapters.HistoryAdapter;
import com.grayapps.saccodemo.Demo.Utils.DatabaseClient;
import com.grayapps.saccodemo.Demo.Utils.Entities;
import com.grayapps.saccodemo.R;

import java.util.ArrayList;
import java.util.List;

public class Savings extends AppCompatActivity {
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private Context context;
    private RecyclerView recyclerView, savingsRec;
    private TextView totalSavings, totalDeposits;
    private String getUserName, getSaccoName, getImageUrl, getUserRole;
    private ArrayList<Integer> amount = new ArrayList<Integer>();
    private LinearLayout svLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings);
        context = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = context.getSharedPreferences("preferences", 0);

        getUserName = sharedPreferences.getString("userName", "");
        getSaccoName = sharedPreferences.getString("userSacco", "");
        getImageUrl = sharedPreferences.getString("saccoImage", "");
        getUserRole = sharedPreferences.getString("userRole", "");

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
        svLayout = findViewById(R.id.svLayout);

        if(getUserRole.equals("Member")){
            svLayout.setVisibility(View.GONE);
        }
        else {
            svLayout.setVisibility(View.VISIBLE);
        }



    }

    private void getHistory() {
        class GetHistory extends AsyncTask<Void, Void, List<Entities>> {

            @Override
            protected List<Entities> doInBackground(Void... voids) {
//
//                a = String.valueOf(DatabaseClient.getInstance(context).getAppDatabase()
//                        .entitiesDao().loadDeposits("Deposits"));

                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child(getSaccoName+"_Transactions");
                databaseReference3.keepSynced(true);
                final Query query1=databaseReference3.orderByChild("transType").equalTo("Deposit");
                query1.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        amount.add(Integer.valueOf(dataSnapshot.child("transAmount").getValue(String.class)));

                        int sum = 0;
                        for(int i = 0; i < amount.size(); i++)
                            sum += amount.get(i);
                        totalSavings.setText(""+sum);
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

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(getSaccoName+"_Transactions");
                databaseReference.keepSynced(true);
                final Query query=databaseReference.orderByChild("transType").equalTo("Deposit");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int getDepoCount = (int) dataSnapshot.getChildrenCount();
                        totalDeposits.setText(""+getDepoCount);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


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
