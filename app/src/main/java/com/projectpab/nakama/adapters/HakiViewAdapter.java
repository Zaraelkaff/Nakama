package com.projectpab.nakama.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectpab.nakama.R;
import com.projectpab.nakama.models.Haki;
import com.projectpab.nakama.models.Pirates;
import com.projectpab.nakama.utils.ItemClickListener;
import com.projectpab.nakama.utils.ItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class HakiViewAdapter extends RecyclerView.Adapter<HakiViewAdapter.ViewHolder> {
    private List<Haki> data = new ArrayList<>();
    private ItemLongClickListener<Haki> itemLongClickListener;
    private ItemClickListener<Haki> itemClickListener;

    public void setData(List<Haki> data, ItemLongClickListener<Haki> itemLongClickListener, ItemClickListener<Haki> itemClickListener) {
        this.data = data;
        this.itemLongClickListener = itemLongClickListener;
        this.itemClickListener = itemClickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HakiViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_haki, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HakiViewAdapter.ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        Haki haki = data.get(pos);
        holder.tvHakiName.setText(haki.getHaki_name());
        holder.tvHakiDate.setText(haki.getCreated_date());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemLongClickListener.onItemLongClick(v, haki, pos);
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(haki, pos);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHakiName, tvHakiDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHakiName = itemView.findViewById(R.id.tv_haki_name);
            tvHakiDate = itemView.findViewById(R.id.tv_haki_date);
        }
    }
}
