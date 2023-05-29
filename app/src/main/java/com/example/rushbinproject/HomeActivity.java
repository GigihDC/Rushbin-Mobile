package com.example.rushbinproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rushbinproject.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new DashboardFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.Dashboard:
                    replaceFragment(new DashboardFragment());
                    break;
                case R.id.History:
                    replaceFragment(new HistoryFragment());
                    break;
                case R.id.PickUp:
                    replaceFragment(new PickUpFragment());
                    break;
                case R.id.Profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.More:
                    replaceFragment(new MoreFragment());
                    break;
            }

            return true;
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}