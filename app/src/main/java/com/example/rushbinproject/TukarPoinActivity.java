package com.example.rushbinproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TukarPoinActivity extends AppCompatActivity {
    TextView btn_kembali, poin_txt;
    Button btn_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukar_poin);

        btn_kembali = findViewById(R.id.txt_back_poin);
        poin_txt = findViewById(R.id.txt_poin);
        btn_request = findViewById(R.id.btn_tukar_poin);

        User user = SharedPrefManager.getInstance(this).getUser();

        btn_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TukarPoinActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPoin = Integer.parseInt(poin_txt.getText().toString());

                if (currentPoin >= 500) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TukarPoinActivity.this);

                    builder.setTitle("Konfirmasi Penukaran Poin");
                    builder.setMessage("Anda yakin ingin menukar 500 poin?");

                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            createPenukaranInDatabase();
                        }
                    });

                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    AlertDialog.Builder kurang = new AlertDialog.Builder(TukarPoinActivity.this);

                    kurang.setMessage("Poin Anda tidak mencukupi untuk melakukan penukaran.");

                    kurang.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = kurang.create();
                    dialog.show();
                }
            }
        });


        TukarPoin();
    }

    private void TukarPoin() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String prefNama = user.getUsername();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.URL_GET_POIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {
                                JSONObject userJson = obj.getJSONObject("user");

                                String totalPoin = userJson.getString("point");
                                poin_txt.setText(totalPoin);

                                user.setPoint(Integer.parseInt(totalPoin));
                                SharedPrefManager.getInstance(TukarPoinActivity.this).saveUser(user);
                            } else {
                                Toast.makeText(TukarPoinActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TukarPoinActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", prefNama);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void updatePoinInDatabase(final int newPoin) {
        User user = SharedPrefManager.getInstance(this).getUser();
        String prefNama = user.getUsername();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.URL_UPDATE_POIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {
                                Toast.makeText(TukarPoinActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(TukarPoinActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TukarPoinActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", prefNama);
                params.put("point", String.valueOf(newPoin));
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void createPenukaranInDatabase() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String prefNama = user.getUsername();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.URL_CREATE_PENUKARAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {
                                Toast.makeText(TukarPoinActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(TukarPoinActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TukarPoinActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", prefNama);
                params.put("tanggal", currentDate);
                params.put("waktu", currentTime);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}
