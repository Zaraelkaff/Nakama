package com.projectpab.nakama.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.projectpab.nakama.databinding.ActivityDetailMovieBinding;
import com.projectpab.nakama.models.Movie;

public class DetailMovieActivity extends AppCompatActivity {
    private ActivityDetailMovieBinding binding;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        movie = getIntent().getParcelableExtra("EXTRA_DATA");

        binding.tvMovieName.setText(movie.getMovie_name());
       // binding.tvMoviePhoto.setText(movie.getMovie_photo());
        binding.tvMovieYear.setText(movie.getMovie_year());

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailMovieActivity.this, MovieActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}