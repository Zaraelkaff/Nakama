package com.projectpab.nakama.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.projectpab.nakama.databinding.ActivityDetailHakiBinding;
import com.projectpab.nakama.models.Haki;

public class DetailHakiActivity extends AppCompatActivity {
    private ActivityDetailHakiBinding binding;
    private Haki haki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailHakiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        haki = getIntent().getParcelableExtra("EXTRA_DATA");

        binding.tvHakiName.setText(haki.getHaki_name());
        binding.tvHakiDescribe.setText(haki.getHaki_describe());
    }
}