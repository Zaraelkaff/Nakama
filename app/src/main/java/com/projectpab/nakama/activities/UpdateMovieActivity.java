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
import com.projectpab.nakama.databinding.ActivityUpdateMovieBinding;
import com.projectpab.nakama.models.Movie;
import com.projectpab.nakama.models.ValueNoData;
import com.projectpab.nakama.services.APIService;
import com.projectpab.nakama.services.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateMovieActivity extends AppCompatActivity {
    private ActivityUpdateMovieBinding binding;
    ActivityResultLauncher<String> launcher;
    private Movie movie;
    Uri uri;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateMovieActivity.this, MovieActivity.class);
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

        movie = getIntent().getParcelableExtra("EXTRA_DATA");
        int movieId = movie.getMovie_id();

        binding.etMovieName.setText(movie.getMovie_name());
        binding.etMovieYear.setText(movie.getMovie_year());

        String image = String.valueOf(Uri.parse(movie.getMovie_photo()));
        Glide.with(binding.tilMoviePic)
                .load(image)
                .into(binding.ivMoviePic);

        binding.tilMoviePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch("image/*");
            }
        });

        binding.btnUpdateMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar();
                String temp = movie.getMovie_photo();
                String key = temp.substring(74,115);
                Toast.makeText(UpdateMovieActivity.this, "key = "+key, Toast.LENGTH_SHORT).show();
                if(uri==null){
                    String movieName = binding.etMovieName.getText().toString();
                    String movieYear= binding.etMovieYear.getText().toString();
                    String moviePhoto = movie.getMovie_photo();

                    boolean bolehUpdateMovie = true;

                    if (TextUtils.isEmpty(movieName)){
                        bolehUpdateMovie = false;
                        binding.etMovieName.setError("nama movie tidak boleh kosong!");
                    }

                    if (TextUtils.isEmpty(movieYear)){
                        bolehUpdateMovie = false;
                        binding.etMovieYear.setError("tahun movie tidak boleh kosong!");
                    }

                    if (bolehUpdateMovie){
                        updateMovie(movieId, movieName, movieYear, moviePhoto);
                    }
                    Toast.makeText(UpdateMovieActivity.this, "Update Movie", Toast.LENGTH_SHORT).show();
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
                                            String movieName = binding.etMovieName.getText().toString();
                                            String movieYear= binding.etMovieYear.getText().toString();
                                            String moviePhoto = uri.toString();

                                            boolean bolehUpdateMovie = true;

                                            if (TextUtils.isEmpty(movieName)){
                                                bolehUpdateMovie = false;
                                                binding.etMovieName.setError("nama movie tidak boleh kosong!");
                                            }

                                            if (TextUtils.isEmpty(movieYear)){
                                                bolehUpdateMovie = false;
                                                binding.etMovieYear.setError("tahun movie tidak boleh kosong!");
                                            }

                                            if (bolehUpdateMovie){
                                                updateMovie(movieId, movieName, movieYear, moviePhoto);
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

    private void updateMovie(int movieId, String movieName, String movieYear, String moviePhoto) {
        showProgressBar();
        APIService api = Utilities.getRetrofit().create(APIService.class);
        api.updateMovie(Utilities.API_KEY, movieId, movieName, movieYear, moviePhoto).enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success == 1){
                        Toast.makeText(UpdateMovieActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(UpdateMovieActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UpdateMovieActivity.this,
                            "Response Code : " + response.code(), Toast.LENGTH_SHORT).show();
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(UpdateMovieActivity.this,
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