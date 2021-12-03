package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.easylearn.databinding.ActivityViewNotesBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewNotesActivity extends AppCompatActivity {

    ActivityViewNotesBinding binding;
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final String chapterId = getIntent().getStringExtra("chapterId");


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String catId = prefs.getString("categoryId", null);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("categoryId", catId);
        editor.putString("chapterId", chapterId);
        editor.commit();


        final ArrayList<Notes_Model> notes = new ArrayList<>();

        final NotesAdapter adapter = new NotesAdapter(getApplicationContext(), notes);

        database.collection("categories")
                .document(catId)
                .collection("chapters")
                .document(chapterId)
                .collection("notes")
                .orderBy("index")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                notes.clear();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                    Notes_Model model = snapshot.toObject(Notes_Model.class);
                    model.setNotesId(snapshot.getId());
                    notes.add(model);
                }
                adapter.notifyDataSetChanged();
            }
        });

        binding.startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QuizActivity.class));
                finish();
            }
        });

        binding.notesList.setAdapter(adapter);
        binding.notesList.setLayoutManager(new LinearLayoutManager(this));
    }
}