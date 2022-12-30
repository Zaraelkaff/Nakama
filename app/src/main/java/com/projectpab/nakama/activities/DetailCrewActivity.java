package com.projectpab.nakama.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
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
        Glide.with(this)
                .load(crew.getCrew_photo())
                .into(binding.ivCrewPhoto);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailCrewActivity.this, CrewActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}