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
import com.projectpab.nakama.adapters.CrewViewAdapter;
import com.projectpab.nakama.databinding.ActivityCrewBinding;
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

public class CrewActivity extends AppCompatActivity {
    private ActivityCrewBinding binding;
    private Pirates pirates;
    private CrewViewAdapter crewViewAdapter;
    private List<Crew> data = new ArrayList<>();
    private int piratesId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        piratesId =  getIntent().getIntExtra("EXTRA_DATA",0);

        crewViewAdapter = new CrewViewAdapter();
        binding.rvCrew.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCrew.setAdapter(crewViewAdapter);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CrewActivity.this, DetailPiratesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.fabAddCrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CrewActivity.this, "id pirates :" + pirates.getPirates_id(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CrewActivity.this, AddCrewActivity.class);
                intent.putExtra("EXTRA_DATA", pirates);
                startActivity(intent);
            }
        });

        binding.srlCrew.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCrewByPirates(piratesId);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        int piratesId = pirates.getPirates_id();
        getCrewByPirates(piratesId);
    }

    private void getCrewByPirates(int piratesId) {
        showProgressBar();
        APIService api = Utilities.getRetrofit().create(APIService.class);
        api.getCrewByPirates(Utilities.API_KEY, piratesId).enqueue(new Callback<ValueData<Crew>>() {
            @Override
            public void onResponse(Call<ValueData<Crew>> call, Response<ValueData<Crew>> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success ==1){
                        data = response.body().getData();
                        crewViewAdapter.setData(data, CrewActivity.this::onItemCrewLongClick, CrewActivity.this::onItemCrewClick);
                    }else{
                        Toast.makeText(CrewActivity.this,
                                message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(CrewActivity.this,
                            "Response code : "+response.code(),
                            Toast.LENGTH_SHORT).show();
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ValueData<Crew>> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(CrewActivity.this,
                        "Retrofit Error : " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onItemCrewLongClick(View view, Crew crew, int i) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_popup);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_edit:
                        Intent intent = new Intent(CrewActivity.this, UpdateCrewActivity.class);
                        intent.putExtra("EXTRA_DATA",crew);
                        startActivity(intent);
                        return true;
                    case R.id.action_delete:
                        int idCrew = crew.getCrew_id();
                        String url = crew.getCrew_photo();
                        int idPirates = crew.getPirates_id();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CrewActivity.this);
                        alertDialogBuilder.setTitle("Konfirmasi");
                        alertDialogBuilder.setMessage("Yakin ingin menghapus crew '" + crew.getCrew_name() + "' ? ");
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteCrew(idCrew, idPirates, url);
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

    private void deleteCrew(int idCrew, int idPirates, String url) {
        APIService api = Utilities.getRetrofit().create(APIService.class);
        api.deleteCrew(Utilities.API_KEY, idCrew).enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success == 1){
                        Toast.makeText(CrewActivity.this, message, Toast.LENGTH_SHORT).show();
                        getCrewByPirates(idPirates);
                    }else{
                        Toast.makeText(CrewActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CrewActivity.this,
                            "Response Code : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                Toast.makeText(CrewActivity.this,
                        "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CrewActivity.this, "Deleted Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CrewActivity.this, "Deleted Failed", Toast.LENGTH_SHORT).show();
            }
        });
        String link = url.substring(74,114);
        Toast.makeText(this, "link : " + link, Toast.LENGTH_SHORT).show();
        DatabaseReference coba = FirebaseDatabase.getInstance().getReference(link);
        coba.removeValue();
    }

    private void onItemCrewClick(Crew crew, int i) {
        Intent intent = new Intent(CrewActivity.this, DetailCrewActivity.class);
        intent.putExtra("EXTRA_DATA", crew);
        startActivity(intent);
    }

    private void showProgressBar(){
        binding.srlCrew.setRefreshing(true);
    }

    private void hideProgressBar(){
        binding.srlCrew.setRefreshing(false);
    }
}