package com.projectpab.nakama.activities;

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

import com.projectpab.nakama.R;
import com.projectpab.nakama.adapters.HakiViewAdapter;
import com.projectpab.nakama.databinding.ActivityHakiBinding;
import com.projectpab.nakama.models.Crew;
import com.projectpab.nakama.models.Haki;
import com.projectpab.nakama.models.ValueData;
import com.projectpab.nakama.models.ValueNoData;
import com.projectpab.nakama.services.APIService;
import com.projectpab.nakama.services.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HakiActivity extends AppCompatActivity {
    private ActivityHakiBinding binding;
    private HakiViewAdapter hakiViewAdapter;
    private List<Haki> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHakiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        hakiViewAdapter = new HakiViewAdapter();
        binding.rvHaki.setLayoutManager(new LinearLayoutManager(this));
        binding.rvHaki.setAdapter(hakiViewAdapter);

        binding.fabAddHaki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HakiActivity.this, AddHakiActivity.class);
                startActivity(intent);
            }
        });

        binding.srlHaki.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllHaki();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllHaki();
    }

    private void showProgressBar(){
        binding.srlHaki.setRefreshing(true);
    }

    private void hideProgressBar(){
        binding.srlHaki.setRefreshing(false);
    }

    private void getAllHaki() {
        showProgressBar();
        APIService api = Utilities.getRetrofit().create(APIService.class);
        api.getAllHaki(Utilities.API_KEY).enqueue(new Callback<ValueData<Haki>>() {
            @Override
            public void onResponse(Call<ValueData<Haki>> call, Response<ValueData<Haki>> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success ==1){
                        data = response.body().getData();
                        hakiViewAdapter.setData(data, HakiActivity.this::onItemHakiLongClick, HakiActivity.this::onItemHakiClick);
                    }else{
                        Toast.makeText(HakiActivity.this,
                                message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(HakiActivity.this,
                            "Response code : "+response.code(),
                            Toast.LENGTH_SHORT).show();
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ValueData<Haki>> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(HakiActivity.this,
                        "Retrofit Error : " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onItemHakiClick(Haki haki, int i) {
        Intent intent = new Intent(HakiActivity.this, DetailHakiActivity.class);
        intent.putExtra("EXTRA_DATA", haki);
        startActivity(intent);
    }

    private void onItemHakiLongClick(View view, Haki haki, int i) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_popup);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_edit:
                        Intent intent = new Intent(HakiActivity.this, UpdateHakiActivity.class);
                        intent.putExtra("EXTRA_DATA",haki);
                        startActivity(intent);
                        return true;
                    case R.id.action_delete:
                        int id = haki.getHaki_id();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HakiActivity.this);
                        alertDialogBuilder.setTitle("Konfirmasi");
                        alertDialogBuilder.setMessage("Yakin ingin menghapus haki '" + haki.getHaki_name() + "' ? ");
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteHaki(id);
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

    private void deleteHaki(int id) {
        APIService api = Utilities.getRetrofit().create(APIService.class);
        api.deleteHaki(Utilities.API_KEY, id).enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                if (response.code() == 200){
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success == 1){
                        Toast.makeText(HakiActivity.this, message, Toast.LENGTH_SHORT).show();
                        getAllHaki();
                    }else{
                        Toast.makeText(HakiActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(HakiActivity.this,
                            "Response Code : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                Toast.makeText(HakiActivity.this,
                        "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}