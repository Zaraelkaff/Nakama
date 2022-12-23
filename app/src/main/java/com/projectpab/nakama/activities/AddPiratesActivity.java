package com.projectpab.nakama.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.projectpab.nakama.databinding.ActivityAddPiratesBinding;
import com.projectpab.nakama.models.ValueNoData;
import com.projectpab.nakama.services.APIService;
import com.projectpab.nakama.services.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPiratesActivity extends AppCompatActivity {
    private ActivityAddPiratesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPiratesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAddPirates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String piratesName = binding.etPiratesName.getText().toString();
                String piratesPhoto = binding.etPiratesPhoto.getText().toString();

                boolean bolehAddPirates = true;

                if (TextUtils.isEmpty(piratesName)){
                    bolehAddPirates = false;
                    binding.etPiratesName.setError("nama pirates tidak boleh kosong!");
                }

                if (TextUtils.isEmpty(piratesPhoto)){
                    bolehAddPirates = false;
                    binding.etPiratesPhoto.setError("poto pirates tidak boleh kosong!");
                }

                if (bolehAddPirates){
                    addPirates(piratesName, piratesPhoto);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void addPirates(String name, String photo){
        showProgressBar();
        APIService api = Utilities.getRetrofit().create(APIService.class);
        api.insertPirates(Utilities.API_KEY, name, photo).enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success == 1){
                        Toast.makeText(AddPiratesActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(AddPiratesActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddPiratesActivity.this,
                            "Response Code : " + response.code(), Toast.LENGTH_SHORT).show();
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(AddPiratesActivity.this,
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