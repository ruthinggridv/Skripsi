package com.example.gungde.reminder_medicine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gungde.reminder_medicine.model.Base;
import com.example.gungde.reminder_medicine.network.GetDataService;
import com.example.gungde.reminder_medicine.network.RetrofitClientInstance;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Daftar extends AppCompatActivity {

    ProgressDialog progressDoalog;
    @BindView(R.id.my_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.btnMasuk)
    Button btnMasuk;
    @BindView(R.id.txtUsername)
    EditText txtUsername;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.txtPhone)
    EditText txtPhone;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.btnDaftar)
    Button btnDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDoalog = new ProgressDialog(Daftar.this);
        progressDoalog.setMessage("Loading....");
        setSpinnerResources();
    }

    private void setSpinnerResources() {
        List<String> kategori = Arrays.asList(getResources().getStringArray(R.array.kategori));
        ArrayAdapter<String> adpKategori = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, kategori);
        adpKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adpKategori);
    }

    @OnClick(R.id.btnDaftar)
    public void register() {
        String username = txtUsername.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String nohp = txtPhone.getText().toString().trim();
        final String kategori = spinner.getSelectedItem().toString();
        String password = txtPassword.getText().toString().trim();

        if (username.equals("") || email.equals("") || nohp.equals("") || kategori.equals("- Pilih Kategori -") || password.equals("")) {
            Toast.makeText(this, "Pastikan semua data terisi!", Toast.LENGTH_SHORT).show();
        } else {
        GetDataService api = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Base> call = api.postData(username, email, nohp, kategori, password);
        progressDoalog.show();
        call.enqueue(new Callback<Base>() {
            @Override
            public void onResponse(Call<Base> call, Response<Base> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Daftar.this, "Berhasil", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Daftar.this, Login.class));
                    finish();
                } else {
                    Toast.makeText(Daftar.this, "Gagal", Toast.LENGTH_SHORT).show();
                }
                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call<Base> call, Throwable t) {
                progressDoalog.dismiss();
                Log.e("ERROR", t.toString());
            }
        });
    }}

    @OnClick(R.id.btnMasuk)
    public void login() {
        startActivity(new Intent(Daftar.this, Login.class));
    }
}
