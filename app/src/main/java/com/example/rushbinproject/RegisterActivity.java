package com.example.rushbinproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText username, password, nama, confirmPassword;
    private Button btn_daftar;
    private TextView txt_login;
    private ImageView btn_kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_kembali = findViewById(R.id.r_tombolKembali);
        nama = findViewById(R.id.register_nama);
        username = findViewById(R.id.register_email);
        password = findViewById(R.id.register_pass);
        confirmPassword = findViewById(R.id.register_pass_2);
        btn_daftar = findViewById(R.id.r_btnReg);
        txt_login = findViewById(R.id.r_tombolLogin);

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        btn_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser() {
        final String sNama = nama.getText().toString().trim();
        final String sUsername = username.getText().toString().trim();
        final String sPassword = password.getText().toString().trim();
        final String sConfirmPassword = confirmPassword.getText().toString().trim();

        //valdiasi form
        if(TextUtils.isEmpty(sNama)){
            nama.setError("Nama Harus Di isi");
            nama.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(sUsername)) {
            username.setError("Please enter username");
            username.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(sPassword)) {
            password.setError("Enter a password");
            password.requestFocus();
            return;
        }

        if(!TextUtils.equals(sPassword, sConfirmPassword)){
            confirmPassword.setError("Pastikan Password sama");
            confirmPassword.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                String namaLengkap = obj.getString("nama_lengkap");
                                String username = obj.getString("username");

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_lengkap", sNama);
                params.put("username", sUsername);
                params.put("password", sPassword );
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}