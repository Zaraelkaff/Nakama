package com.projectpab.nakama.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectpab.nakama.R;
import com.projectpab.nakama.models.Pirates;
import com.projectpab.nakama.utils.ItemClickListener;
import com.projectpab.nakama.utils.ItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class PiratesViewAdapter extends RecyclerView.Adapter<PiratesViewAdapter.ViewHolder> {
    private List<Pirates> data= new ArrayList<>();
    private ItemLongClickListener<Pirates> itemLongClickListener;
    private ItemClickListener<Pirates> itemClickListener;

    public void setData(List<Pirates> data, ItemLongClickListener<Pirates> itemLongClickListener, ItemClickListener<Pirates> itemClickListener) {
        this.data = data;
        this.itemLongClickListener = itemLongClickListener;
        this.itemClickListener = itemClickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PiratesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pirates, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PiratesViewAdapter.ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        Pirates pirates = data.get(pos);
        holder.tvPiratesName.setText(pirates.getPirates_name());
        holder.tvPiratesDate.setText(pirates.getCreated_date());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemLongClickListener.onItemLongClick(v, pirates, pos);
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(pirates, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPiratesName, tvPiratesDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPiratesName = itemView.findViewById(R.id.tv_pirates_name);
            tvPiratesDate = itemView.findViewById(R.id.tv_pirates_date);
        }
    }
}
