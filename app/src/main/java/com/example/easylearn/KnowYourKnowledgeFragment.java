package com.example.easylearn;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easylearn.databinding.FragmentKnowYourKnowledgeBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class KnowYourKnowledgeFragment extends Fragment {

    public KnowYourKnowledgeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentKnowYourKnowledgeBinding binding;
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentKnowYourKnowledgeBinding.inflate(inflater, container, false);

        final ArrayList<Category_Model> categories = new ArrayList<>();
        final KnowYourKnowledgeAdapter adapter = new KnowYourKnowledgeAdapter(getContext(), categories);

        database.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        categories.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()){
                            Category_Model model = snapshot.toObject(Category_Model.class);
                            model.setCategoryId(snapshot.getId());
                            categories.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        binding.knowCategory.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.knowCategory.setAdapter(adapter);

        return binding.getRoot();
    }
}