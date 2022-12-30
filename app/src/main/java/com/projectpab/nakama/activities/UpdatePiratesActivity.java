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
    ActivityResultLauncher<String> launcher;
    private Pirates pirates;
    Uri uri;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdatePiratesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(UpdatePiratesActivity.this, PiratesActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri2) {
                binding.ivPiratesPic.setImageURI(uri2);
                uri = uri2;
            }
        });

        pirates = getIntent().getParcelableExtra("EXTRA_DATA");
        int piratesId = pirates.getPirates_id();

        binding.etPiratesName.setText(pirates.getPirates_name());

        String image = String.valueOf(Uri.parse(pirates.getPirates_photo()));
        Glide.with(binding.tilPiratesPic)
                .load(image)
                .into(binding.ivPiratesPic);

        binding.tilPiratesPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch("image/*");
            }
        });

        binding.btnUpdatePirates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar();
                String temp = pirates.getPirates_photo();
                String key = temp.substring(74,114);
                Toast.makeText(UpdatePiratesActivity.this, "key = "+key, Toast.LENGTH_SHORT).show();
                if(uri==null){
                    String piratesName = binding.etPiratesName.getText().toString();
                    String piratesPhoto = pirates.getPirates_photo();

                    boolean bolehUpdatePirates = true;

                    if (TextUtils.isEmpty(piratesName)){
                        bolehUpdatePirates = false;
                        binding.etPiratesName.setError("nama pirates tidak boleh kosong!");
                    }

                    if (bolehUpdatePirates){
                        updatePirates(piratesId, piratesName, piratesPhoto);
                    }
                    Toast.makeText(UpdatePiratesActivity.this, "Update Pirates Success!", Toast.LENGTH_SHORT).show();
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
                                            String piratesName = binding.etPiratesName.getText().toString();
                                            String piratesPhoto = pirates.getPirates_photo();

                                            boolean bolehUpdatePirates = true;

                                            if (TextUtils.isEmpty(piratesName)){
                                                bolehUpdatePirates = false;
                                                binding.etPiratesName.setError("nama pirates tidak boleh kosong!");
                                            }

                                            if (bolehUpdatePirates){
                                                updatePirates(piratesId, piratesName, piratesPhoto);
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                    Toast.makeText(UpdatePiratesActivity.this, "haloooo2", Toast.LENGTH_SHORT).show();
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