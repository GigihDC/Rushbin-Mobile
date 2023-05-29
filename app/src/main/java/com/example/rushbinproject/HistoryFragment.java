package com.example.rushbinproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HistoryFragment extends Fragment {
    CardView pengantaran, penukaran, pendapatan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        pengantaran = view.findViewById(R.id.card_pengantaran);
        penukaran = view.findViewById(R.id.card_penukaran);
        pendapatan = view.findViewById(R.id.card_pendapatan);

        pengantaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PengantaranHistoryActivity.class);
                startActivity(intent);
            }
        });

        penukaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PenukaranHistoryActivity.class);
                startActivity(intent);
            }
        });

        pendapatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PendapatanHistoryActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
