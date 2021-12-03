package com.example.easylearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.easylearn.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    final FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore database;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Your Account Creation is in Progress...");

        binding.createNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass, name, confirmPass;

                email = binding.regEmailBox.getText().toString().trim();
                pass = binding.passReg.getText().toString();
                name = binding.nameReg.getText().toString().trim();
                confirmPass = binding.confirmPass.getText().toString();

                if (name.isEmpty()){
                    binding.nameReg.setError("This field cannot be empty.");
                } else if (email.isEmpty()){
                    binding.regEmailBox.setError("This field cannot be empty.");
                }else if (pass.isEmpty() || pass.length()<6){
                    binding.passReg.setError("Password Length Should Exceed 6 Characters!");
                } else if (!pass.equals(confirmPass)){
                    binding.confirmPass.setError("Password Does Not Match!");
                } else {
                    final User user = new User(name, email, pass);
                    dialog.show();

                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String uid = task.getResult().getUser().getUid();
                                database
                                        .collection("users")
                                        .document(uid)
                                        .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
//                                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if (task.isSuccessful()){
//                                                        dialog.dismiss();
//                                                        Toast.makeText(getApplicationContext(), "Verification Email Sent Successfully. PLease Verify your Email.", Toast.LENGTH_SHORT).show();
//                                                    } else {
//                                                        Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                            });
                                            binding.nameReg.setText("");
                                            binding.regEmailBox.setText("");
                                            binding.passReg.setText("");
                                            binding.confirmPass.setText("");
                                            dialog.dismiss();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                        });
                            } else {
                                dialog.dismiss();
                                Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void onLoginRegisterActivityClick(View view){
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.stay);
        finish();
    }
}