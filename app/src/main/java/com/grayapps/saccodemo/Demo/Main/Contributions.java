package com.grayapps.saccodemo.Demo.Main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grayapps.saccodemo.Demo.Adapters.ContibutionsAdapter;
import com.grayapps.saccodemo.Demo.Utils.DatabaseClient;
import com.grayapps.saccodemo.Demo.Utils.Entities;
import com.grayapps.saccodemo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Contributions extends AppCompatActivity {
    Date c = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
    String formattedDate = df.format(c);
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    DatabaseReference databaseReference, databaseReference2;
    private String getUserName, getSaccoName, getImageUrl, getUserRole, getUserPhone;
    private FloatingActionButton addMember, depositNow;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributions);
        context = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        sharedPreferences = context.getSharedPreferences("preferences", 0);

        getUserName = sharedPreferences.getString("userName", "");
        getSaccoName = sharedPreferences.getString("userSacco", "");
        getImageUrl = sharedPreferences.getString("saccoImage", "");
        getUserRole = sharedPreferences.getString("userRole", "");
        getUserPhone = sharedPreferences.getString("userPhone", "");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Sacco_Accounts");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Sacco_Deposits");

        addMember = findViewById(R.id.addMember);
        depositNow = findViewById(R.id.deposit);

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMember(v);
            }
        });
        depositNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depositNow(v);
            }
        });

        recyclerView = findViewById(R.id.transactionHisRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getContributions();
    }

    private void depositNow(View view) {
        makeDeposit(view);
    }
    private void getContributions() {
        class GetContributions extends AsyncTask<Void, Void, List<Entities>> {

            @Override
            protected List<Entities> doInBackground(Void... voids) {
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
                ContibutionsAdapter adapter = new ContibutionsAdapter(Contributions.this, entities);
                recyclerView.setAdapter(adapter);
            }
        }

        GetContributions gt = new GetContributions();
        gt.execute();
    }
    public void makeDeposit(View view){
        builder = new AlertDialog.Builder(context,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.make_deposit, viewGroup, false);


        TextView ok =dialogView.findViewById(R.id.ok);
        builder.setView(dialogView);

        final TextInputEditText money =dialogView.findViewById(R.id.money);
        builder.setView(dialogView);

        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                final String mmy = money.getText().toString().trim();
                int amount = Integer.parseInt(mmy);
                if(mmy.isEmpty()){
                    money.setError(getString(R.string.amountReq));
                    money.requestFocus();
                    return;
                }
                if(amount < 2000){
                    money.setError(getString(R.string.cannotDep));
                    money.requestFocus();
                    return;
                }
                class MakeDeposit extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {

                        Entities entities = new Entities();
                        entities.setDate(formattedDate);
                        entities.setAction("Deposit");
                        entities.setMember(getUserName);
                        entities.setAmount(mmy);

                        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                                .entitiesDao()
                                .insert(entities);

                        final DatabaseReference db = databaseReference.push();
                        db.child("CreatedOn").setValue(formattedDate);
                        db.child("userSacco").setValue(getSaccoName);
                        db.child("userName").setValue(getUserName);
                        db.child("saccoImage").setValue(""+getImageUrl);
                        db.child("userPhone").setValue(getUserPhone);
                        db.child("userRole").setValue("Member");

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        Toast.makeText(context, getString(R.string.amountDeposited), Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                }
                MakeDeposit md = new MakeDeposit();
                md.execute();

            }
        });
        alertDialog.show();
    }

    public void addMember(View view){
        builder = new AlertDialog.Builder(context,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.add_member_dialog, viewGroup, false);


        TextView add =dialogView.findViewById(R.id.addMember);
        builder.setView(dialogView);

        final TextInputEditText mName =dialogView.findViewById(R.id.MName);
        builder.setView(dialogView);

        final TextInputEditText ugCode =dialogView.findViewById(R.id.ugCode);
        builder.setView(dialogView);

        final TextInputEditText phone =dialogView.findViewById(R.id.userPhone);
        builder.setView(dialogView);


        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                final String mobile = ugCode.getText().toString() + "" + phone.getText().toString().trim();
                final String name = mName.getText().toString().trim();
                if(name.isEmpty()){
                    mName.setError(getString(R.string.nameReq));
                    mName.requestFocus();
                    return;
                }
                if(phone.getText().toString().trim().isEmpty()){
                    phone.setError(getString(R.string.phoneReq));
                    phone.requestFocus();
                    return;
                }
                class SaveMember extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        final DatabaseReference db = databaseReference.push();
                        db.child("CreatedOn").setValue(formattedDate);
                        db.child("userSacco").setValue(getSaccoName);
                        db.child("userName").setValue(name);
                        db.child("saccoImage").setValue(""+getImageUrl);
                        db.child("userPhone").setValue(mobile);
                        db.child("userRole").setValue("Member");

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        Toast.makeText(context, getString(R.string.memberRegisterd), Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                }
                SaveMember st = new SaveMember();
                st.execute();

            }
        });
        alertDialog.show();
    }

    private void back() {
        Contributions.super.onBackPressed();
    }
}
