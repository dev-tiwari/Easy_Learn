package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.easylearn.databinding.ActivityChaptersBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class ChaptersActivity extends AppCompatActivity {

    ActivityChaptersBinding binding;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChaptersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();

        final String catId = getIntent().getStringExtra("catId");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("categoryId", catId);
        editor.commit();

        final ArrayList<Chapters_Model> chapters = new ArrayList<>();

        final ChapterAdapter adapter = new ChapterAdapter(getApplicationContext(), chapters);

        database
                .collection("categories")
                .document(catId)
                .collection("chapters")
                .orderBy("index")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                chapters.clear();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                    Chapters_Model model = snapshot.toObject(Chapters_Model.class);
                    model.setChapterId(snapshot.getId());
                    chapters.add(model);
                }
                adapter.notifyDataSetChanged();
            }
        });

        binding.chapterList.setAdapter(adapter);
        binding.chapterList.setLayoutManager(new LinearLayoutManager(this));

    }
}