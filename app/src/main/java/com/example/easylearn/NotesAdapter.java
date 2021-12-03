package com.example.easylearn;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    Context context;
    ArrayList<Notes_Model> notesModel;

    public NotesAdapter(Context context, ArrayList<Notes_Model> notesModel) {
        this.context = context;
        this.notesModel = notesModel;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_notes, parent,false);
        return new NotesAdapter.NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Notes_Model model = notesModel.get(position);
        holder.textView.setText(model.getTopicName());
        holder.textView1.setText(model.getNotes());
    }

    @Override
    public int getItemCount() {
        return notesModel.size();
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        TextView textView1;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.topicsName);
            textView1 = itemView.findViewById(R.id.notes);
        }
    }
}
