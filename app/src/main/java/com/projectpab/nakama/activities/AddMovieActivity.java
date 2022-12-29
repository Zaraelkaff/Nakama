package com.projectpab.nakama.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.projectpab.nakama.databinding.ActivityAddMovieBinding;
import com.projectpab.nakama.models.ValueNoData;
import com.projectpab.nakama.services.APIService;
import com.projectpab.nakama.services.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMovieActivity extends AppCompatActivity {
    private ActivityAddMovieBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMovieActivity.this, MovieActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movieName = binding.etMovieName.getText().toString();
                String movieYear= binding.etMovieYear.getText().toString();
                String moviePhoto = binding.etMoviePhoto.getText().toString();

                boolean bolehAddMovie = true;

                if (TextUtils.isEmpty(movieName)){
                    bolehAddMovie = false;
                    binding.etMovieName.setError("nama movie tidak boleh kosong!");
                }

                if (TextUtils.isEmpty(movieYear)){
                    bolehAddMovie = false;
                    binding.etMovieYear.setError("tahun movie tidak boleh kosong!");
                }

                if (TextUtils.isEmpty(moviePhoto)){
                    bolehAddMovie = false;
                    binding.etMoviePhoto.setError("poto movie tidak boleh kosong!");
                }

                if (bolehAddMovie){
                    addMovie(movieName, movieYear, moviePhoto);
                }
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