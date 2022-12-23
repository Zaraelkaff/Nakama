package com.projectpab.nakama.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.projectpab.nakama.databinding.ActivityAddCrewBinding;
import com.projectpab.nakama.databinding.ActivityUpdateCrewBinding;
import com.projectpab.nakama.models.Crew;
import com.projectpab.nakama.models.ValueNoData;
import com.projectpab.nakama.services.APIService;
import com.projectpab.nakama.services.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCrewActivity extends AppCompatActivity {
    private ActivityUpdateCrewBinding binding;
    private Crew crew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateCrewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        crew = getIntent().getParcelableExtra("EXTRA_DATA");
        int crewId = crew.getCrew_id();

        binding.etCrewName.setText(crew.getCrew_name());
        binding.etCrewPhoto.setText(crew.getCrew_photo());
        binding.etCrewBounty.setText(crew.getCrew_bounty());

        binding.btnUpdateCrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String crewName = binding.etCrewName.getText().toString();
                String crewBounty = binding.etCrewBounty.getText().toString();
                String crewPhoto = binding.etCrewPhoto.getText().toString();
                int piratesId = crew.getPirates_id();
                Toast.makeText(UpdateCrewActivity.this, "pirates id =" + piratesId, Toast.LENGTH_SHORT).show();
                boolean bolehUpdateCrew = true;

                if (TextUtils.isEmpty(crewName)){
                    bolehUpdateCrew = false;
                    binding.etCrewName.setError("nama crew tidak boleh kosong!");
                }

                if (TextUtils.isEmpty(crewBounty)){
                    bolehUpdateCrew = false;
                    binding.etCrewBounty.setError("bounty crew tidak boleh kosong!");
                }

                if (TextUtils.isEmpty(crewPhoto)){
                    bolehUpdateCrew = false;
                    binding.etCrewPhoto.setError("poto crew tidak boleh kosong!");
                }

                if (bolehUpdateCrew){
                    updateCrew(crewId, piratesId, crewName, crewPhoto, crewBounty);
                }

            }
        });


    }

    private void updateCrew(int crewId, int piratesId, String crewName, String crewPhoto, String crewBounty) {
        showProgressBar();
        APIService api = Utilities.getRetrofit().create(APIService.class);
        Toast.makeText(this, "HALOW", Toast.LENGTH_SHORT).show();
        api.updateCrew(Utilities.API_KEY, crewId, piratesId, crewName, crewPhoto, crewBounty).enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success == 1){
                        Toast.makeText(UpdateCrewActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(UpdateCrewActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UpdateCrewActivity.this,
                            "Response Code : " + response.code(), Toast.LENGTH_SHORT).show();
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(UpdateCrewActivity.this,
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