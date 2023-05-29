package com.example.rushbinproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {
    TextView poin_txt, pickup_more;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        poin_txt = view.findViewById(R.id.poin);
        pickup_more = view.findViewById(R.id.txt_pickup_more);

        pickup_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TukarPoinActivity.class);
                startActivity(intent);
            }
        });

        Poin();

        return view;
    }

    private void Poin() {
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        String prefNama = user.getUsername();
        System.out.println(prefNama);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.URL_GET_POIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {
                                JSONObject userJson = obj.getJSONObject("user");

                                String totalPoin = userJson.getString("point");

                                poin_txt.setText(totalPoin + " Poin");
                            } else {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", prefNama);
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}

