package com.example.rushbinproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    TextView btn_kembali;
    EditText edt_nama, edt_email, edt_telepon, edt_alamat1, edt_alamat2, edt_alamat3;
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btn_kembali = findViewById(R.id.txt_back_profile);
        edt_email = findViewById(R.id.edit_email);
        edt_nama = findViewById(R.id.edit_nama);
        edt_telepon = findViewById(R.id.edit_tlp);
        edt_alamat1 = findViewById(R.id.edit_alamat_1);
        edt_alamat2 = findViewById(R.id.edit_alamat_2);
        edt_alamat3 = findViewById(R.id.edit_alamat_3);
        btn_save = findViewById(R.id.btn_edit_save);

        btn_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        User user = SharedPrefManager.getInstance(this).getUser();

        EditText editText = findViewById(R.id.edit_email);
        editText.setKeyListener(null);

        edt_email.setText(user.getUsername());
        edt_nama.setText(user.getNama());

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileEdit();
            }
        });

        DataDiri();
    }

    private void profileEdit() {
        final String sUsername = edt_email.getText().toString().trim();
        final String sNama = edt_nama.getText().toString().trim();
        final String sTelepon = edt_telepon.getText().toString().trim();
        final String sAlamat1 = edt_alamat1.getText().toString().trim();
        final String sAlamat2 = edt_alamat2.getText().toString().trim();
        final String sAlamat3 = edt_alamat3.getText().toString().trim();

        // Buat objek JSON
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", sUsername);
            jsonBody.put("nama_lengkap", sNama);
            jsonBody.put("telepon", sTelepon);
            jsonBody.put("alamat1", sAlamat1);
            jsonBody.put("alamat2", sAlamat2);
            jsonBody.put("alamat3", sAlamat3);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, DbContract.URL_UPDATE_PROFILE, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.getBoolean("error")) {
                                Toast.makeText(EditProfileActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(EditProfileActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
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
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void DataDiri() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String prefNama = user.getUsername();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.URL_GET_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {
                                JSONObject userJson = obj.getJSONObject("user");

                                String hp = userJson.getString("telepon");
                                String rumah1 = userJson.getString("alamat1");
                                String rumah2 = userJson.getString("alamat2");
                                String rumah3 = userJson.getString("alamat3");

                                edt_telepon.setText(hp);
                                edt_alamat1.setText(rumah1);
                                edt_alamat2.setText(rumah2);
                                edt_alamat3.setText(rumah3);
                            } else {
                                Toast.makeText(EditProfileActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", prefNama);
                return encodeParameters(params, getParamsEncoding());
            }

            private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
                StringBuilder encodedParams = new StringBuilder();
                try {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                        encodedParams.append('=');
                        encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                        encodedParams.append('&');
                    }
                    return encodedParams.toString().getBytes(paramsEncoding);
                } catch (UnsupportedEncodingException uee) {
                    throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
                }
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}
