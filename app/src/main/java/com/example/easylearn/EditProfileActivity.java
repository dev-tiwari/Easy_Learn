package com.example.easylearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.easylearn.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    ProgressDialog dialog;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait for a while..");

        String id = auth.getCurrentUser().getUid();
        setContentView(binding.getRoot());

        database.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                binding.editName.setText(user.getName());
                binding.editEmail.setText(user.getEmail());
                binding.phoneEdit.setText(user.getPhoneNumber());
                if (user.getProfile()!=null) {
                    Glide.with(getApplicationContext())
                            .load(user.getProfile())
                            .into(binding.editProfileImage);
                }
            }
        });

//        binding.editSubmitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.show();
//                auth.getCurrentUser().updateEmail(binding.editEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        dialog.dismiss();
//                        if (task.isSuccessful()){
//                            Toast.makeText(getApplicationContext(), "Your Profile is Successfully Updated.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//                AuthCredential credential = EmailAuthProvider.getCredential(auth.getCurrentUser().getEmail().toString(), binding.currentPassword.getText().toString());
//                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        dialog.dismiss();
//                        if (task.isSuccessful()){
//                            auth.getCurrentUser().updateEmail(binding.editEmail.getText().toString().trim());
//                            User user = new User(binding.editName.getText().toString().trim(), binding.editEmail.getText().toString().trim(), binding.currentPassword.getText().toString());
//                            database.collection("users")
//                                    .document(id)
//                                    .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()){
//                                        Toast.makeText(getApplicationContext(), "Profile Successfully Updated.", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        }
//                    }
//                });
//            }
//        });
//        database.collection("users")
//                .document(id)
//                .set()
    }
}