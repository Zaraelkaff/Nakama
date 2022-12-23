package com.projectpab.nakama.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.projectpab.nakama.databinding.ActivityAddHakiBinding;
import com.projectpab.nakama.models.ValueNoData;
import com.projectpab.nakama.services.APIService;
import com.projectpab.nakama.services.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddHakiActivity extends AppCompatActivity {
    private ActivityAddHakiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddHakiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAddHaki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hakiName = binding.etHakiName.getText().toString();
                String hakiDescribe = binding.etHakiDescribe.getText().toString();

                boolean bolehAddHaki = true;

                if (TextUtils.isEmpty(hakiName)){
                    bolehAddHaki = false;
                    binding.etHakiName.setError("nama pirates tidak boleh kosong!");
                }

                if (TextUtils.isEmpty(hakiDescribe)){
                    bolehAddHaki = false;
                    binding.etHakiDescribe.setError("poto pirates tidak boleh kosong!");
                }

                if (bolehAddHaki){
                    addHaki(hakiName, hakiDescribe);
                }
            }
        });
    }

    private void addHaki(String hakiName, String hakiDescribe) {
        showProgressBar();
        APIService api = Utilities.getRetrofit().create(APIService.class);
        api.insertHaki(Utilities.API_KEY, hakiName, hakiDescribe).enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success == 1){
                        Toast.makeText(AddHakiActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(AddHakiActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddHakiActivity.this,
                            "Response Code : " + response.code(), Toast.LENGTH_SHORT).show();
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(AddHakiActivity.this,
                        "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void showProgressBar(){
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        binding.progressBar.setVisibility(View.GONE);
    }

}