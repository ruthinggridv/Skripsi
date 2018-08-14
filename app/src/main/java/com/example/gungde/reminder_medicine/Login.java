package com.example.gungde.reminder_medicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gungde.reminder_medicine.model.LoginModel;
import com.example.gungde.reminder_medicine.network.GetDataService;
import com.example.gungde.reminder_medicine.network.RetrofitClientInstance;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    ProgressDialog progressDoalog;
    @BindView(R.id.my_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.txtUsername)
    EditText txtUsername;
    @BindView(R.id.txtPassword)
    EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDoalog = new ProgressDialog(Login.this);
        progressDoalog.setMessage("Loading....");

    }

    @OnClick(R.id.btnLogin)
    public void login() {
        String username = txtUsername.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if (username.equals("") || password.equals("")) {
            Toast.makeText(this, "Pastikan semua data terisi!", Toast.LENGTH_SHORT).show();
        } else {
            GetDataService api = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<LoginModel> call = api.LoginUser(username, password);
            progressDoalog.show();
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    progressDoalog.dismiss();
                    LoginModel resp = response.body();
                    if (resp.getStatus().equals("true")) {
                        progressDoalog.dismiss();
                        SharedPreferences pref = getSharedPreferences("medical", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("isLogin", true);
                        editor.putString("id_user", resp.getData().getId_user());
                        editor.putString("username", resp.getData().getUsername());
                        editor.putString("email", resp.getData().getEmail());
                        editor.putString("nohp", resp.getData().getNohp());
                        editor.putString("kategori", resp.getData().getKategori());
                        editor.putString("password", resp.getData().getPassword());
                        editor.apply();
                        startActivity(new Intent(Login.this, Beranda.class));
                        finish();
                    } else {
                        Toast.makeText(Login.this, "username atau password salah!", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    progressDoalog.dismiss();
                    Log.e("ERROR", t.toString());
                }
            });
        }
    }

    @OnClick(R.id.btnRegister)
    public void register() {
        startActivity(new Intent(Login.this, Daftar.class));
    }
}
