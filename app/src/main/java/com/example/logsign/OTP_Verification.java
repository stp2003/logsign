package com.example.logsign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTP_Verification extends AppCompatActivity {

    private OTP_Verification binding;
    private String verificationId;
    Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OTP_Verification.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editTextInput();

        binding.tvMobile.setText(String.format(
                "+91-%s", getIntent().getStringExtra("phone")
        ));

        verificationId = getIntent().getStringExtra("verificationId");

        binding.tvResendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( OTP_Verification.this, "OTP Send Successfully.", Toast.LENGTH_SHORT).show();
            }
        });

        btnVerify = findViewById( R.id.btnVerify );

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBarVerify.setVisibility(View.VISIBLE);
                binding.btnVerify.setVisibility(View.INVISIBLE);
                if (binding.etC1.getText().toString().trim().isEmpty() ||
                        binding.etC2.getText().toString().trim().isEmpty() ||
                        binding.etC3.getText().toString().trim().isEmpty() ||
                        binding.etC4.getText().toString().trim().isEmpty() ||
                        binding.etC5.getText().toString().trim().isEmpty() ||
                        binding.etC6.getText().toString().trim().isEmpty()) {
                    Toast.makeText( OTP_Verification.this, "OTP is not Valid!", Toast.LENGTH_SHORT).show();
                } else {
                    if (verificationId != null) {
                        String code = binding.etC1.getText().toString().trim() +
                                binding.etC2.getText().toString().trim() +
                                binding.etC3.getText().toString().trim() +
                                binding.etC4.getText().toString().trim() +
                                binding.etC5.getText().toString().trim() +
                                binding.etC6.getText().toString().trim();

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                        FirebaseAuth
                                .getInstance()
                                .signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            binding.progressBarVerify.setVisibility(View.VISIBLE);
                                            binding.btnVerify.setVisibility(View.INVISIBLE);
                                            Toast.makeText( OTP_Verification.this, "Welcome...", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(OTP_Verification.this, SignUp.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } else {
                                            binding.progressBarVerify.setVisibility(View.GONE);
                                            binding.btnVerify.setVisibility(View.VISIBLE);
                                            Toast.makeText( OTP_Verification.this, "OTP is not Valid!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    private void editTextInput() {
        binding.etC1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etC2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etC3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etC4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etC5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}

