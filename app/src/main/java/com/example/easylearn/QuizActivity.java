package com.example.easylearn;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easylearn.databinding.ActivityQuizBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;
    ArrayList<Question> questions;
    public Question question;
    int index = 0;
    public int correct = 0;
    CountDownTimer timer;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        questions = new ArrayList<>();

        String cat_Id = getIntent().getStringExtra("catId");

        database = FirebaseFirestore.getInstance();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String catId = prefs.getString("categoryId", null);
        final String chapterId =prefs.getString("chapterId", null);

        Random random = new Random();
        int rand = random.nextInt(3);

        if (cat_Id==null) {
            database.collection("categories")
                    .document(catId)
                    .collection("chapters")
                    .document(chapterId)
                    .collection("questions")
                    .whereGreaterThanOrEqualTo("index", rand)
                    .orderBy("index")
                    .limit(2).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.getDocuments().size() < 2) {
                        database.collection("categories")
                                .document(catId)
                                .collection("chapters")
                                .document(chapterId)
                                .collection("questions")
                                .whereLessThanOrEqualTo("index", rand)
                                .orderBy("index")
                                .limit(2).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                    Question question = snapshot.toObject(Question.class);
                                    questions.add(question);
                                }
                                setNextQuestion();
                            }
                        });
                    } else {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Question question = snapshot.toObject(Question.class);
                            questions.add(question);
                        }
                        setNextQuestion();
                    }
                }
            });
        } else {
            database.collection("categories")
                    .document(cat_Id)
                    .collection("questions")
                    .whereGreaterThanOrEqualTo("index", rand)
                    .orderBy("index")
                    .limit(2).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.getDocuments().size() < 2) {
                        database.collection("categories")
                                .document(catId)
                                .collection("chapters")
                                .document(chapterId)
                                .collection("questions")
                                .whereLessThanOrEqualTo("index", rand)
                                .orderBy("index")
                                .limit(2).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                    Question question = snapshot.toObject(Question.class);
                                    questions.add(question);
                                }
                                setNextQuestion();
                            }
                        });
                    } else {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Question question = snapshot.toObject(Question.class);
                            questions.add(question);
                        }
                        setNextQuestion();
                    }
                }
            });
        }
        
        resetTimer();
        setNextQuestion();
    }

    void setNextQuestion() {
        if (timer != null) {
            timer.cancel();
        }
        timer.start();
        if (index < questions.size()) {
            binding.questionCounter.setText(String.format("%d/%d", (index+1), questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());
        }
    }

    void checkAnswer(TextView textView){
        String selectedAnswer = textView.getText().toString();
        textView.setBackground(getResources().getDrawable(R.drawable.options_selected));
        if (selectedAnswer.equals(question.getAnswer())){
            textView.setBackground(getResources().getDrawable(R.drawable.correct_options_selected));
            correct++;
        } else {
            textView.setBackground(getResources().getDrawable(R.drawable.wrong_options_selected));
        }
    }

    void reset(){
        binding.option1.setBackground(getResources().getDrawable(R.drawable.options_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.options_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.options_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.options_unselected));
    }

    void resetTimer(){
        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                if (questions.size() > index) {
                    setNextQuestion();
                } else {
                    startActivity(new Intent(getApplicationContext(), ResultActivity.class));
                    finish();
                }
            }
        };
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                if (timer != null){
                    timer.cancel();
                }
                TextView selected = (TextView) view;
                checkAnswer(selected);
                break;
            case R.id.nextButton:
                reset();
                index++;
                setNextQuestion();
                if (questions.size()>=index){
                    Button next = (Button) view;
                    buttonDisable(next);
                }
                break;
            case R.id.submitButton:
                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                intent.putExtra("correct", correct);
                intent.putExtra("total", questions.size());
                startActivity(intent);
                finish();
        }
    }

    void buttonDisable(Button button){
        button.setVisibility(View.INVISIBLE);
    }
}