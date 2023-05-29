package com.example.rushbinproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button login;
    TextView btn_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        login = (Button) findViewById(R.id.loginButton);
        btn_reg = (TextView) findViewById(R.id.btn_reg);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

    }

    private void userLogin() {
        final String sUsername = username.getText().toString();
        final String sPassword = password.getText().toString();
        User user = SharedPrefManager.getInstance(LoginActivity.this).getUser();
        String prefNama = user.getUsername();
        System.out.println(prefNama);


        //validasi form
        if (TextUtils.isEmpty(sUsername)) {
            username.setError("Tolong Masukkan Username Kamu");
            username.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(sPassword)) {
            password.setError("Tolong Masukkan Password Kamu");
            password.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                JSONObject userJson = obj.getJSONObject("user");

                                User user = new User(
                                        userJson.getString("username"),
                                        userJson.getString("nama_lengkap"),
                                        userJson.getString("id_pengguna"),
                                        userJson.getInt("point")
                                        );

                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                finish();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", sUsername);
                params.put("password", sPassword);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

}