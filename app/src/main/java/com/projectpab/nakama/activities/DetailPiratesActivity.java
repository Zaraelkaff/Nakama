package com.projectpab.nakama.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.projectpab.nakama.databinding.ActivityDetailPiratesBinding;
import com.projectpab.nakama.databinding.ActivityPiratesBinding;
import com.projectpab.nakama.models.Pirates;

public class DetailPiratesActivity extends AppCompatActivity {
    private ActivityDetailPiratesBinding binding;
    private Pirates pirates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailPiratesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pirates = getIntent().getParcelableExtra("EXTRA_DATA");
        String piratesName = pirates.getPirates_name();
        String piratesPhoto = pirates.getPirates_photo();

        binding.tvPiratesName.setText(piratesName);
        //binding.tvPiratesPhoto.setText(piratesPhoto);


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPiratesActivity.this, PiratesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnCrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailPiratesActivity.this, "id pirates :" + pirates.getPirates_id(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailPiratesActivity.this, CrewActivity.class);
                intent.putExtra("EXTRA_DATA", pirates);
                startActivity(intent);
            }
        });

    }
}