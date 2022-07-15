package com.example.logsign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Forget_Password extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;
    private ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailEditText = (EditText) findViewById( R.id.email );
        resetPasswordButton = (Button) findViewById( R.id.resetPassword );
        progressBar = (ProgressBar) findViewById( R.id.progressBar );

        mAuth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if ( email.isEmpty() )  {
            emailEditText.setError("Email is Required" );
            emailEditText.requestFocus();
            return;
        }

        if ( !Patterns.EMAIL_ADDRESS.matcher( email ).matches() )  {
            emailEditText.setError( "Please provide valid Email" );
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility( View.VISIBLE );
        mAuth.sendPasswordResetEmail( email ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                  if ( task.isSuccessful() ) {
                      Toast.makeText(Forget_Password.this, "Check your EMAIL to Reset your PassWord", Toast.LENGTH_LONG ).show();
                  }
                  else
                      Toast.makeText(Forget_Password.this, "Try Again! Something wrong happened", Toast.LENGTH_LONG).show();
            }
        });
    }
}

