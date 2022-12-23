package com.projectpab.nakama.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectpab.nakama.R;
import com.projectpab.nakama.models.Crew;
import com.projectpab.nakama.models.Pirates;
import com.projectpab.nakama.utils.ItemClickListener;
import com.projectpab.nakama.utils.ItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class CrewViewAdapter extends RecyclerView.Adapter<CrewViewAdapter.ViewHolder> {
    private List<Crew> data= new ArrayList<>();
    private ItemLongClickListener<Crew> itemLongClickListener;
    private ItemClickListener<Crew> itemClickListener;

    public void setData(List<Crew> data, ItemLongClickListener<Crew> itemLongClickListener, ItemClickListener<Crew> itemClickListener) {
        this.data = data;
        this.itemLongClickListener = itemLongClickListener;
        this.itemClickListener = itemClickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CrewViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crew, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrewViewAdapter.ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        Crew crew = data.get(pos);
        holder.tvCrewName.setText(crew.getCrew_name());
        holder.tvCrewDate.setText(crew.getCreated_date());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemLongClickListener.onItemLongClick(v, crew, pos);
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(crew, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCrewName, tvCrewDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCrewName = itemView.findViewById(R.id.tv_crew_name);
            tvCrewDate = itemView.findViewById(R.id.tv_crew_date);
        }
    }
}
