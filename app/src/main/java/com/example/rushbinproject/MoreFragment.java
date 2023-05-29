package com.example.rushbinproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class MoreFragment extends Fragment {
    CardView about, kebijakan, logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        about = (CardView) view.findViewById(R.id.card_about);
        kebijakan = (CardView) view.findViewById(R.id.card_syarat);
        logout = (CardView) view.findViewById(R.id.card_logout);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogout = new Intent(getActivity(), AboutActivity.class);
                startActivity(intentLogout);
            }
        });

        kebijakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogout = new Intent(getActivity(), KebijakanActivity.class);
                startActivity(intentLogout);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Konfirmasi Keluar");
                builder.setMessage("Anda yakin ingin keluar?");

                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentLogout = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intentLogout);
                        getActivity().finish();
                        SharedPrefManager.getInstance(getActivity()).logout();
                    }
                });

                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

        });

        return view;
    }
}
