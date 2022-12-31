package com.projectpab.nakama.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;
import com.projectpab.nakama.R;
import com.projectpab.nakama.services.Utilities;

public class ZoomActivity extends AppCompatActivity {

    private ZoomageView zvImage;
    private ImageView ivBack;
    private String fileUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        fileUrl = getIntent().getExtras().getString(Utilities.EXTRA_ZOOM_FOTO);

        zvImage = findViewById(R.id.zoomageView);
        ivBack = findViewById(R.id.iv_back);

        Glide.with(getApplicationContext())
                .load(fileUrl)
                .into(zvImage);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}