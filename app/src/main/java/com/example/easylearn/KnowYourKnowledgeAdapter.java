package com.example.easylearn;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class KnowYourKnowledgeAdapter extends RecyclerView.Adapter<KnowYourKnowledgeAdapter.KnowYourKnowledgeViewHolder>{

    Context context;
    ArrayList<Category_Model> categoryModels;

    public KnowYourKnowledgeAdapter(Context context, ArrayList<Category_Model> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
    }

    @NonNull
    @Override
    public KnowYourKnowledgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, null);
        return new KnowYourKnowledgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KnowYourKnowledgeViewHolder holder, int position) {
        Category_Model model = categoryModels.get(position);
        holder.textView.setText(model.getCategoryName());
        Glide.with(context).load(model.getCategoryImage()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuizActivity.class);
                intent.putExtra("catId", model.getCategoryId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public static class KnowYourKnowledgeViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public KnowYourKnowledgeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.textView2);
        }
    }
}
