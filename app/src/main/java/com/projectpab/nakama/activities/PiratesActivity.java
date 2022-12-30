package com.projectpab.nakama.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.projectpab.nakama.R;
import com.projectpab.nakama.adapters.PiratesViewAdapter;
import com.projectpab.nakama.databinding.ActivityPiratesBinding;
import com.projectpab.nakama.models.Crew;
import com.projectpab.nakama.models.Pirates;
import com.projectpab.nakama.models.ValueData;
import com.projectpab.nakama.models.ValueNoData;
import com.projectpab.nakama.services.APIService;
import com.projectpab.nakama.services.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PiratesActivity extends AppCompatActivity {
    private ActivityPiratesBinding binding;
    private PiratesViewAdapter piratesViewAdapter;
    private List<Pirates> data = new ArrayList<>();
    private List<Crew> dataCrew = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPiratesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        piratesViewAdapter = new PiratesViewAdapter();
        binding.rvPirates.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPirates.setAdapter(piratesViewAdapter);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PiratesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.fabAddPirates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PiratesActivity.this, AddPiratesActivity.class);
                startActivity(intent);
            }
        });

        binding.srlPirates.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllPirates();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllPirates();
    }

    private void showProgressBar(){
        binding.srlPirates.setRefreshing(true);
    }

    private void hideProgressBar(){
        binding.srlPirates.setRefreshing(false);
    }

    private void getAllPirates(){
        showProgressBar();
        APIService api = Utilities.getRetrofit().create(APIService.class);
        api.getAllPirates(Utilities.API_KEY).enqueue(new Callback<ValueData<Pirates>>() {
            @Override
            public void onResponse(Call<ValueData<Pirates>> call, Response<ValueData<Pirates>> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success ==1){
                        data = response.body().getData();
                        piratesViewAdapter.setData(data, PiratesActivity.this::onItemPiratesLongClick, PiratesActivity.this::onItemPiratesClick);
                    }else{
                        Toast.makeText(PiratesActivity.this,
                                message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(PiratesActivity.this,
                            "Response code : "+response.code(),
                            Toast.LENGTH_SHORT).show();
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ValueData<Pirates>> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(PiratesActivity.this,
                        "Retrofit Error : " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onItemPiratesClick(Pirates pirates, int i) {
        Intent intent = new Intent(PiratesActivity.this, DetailPiratesActivity.class);
        intent.putExtra("EXTRA_DATA", pirates);
        startActivity(intent);
    }

    private void onItemPiratesLongClick(View view, Pirates pirates, int i) {
        // Toast.makeText(this, "Pos : " + pirates.getPirates_name(), Toast.LENGTH_SHORT).show();
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_popup);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_edit:
                        Intent intent = new Intent(PiratesActivity.this, UpdatePiratesActivity.class);
                        intent.putExtra("EXTRA_DATA",pirates);
                        startActivity(intent);
                        return true;
                    case R.id.action_delete:
                        int id = pirates.getPirates_id();
                        String url = pirates.getPirates_photo();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PiratesActivity.this);
                        alertDialogBuilder.setTitle("Konfirmasi");
                        alertDialogBuilder.setMessage("Yakin ingin menghapus pirates '" + pirates.getPirates_name() + "' ? ");
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deletePirates(id, url);
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

    private void deletePirates(int id, String url) {
        APIService api = Utilities.getRetrofit().create(APIService.class);
        api.getCrewByPirates(Utilities.API_KEY, id).enqueue(new Callback<ValueData<Crew>>() {
            @Override
            public void onResponse(Call<ValueData<Crew>> call, Response<ValueData<Crew>> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success ==1){
                        dataCrew = response.body().getData();
                        int size = dataCrew.size();
                        for(int i = 0; i<size; i++){
                            String urlCrew = dataCrew.get(i).getCrew_photo();
                            Toast.makeText(PiratesActivity.this, "UrlCrew : "+urlCrew, Toast.LENGTH_SHORT).show();
                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(urlCrew);
                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(PiratesActivity.this, "BERHASIL", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PiratesActivity.this, "GAGAL", Toast.LENGTH_SHORT).show();
                                }
                            });
                            String link2 = urlCrew.substring(74,114);
                            Toast.makeText(PiratesActivity.this, "link: "+link2, Toast.LENGTH_SHORT).show();
                            DatabaseReference coba2 = FirebaseDatabase.getInstance().getReference(link2);
                            coba2.removeValue();
                        }
                    }else{
                        Toast.makeText(PiratesActivity.this,
                                message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(PiratesActivity.this,
                            "Response code : "+response.code(),
                            Toast.LENGTH_SHORT).show();
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ValueData<Crew>> call, Throwable t) {
                Toast.makeText(PiratesActivity.this, "halo gagal", Toast.LENGTH_SHORT).show();

            }
        });

        api.deletePirates(Utilities.API_KEY, id).enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success == 1){
                        Toast.makeText(PiratesActivity.this, message, Toast.LENGTH_SHORT).show();
                        getAllPirates();
                    }else{
                        Toast.makeText(PiratesActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(PiratesActivity.this,
                            "Response Code : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                Toast.makeText(PiratesActivity.this,
                        "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(PiratesActivity.this, "BERHASIL", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PiratesActivity.this, "GAGAL", Toast.LENGTH_SHORT).show();
            }
        });
        String link = url.substring(74,117);
        Toast.makeText(this, "link =" + link, Toast.LENGTH_SHORT).show();
        DatabaseReference coba = FirebaseDatabase.getInstance().getReference(link);
        coba.removeValue();
    }


}