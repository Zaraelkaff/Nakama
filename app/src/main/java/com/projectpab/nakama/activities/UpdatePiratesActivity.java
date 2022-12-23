package com.projectpab.nakama.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.projectpab.nakama.databinding.ActivityAddPiratesBinding;
import com.projectpab.nakama.databinding.ActivityUpdatePiratesBinding;
import com.projectpab.nakama.models.Pirates;
import com.projectpab.nakama.models.ValueNoData;
import com.projectpab.nakama.services.APIService;
import com.projectpab.nakama.services.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePiratesActivity extends AppCompatActivity {
    private ActivityUpdatePiratesBinding binding;
    private Pirates pirates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdatePiratesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pirates = getIntent().getParcelableExtra("EXTRA_DATA");
        int piratesId = pirates.getPirates_id();

        binding.etPiratesName.setText(pirates.getPirates_name());
        binding.etPiratesPhoto.setText(pirates.getPirates_photo());

        binding.btnUpdatePirates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String piratesName = binding.etPiratesName.getText().toString();
                String piratesPhoto = binding.etPiratesPhoto.getText().toString();

                boolean bolehUpdatePirates = true;

                if (TextUtils.isEmpty(piratesName)){
                    bolehUpdatePirates = false;
                    binding.etPiratesName.setError("nama pirates tidak boleh kosong!");
                }

                if (TextUtils.isEmpty(piratesPhoto)){
                    bolehUpdatePirates = false;
                    binding.etPiratesPhoto.setError("poto pirates tidak boleh kosong!");
                }

                if (bolehUpdatePirates){
                    updatePirates(piratesId, piratesName, piratesPhoto);
                }
            }
        });
    }

    private void updatePirates(int piratesId, String piratesName, String piratesPhoto) {
        showProgressBar();
        APIService api = Utilities.getRetrofit().create(APIService.class);
        api.updatePirates(Utilities.API_KEY, piratesId, piratesName, piratesPhoto).enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success == 1){
                        Toast.makeText(UpdatePiratesActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(UpdatePiratesActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UpdatePiratesActivity.this,
                            "Response Code : " + response.code(), Toast.LENGTH_SHORT).show();
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(UpdatePiratesActivity.this,
                        "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressBar(){
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        binding.progressBar.setVisibility(View.GONE);
    }
}