package com.projectpab.nakama.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.projectpab.nakama.R;
import com.projectpab.nakama.adapters.MovieViewAdapter;
import com.projectpab.nakama.databinding.ActivityMovieBinding;
import com.projectpab.nakama.models.Movie;
import com.projectpab.nakama.models.ValueData;
import com.projectpab.nakama.models.ValueNoData;
import com.projectpab.nakama.services.APIService;
import com.projectpab.nakama.services.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {
    private ActivityMovieBinding binding;
    private MovieViewAdapter movieViewAdapter;
    private List<Movie> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        movieViewAdapter = new MovieViewAdapter();
        binding.rvMovie.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMovie.setAdapter(movieViewAdapter);

        binding.fabAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieActivity.this, AddMovieActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllMovie();
    }

    private void getAllMovie() {
        showProgressBar();
        APIService api = Utilities.getRetrofit().create(APIService.class);
        api.getAllMovie(Utilities.API_KEY).enqueue(new Callback<ValueData<Movie>>() {
            @Override
            public void onResponse(Call<ValueData<Movie>> call, Response<ValueData<Movie>> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success ==1){
                        data = response.body().getData();
                        movieViewAdapter.setData(data, MovieActivity.this::onItemMovieLongClick, MovieActivity.this::onItemMovieClick);
                    }else{
                        Toast.makeText(MovieActivity.this,
                                message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MovieActivity.this,
                            "Response code : "+response.code(),
                            Toast.LENGTH_SHORT).show();
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ValueData<Movie>> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(MovieActivity.this,
                        "Retrofit Error : " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onItemMovieClick(Movie movie, int i) {
        Toast.makeText(this, "HALOOOOO", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MovieActivity.this, DetailMovieActivity.class);
        intent.putExtra("EXTRA_DATA", movie);
        startActivity(intent);
    }

    private void onItemMovieLongClick(View view, Movie movie, int i){
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_popup);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_edit:
                        Intent intent = new Intent(MovieActivity.this, UpdateMovieActivity.class);
                        intent.putExtra("EXTRA_DATA",movie);
                        startActivity(intent);
                        return true;
                    case R.id.action_delete:
                        int id = movie.getMovie_id();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MovieActivity.this);
                        alertDialogBuilder.setTitle("Konfirmasi");
                        alertDialogBuilder.setMessage("Yakin ingin menghapus pirates '" + movie.getMovie_name() + "' ? ");
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deletePost(id);
                            }
                        });
                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whick) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void deletePost(int id) {
        APIService api = Utilities.getRetrofit().create(APIService.class);
        api.deleteMovie(Utilities.API_KEY, id).enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success == 1){
                        Toast.makeText(MovieActivity.this, message, Toast.LENGTH_SHORT).show();
                        getAllMovie();
                    }else{
                        Toast.makeText(MovieActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MovieActivity.this,
                            "Response Code : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                Toast.makeText(MovieActivity.this,
                        "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressBar(){
        binding.srlMovie.setRefreshing(true);
    }

    private void hideProgressBar(){
        binding.srlMovie.setRefreshing(false);
    }


}