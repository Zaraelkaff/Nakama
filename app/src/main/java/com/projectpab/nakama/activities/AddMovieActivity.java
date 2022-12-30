package com.projectpab.nakama.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projectpab.nakama.databinding.ActivityAddMovieBinding;
import com.projectpab.nakama.models.ValueNoData;
import com.projectpab.nakama.services.APIService;
import com.projectpab.nakama.services.Utilities;

import java.util.ArrayDeque;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMovieActivity extends AppCompatActivity {
    private ActivityAddMovieBinding binding;
    ActivityResultLauncher<String> launcher;
    Uri uri;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMovieActivity.this, MovieActivity.class);
                startActivity(intent);
                finish();
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri2) {
                binding.ivMoviePic.setImageURI(uri2);
                uri = uri2;
            }
        });

        binding.tilMoviePic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                launcher.launch("image/*");
            }
        });

        binding.btnAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar();
                String key = "movie"+UUID.randomUUID().toString();
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

                                        String movieName = binding.etMovieName.getText().toString();
                                        String movieYear= binding.etMovieYear.getText().toString();
                                        String moviePhoto = uri.toString();

                                        boolean bolehAddMovie = true;

                                        if (TextUtils.isEmpty(movieName)){
                                            bolehAddMovie = false;
                                            binding.etMovieName.setError("Nama movie tidak boleh kosong!");
                                        }

                                        if (TextUtils.isEmpty(movieYear)){
                                            bolehAddMovie = false;
                                            binding.etMovieYear.setError("Tahun movie tidak boleh kosong!");
                                        }

                                        if (bolehAddMovie){
                                            addMovie(movieName, movieYear, moviePhoto);
                                            hideProgressBar();
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

    private void addMovie(String movieName, String movieYear, String moviePhoto) {
        showProgressBar();
        APIService api = Utilities.getRetrofit().create(APIService.class);
        api.insertMovie(Utilities.API_KEY, movieName, movieYear, moviePhoto).enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success == 1){
                        Toast.makeText(AddMovieActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(AddMovieActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddMovieActivity.this,
                            "Response Code : " + response.code(), Toast.LENGTH_SHORT).show();
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(AddMovieActivity.this,
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