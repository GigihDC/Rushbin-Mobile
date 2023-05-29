package com.example.rushbinproject;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PickUpFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST_1 = 1;
    private static final int PICK_IMAGE_REQUEST_2 = 2;
    private static final int PICK_IMAGE_REQUEST_3 = 3;
    private ImageView add_1, add_2, add_3;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private EditText deskripsi;
    private Button pickup_send;
    private String sGambar1, sGambar2, sGambar3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pickup, container, false);
        spinner = view.findViewById(R.id.alamat_spinner);

        add_1 = view.findViewById(R.id.pickup_add_1);
        add_2 = view.findViewById(R.id.pickup_add_2);
        add_3 = view.findViewById(R.id.pickup_add_3);
        deskripsi = view.findViewById(R.id.pickup_deskripsi);
        pickup_send = view.findViewById(R.id.pickup_send_button);

        add_1.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture 1"), PICK_IMAGE_REQUEST_1);
        });

        add_2.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture 2"), PICK_IMAGE_REQUEST_2);
        });

        add_3.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture 3"), PICK_IMAGE_REQUEST_3);
        });

        pickup_send.setOnClickListener(v -> sendPickup());

        DataDiri();
        return view;
    }

    private void DataDiri() {
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        String prefUsername = user.getUsername();
        System.out.println(prefUsername);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.URL_GET_PROFILE,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.getBoolean("error")) {
                            JSONObject userJson = obj.getJSONObject("user");

                            String rumah1 = userJson.getString("alamat1");
                            String rumah2 = userJson.getString("alamat2");
                            String rumah3 = userJson.getString("alamat3");

                            List<String> spinnerItemsList = new ArrayList<>();
                            spinnerItemsList.add(rumah1);
                            spinnerItemsList.add(rumah2);
                            spinnerItemsList.add(rumah3);

                            adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerItemsList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                        } else {
                            Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show()) {
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

    private void sendPickup() {
        Drawable drawable1 = add_1.getDrawable();
        Drawable drawable2 = add_2.getDrawable();
        Drawable drawable3 = add_3.getDrawable();

        Bitmap bitmap1 = drawableToBitmap(drawable1);
        Bitmap bitmap2 = drawableToBitmap(drawable2);
        Bitmap bitmap3 = drawableToBitmap(drawable3);

        if (bitmap1 != null) {
            sGambar1 = convertBitmapToBase64(bitmap1);
        }

        if (bitmap2 != null) {
            sGambar2 = convertBitmapToBase64(bitmap2);
        }

        if (bitmap3 != null) {
            sGambar3 = convertBitmapToBase64(bitmap3);
        }

        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        String prefNama = user.getUsername();

        String sAlamat = spinner.getSelectedItem().toString().trim();
        String sDeskripsi = deskripsi.getText().toString().trim();

        if (TextUtils.isEmpty(sGambar1)){
            Toast.makeText(getActivity(), "Masukkan Foto Sampah 1 yang akan diangkut", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(sGambar2)){
            Toast.makeText(getActivity(), "Masukkan Foto Sampah 2 yang akan diangkut", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(sGambar3)){
            Toast.makeText(getActivity(), "Masukkan Foto Sampah 3 yang akan diangkut", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(sAlamat)){
            Toast.makeText(getActivity(), "Masukkan alamat yang dituju", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(sDeskripsi)) {
            Toast.makeText(getActivity(), "Masukkan Deskripsi Sampah yang akan diangkut", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.URL_PICKUP,
                response -> {
                    if (response.startsWith("<br")) {
                        response = response.substring(3);
                    }

                    response = response.trim();
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.getBoolean("error")) {
                            Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            JSONObject userJson = obj.getJSONObject("user");
                            // Do something with the server response if needed
                        } else {
                            Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", prefNama);
                params.put("gambar1", sGambar1);
                params.put("gambar2", sGambar2);
                params.put("gambar3", sGambar3);
                params.put("alamat", sAlamat);
                params.put("tanggal", currentDate);
                params.put("deskripsi", sDeskripsi);

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable || drawable instanceof androidx.vectordrawable.graphics.drawable.VectorDrawableCompat) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
        return null;
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);

                switch (requestCode) {
                    case PICK_IMAGE_REQUEST_1:
                        add_1.setImageBitmap(bitmap);
                        break;
                    case PICK_IMAGE_REQUEST_2:
                        add_2.setImageBitmap(bitmap);
                        break;
                    case PICK_IMAGE_REQUEST_3:
                        add_3.setImageBitmap(bitmap);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
