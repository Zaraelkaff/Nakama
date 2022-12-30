package com.projectpab.nakama.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.projectpab.nakama.databinding.ActivityUpdateHakiBinding;
import com.projectpab.nakama.models.Haki;
import com.projectpab.nakama.models.ValueNoData;
import com.projectpab.nakama.services.APIService;
import com.projectpab.nakama.services.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateHakiActivity extends AppCompatActivity {
    private ActivityUpdateHakiBinding binding;
    private Haki haki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateHakiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        haki = getIntent().getParcelableExtra("EXTRA_DATA");
        int hakiId = haki.getHaki_id();

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(UpdateHakiActivity.this, HakiActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        binding.etHakiName.setText(haki.getHaki_name());
        binding.etHakiDescribe.setText(haki.getHaki_describe());

        binding.btnUpdateHaki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hakiName = binding.etHakiName.getText().toString();
                String hakiDescribe = binding.etHakiDescribe.getText().toString();

                boolean bolehUpdateHaki = true;

                if (TextUtils.isEmpty(hakiName)){
                    bolehUpdateHaki = false;
                    binding.etHakiName.setError("nama pirates tidak boleh kosong!");
                }

                if (TextUtils.isEmpty(hakiDescribe)){
                    bolehUpdateHaki = false;
                    binding.etHakiDescribe.setError("poto pirates tidak boleh kosong!");
                }

                if (bolehUpdateHaki){
                    updateHaki(hakiId, hakiName, hakiDescribe);
                }
            }
        });
    }

    private void updateHaki(int hakiId, String hakiName, String hakiDescribe) {
        showProgressBar();
        APIService api = Utilities.getRetrofit().create(APIService.class);
        api.updateHaki(Utilities.API_KEY, hakiId, hakiName, hakiDescribe).enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success == 1){
                        Toast.makeText(UpdateHakiActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(UpdateHakiActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UpdateHakiActivity.this,
                            "Response Code : " + response.code(), Toast.LENGTH_SHORT).show();
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(UpdateHakiActivity.this,
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