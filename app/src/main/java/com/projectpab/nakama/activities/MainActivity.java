package com.projectpab.nakama.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.projectpab.nakama.R;
import com.projectpab.nakama.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MediaPlayer audio;
    private ActivityMainBinding binding;

    private List<SlideModel> imageList = new ArrayList<>();
    private ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageList.add(new SlideModel(R.drawable.onepiece3,null));
        imageList.add(new SlideModel(R.drawable.onepiece1,null));
        imageList.add(new SlideModel(R.drawable.onepiece2,null));

        imageSlider = findViewById(R.id.image_slider);
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP);

        audio = MediaPlayer.create(this, R.raw.sound);
        audio.setLooping(true);
        audio.setVolume(1,1);
        audio.start();

        binding.sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audio.isPlaying()){
                    audio.pause();
                    binding.sound.setImageResource(R.drawable.ic_volume_off_24);
                }else{
                    audio.start();
                    binding.sound.setImageResource(R.drawable.ic_volume_up_24);
                }
            }
        });

        binding.pirates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PiratesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.haki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HakiActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}