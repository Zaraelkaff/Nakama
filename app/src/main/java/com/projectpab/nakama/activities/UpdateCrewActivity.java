package com.projectpab.nakama.activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
    ActivityResultLauncher<String> launcher;
    private Crew crew;
    Uri uri;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateCrewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(UpdateCrewActivity.this, CrewActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri2) {
                binding.ivCrewPic.setImageURI(uri2);
                uri = uri2;
            }
        });

        crew = getIntent().getParcelableExtra("EXTRA_DATA");
        int crewId = crew.getCrew_id();

        binding.etCrewName.setText(crew.getCrew_name());
        binding.etCrewBounty.setText(crew.getCrew_bounty());

        String image = String.valueOf(Uri.parse(crew.getCrew_photo()));
        Glide.with(binding.tilCrewPic)
                .load(image)
                .into(binding.ivCrewPic);

        binding.tilCrewPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch("image/*");
            }
        });

        binding.btnUpdateCrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar();
                String temp = crew.getCrew_photo();
                String key = temp.substring(74,114);
                Toast.makeText(UpdateCrewActivity.this, "key = "+key, Toast.LENGTH_SHORT).show();
                if(uri==null){
                    String crewName = binding.etCrewName.getText().toString();
                    String crewBounty = binding.etCrewBounty.getText().toString();
                    String crewPhoto = crew.getCrew_photo();
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

                    if (bolehUpdateCrew){
                        updateCrew(crewId, piratesId, crewName, crewPhoto, crewBounty);
                    }
                    Toast.makeText(UpdateCrewActivity.this, "Update Crew Success!", Toast.LENGTH_SHORT).show();
                } else{
                    StorageReference reference = storage.getReference().child(key);
                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    database.getReference().child(key).setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            String crewName = binding.etCrewName.getText().toString();
                                            String crewBounty = binding.etCrewBounty.getText().toString();
                                            String crewPhoto = uri.toString();
                                            int piratesId = crew.getPirates_id();
                                            Toast.makeText(UpdateCrewActivity.this, "pirates id =" + piratesId, Toast.LENGTH_SHORT).show();
                                            boolean bolehUpdateCrew = true;

                                            if (TextUtils.isEmpty(crewName)){
                                                bolehUpdateCrew = false;
                                                binding.etCrewName.setError("Nama crew tidak boleh kosong!");
                                            }

                                            if (TextUtils.isEmpty(crewBounty)){
                                                bolehUpdateCrew = false;
                                                binding.etCrewBounty.setError("Bounty crew tidak boleh kosong!");
                                            }

                                            if (bolehUpdateCrew){
                                                updateCrew(crewId, piratesId, crewName, crewPhoto, crewBounty);
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });


    }

    private void updateCrew(int crewId, int piratesId, String crewName, String crewPhoto, String crewBounty) {
        showProgressBar();
        APIService api = Utilities.getRetrofit().create(APIService.class);
        Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show();
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