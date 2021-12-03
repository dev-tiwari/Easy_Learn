package com.example.easylearn;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.easylearn.databinding.FragmentWalletBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WalletFragment extends Fragment {

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentWalletBinding binding;
    FirebaseFirestore database;
    User user;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentWalletBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(this.getContext());
        dialog.setMessage("Submitting Request...");

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                binding.currentCoins.setText(String.valueOf(user.getCoins()));
                binding.rupeesCalc.setText(String.valueOf(user.getCoins()/1000));
            }
        });

        binding.withdrawalSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getCoins() >= 75000){
                    String uid = FirebaseAuth.getInstance().getUid();
                    String paytm = binding.withdrawalPhoneBox.getText().toString();
                    if (paytm.isEmpty()){
                        binding.withdrawalPhoneBox.setError("This field cannot be empty.");
                    } else if (paytm.length()<10){
                        binding.withdrawalPhoneBox.setError("Phone number cannot be less than 10 digits.");
                    } else {
                        dialog.show();
                        WithdrawalRequest request = new WithdrawalRequest(uid, paytm, user.getName());
                        database.collection("withdrawal")
                                .document(FirebaseAuth.getInstance().getUid())
                                .set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Withdrawal Request Sent Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "More Coins Needed for Withdrawal.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }
}