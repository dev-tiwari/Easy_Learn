package com.example.easylearn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easylearn.databinding.RowLeaderboardsBinding;

import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    Context context;
    ArrayList<User> users;

    public LeaderboardAdapter(Context context, ArrayList<User> users){
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_leaderboards, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        User user = users.get(position);

        holder.binding.leaderboardName.setText(user.getName());
        holder.binding.leaderboardScore.setText(String.valueOf(user.getCoins()));
        holder.binding.leaderboardRank.setText(String.format("%d", (position+1)));

        Glide
                .with(context)
                .load(user.getProfile())
                .into(holder.binding.leaderboardImage);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder{

        RowLeaderboardsBinding binding;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowLeaderboardsBinding.bind(itemView);

        }
    }
}
