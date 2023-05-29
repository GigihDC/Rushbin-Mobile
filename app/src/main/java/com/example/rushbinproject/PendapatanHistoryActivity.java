package com.example.rushbinproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PendapatanHistoryActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> dataList;
    private ArrayAdapter<String> adapter;
    private ProgressBar progressBar;
    private TextView textProgress;
    private static final int PROGRESS_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendapatan_history);

        listView = findViewById(R.id.list_view);
        dataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.list_data_master, dataList);
        progressBar = findViewById(R.id.progressBar);
        textProgress = findViewById(R.id.textProgress);
        listView.setAdapter(adapter);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000); // Perbarui setiap 1 detik
            }
        };
        handler.postDelayed(runnable, 0); // Mulai perbarui tanggal dan jam

        getData();
    }

    private void getData() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String prefEmail = user.getUsername();

        progressBar.setVisibility(View.VISIBLE);
        textProgress.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, DbContract.URL_GET_DATA_SETOR, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("poin");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String data = "Kode Transaksi: " + jsonObject.getString("kode_transaksi") + "\n" +
                                                "Nama: " + jsonObject.getString("nama_lengkap") + "\n" +
                                                "Tanggal: " + jsonObject.getString("tanggal") + "\n" +
                                                "Waktu: " + jsonObject.getString("waktu") + "\n" +
                                                "Poin yang didapat: " + jsonObject.getString("total_point");
                                        dataList.add(data);
                                    }
                                    progressBar.setVisibility(View.GONE);
                                    textProgress.setVisibility(View.GONE);

                                    adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    Log.e("Error", e.getMessage());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Kode penanganan kesalahan lainnya
                            }
                        }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded";
                    }

                    @Override
                    public byte[] getBody() {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", prefEmail);
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

                VolleySingleton.getInstance(PendapatanHistoryActivity.this).addToRequestQueue(jsonObjectRequest);
            }
        }, PROGRESS_DELAY);
    }
}
