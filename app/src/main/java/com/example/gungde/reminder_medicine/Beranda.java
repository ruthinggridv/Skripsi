package com.example.gungde.reminder_medicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gungde.reminder_medicine.adapter.CustomAdapter;
import com.example.gungde.reminder_medicine.model.DataObat;
import com.example.gungde.reminder_medicine.model.InputObat;
import com.example.gungde.reminder_medicine.model.LoginModel;
import com.example.gungde.reminder_medicine.network.GetDataService;
import com.example.gungde.reminder_medicine.network.RetrofitClientInstance;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Beranda extends AppCompatActivity {

    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;
    @BindView(R.id.nama)
    TextView nama;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.prograss)
    ProgressBar prograss;
    @BindView(R.id.listObat)
    RecyclerView listObat;
    @BindView(R.id.addfab)
    FloatingActionButton addfab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDoalog = new ProgressDialog(Beranda.this);
        progressDoalog.setMessage("Loading....");
    }

    @Override
    protected void onStart() {
        super.onStart();
        getList();
    }

    private void getList(){
        progressDoalog.show();

        SharedPreferences pref = getSharedPreferences("medical", Context.MODE_PRIVATE);
        String id_user = pref.getString("id_user", "0");

        /*Create handle for the RetrofitInstance interface*/
        GetDataService api = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Call<DataObat> call = api.getObat(id_user);
        call.enqueue(new Callback<DataObat>() {

            @Override
            public void onResponse(@NonNull Call<DataObat> call, @NonNull Response<DataObat> response) {
                DataObat resp = response.body();
                if (response.body().getData().size() != 0) {
                    generateDataList(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<DataObat> call, Throwable t) {
                progressDoalog.dismiss();
                Log.e("Error", t.toString());
                Toast.makeText(Beranda.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
        progressDoalog.dismiss();
    }

    private void generateDataList(List<DataObat.Data> data) {
        recyclerView = findViewById(R.id.listObat);
        adapter = new CustomAdapter(this, data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Beranda.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.addfab)
    public void tambah() {
        startActivity(new Intent(Beranda.this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

