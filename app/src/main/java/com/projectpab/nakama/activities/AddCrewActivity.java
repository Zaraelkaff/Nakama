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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projectpab.nakama.adapters.CrewViewAdapter;
import com.projectpab.nakama.databinding.ActivityAddCrewBinding;
import com.projectpab.nakama.databinding.ActivityAddPiratesBinding;
import com.projectpab.nakama.models.Crew;
import com.projectpab.nakama.models.Pirates;
import com.projectpab.nakama.models.ValueNoData;
import com.projectpab.nakama.services.APIService;
import com.projectpab.nakama.services.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCrewActivity extends AppCompatActivity {
    private ActivityAddCrewBinding binding;
    private Pirates pirates;
    ActivityResultLauncher<String> launcher;
    Uri uri;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCrewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri2) {
                binding.ivCrewPic.setImageURI(uri2);
                uri=uri2;
            }
        });

        binding.tilCrewPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch("image/*");
            }
        });

        pirates = getIntent().getParcelableExtra("EXTRA_DATA");

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCrewActivity.this, CrewActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnAddCrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar();
                String key = "crew"+ UUID.randomUUID().toString();
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
                                        int piratesId = pirates.getPirates_id();
                                        Toast.makeText(AddCrewActivity.this, "pirates id =" + piratesId, Toast.LENGTH_SHORT).show();
                                        boolean bolehAddCrew = true;

                                        if (TextUtils.isEmpty(crewName)) {
                                            bolehAddCrew = false;
                                            binding.etCrewName.setError("Nama crew tidak boleh kosong!");
                                        }

                                        if (TextUtils.isEmpty(crewBounty)) {
                                            bolehAddCrew = false;
                                            binding.etCrewBounty.setError("Bounty crew tidak boleh kosong!");
                                        }

                                        if (bolehAddCrew) {
                                            addCrew(piratesId, crewName, crewPhoto, crewBounty);
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private void addCrew(int piratesId, String crewName, String crewPhoto, String crewBounty) {
        showProgressBar();
        APIService api = Utilities.getRetrofit().create(APIService.class);
        api.insertCrew(Utilities.API_KEY, piratesId, crewName, crewPhoto, crewBounty).enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success == 1){
                        Toast.makeText(AddCrewActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(AddCrewActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddCrewActivity.this,
                            "Response Code : " + response.code(), Toast.LENGTH_SHORT).show();
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(AddCrewActivity.this,
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