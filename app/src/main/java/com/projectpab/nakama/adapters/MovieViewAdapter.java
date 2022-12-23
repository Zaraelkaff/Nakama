package com.projectpab.nakama.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectpab.nakama.R;
import com.projectpab.nakama.models.Movie;
import com.projectpab.nakama.utils.ItemClickListener;
import com.projectpab.nakama.utils.ItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewAdapter.ViewHolder> {
    private List<Movie> data = new ArrayList<>();
    private ItemLongClickListener<Movie> itemLongClickListener;
    private ItemClickListener<Movie> itemClickListener;

    public void setData(List<Movie> data, ItemLongClickListener<Movie> itemLongClickListener, ItemClickListener<Movie> itemClickListener) {
        this.data = data;
        this.itemLongClickListener = itemLongClickListener;
        this.itemClickListener = itemClickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewAdapter.ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        Movie movie = data.get(pos);
        holder.tvMovieName.setText(movie.getMovie_name());
        holder.tvMovieDate.setText(movie.getCreated_date());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemLongClickListener.onItemLongClick(v, movie, pos);
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(movie, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMovieName,tvMovieDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMovieName = itemView.findViewById(R.id.tv_movie_name);
            tvMovieDate = itemView.findViewById(R.id.tv_movie_date);
        }
    }
}
