package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ComponentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.easylearn.databinding.ActivityResultBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;

    public int correct;
    public int total;
    int POINTS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_result);

        correct = getIntent().getIntExtra("correct", 0);
        total = getIntent().getIntExtra("total", 0);

        int coins = correct * POINTS;

//        Toast.makeText(getApplicationContext(), correct, Toast.LENGTH_SHORT).show();

        binding.score.setText(String.format("%d/%d", correct, total));
        binding.earnedCoins.setText(String.valueOf(coins));

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(coins));
    }

    public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

    public void onRestartClick(View v) {
        startActivity(new Intent(getApplicationContext(), ChaptersActivity.class));
        finish();
    }
}