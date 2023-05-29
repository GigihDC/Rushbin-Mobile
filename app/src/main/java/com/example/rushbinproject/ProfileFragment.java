package com.example.rushbinproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    TextView nama_txt, email_txt, telepon_txt, alamat1_txt, alamat2_txt, alamat3_txt;
    Button edit_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nama_txt = (TextView) view.findViewById(R.id.txt_nama);
        email_txt = (TextView) view.findViewById(R.id.txt_email);
        telepon_txt = (TextView) view.findViewById(R.id.txt_telepon);
        alamat1_txt = (TextView) view.findViewById(R.id.txt_alamat_1);
        alamat2_txt = (TextView) view.findViewById(R.id.txt_alamat_2);
        alamat3_txt = (TextView) view.findViewById(R.id.txt_alamat_3);
        edit_btn = (Button) view.findViewById(R.id.btn_edit);

        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        nama_txt.setText(String.valueOf(user.getNama()));
        email_txt.setText(String.valueOf(user.getUsername()));

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        DataDiri();
        return view;
    }

    private void DataDiri() {
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        String prefUsername = user.getUsername();
        System.out.println(prefUsername);

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

                                telepon_txt.setText(hp);
                                alamat1_txt.setText(rumah1);
                                alamat2_txt.setText(rumah2);
                                alamat3_txt.setText(rumah3);
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
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", prefUsername);
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

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
