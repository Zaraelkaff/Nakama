package com.projectpab.nakama.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.projectpab.nakama.databinding.ActivityDetailCrewBinding;
import com.projectpab.nakama.models.Crew;

public class DetailCrewActivity extends AppCompatActivity {
    private ActivityDetailCrewBinding binding;
    private Crew crew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailCrewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        crew = getIntent().getParcelableExtra("EXTRA_DATA");

        binding.tvCrewName.setText(crew.getCrew_name());
        binding.tvCrewBounty.setText(crew.getCrew_bounty());
        binding.tvCrewPhoto.setText(crew.getCrew_photo());
    }
}