package com.projectpab.nakama.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

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
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        movie = getIntent().getParcelableExtra("EXTRA_DATA");
        int movieId = movie.getMovie_id();

        binding.etMovieName.setText(movie.getMovie_name());
        binding.etMovieYear.setText(movie.getMovie_year());
        binding.etMoviePhoto.setText(movie.getMovie_photo());

        binding.btnUpdateMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movieName = binding.etMovieName.getText().toString();
                String movieYear= binding.etMovieYear.getText().toString();
                String moviePhoto = binding.etMoviePhoto.getText().toString();

                boolean bolehUpdateMovie = true;

                if (TextUtils.isEmpty(movieName)){
                    bolehUpdateMovie = false;
                    binding.etMovieName.setError("nama movie tidak boleh kosong!");
                }

                if (TextUtils.isEmpty(movieYear)){
                    bolehUpdateMovie = false;
                    binding.etMovieYear.setError("tahun movie tidak boleh kosong!");
                }

                if (TextUtils.isEmpty(moviePhoto)){
                    bolehUpdateMovie = false;
                    binding.etMoviePhoto.setError("poto movie tidak boleh kosong!");
                }

                if (bolehUpdateMovie){
                    updateMovie(movieId, movieName, movieYear, moviePhoto);
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