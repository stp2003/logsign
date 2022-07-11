package com.example.logsign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    TextView alreadyHaveAccount;
    EditText inputEmail , inputPassword , inputConfirmPassword;
    Button btnRegister;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN );


        alreadyHaveAccount = findViewById( R.id.alreadyHaveAccount );

        inputEmail = findViewById( R.id.inputEmail );
        inputPassword = findViewById( R.id.inputPassword );
        inputConfirmPassword = findViewById( R.id.inputConfirmPassword );
        btnRegister = findViewById(R.id.btnLogin);


        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        alreadyHaveAccount.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( SignUp.this , MainActivity.class ) );
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuth();
            }
        });

    }

    private void PerformAuth()  {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPassword.getText().toString();

        if ( !email.matches( emailPattern ))  {
            inputEmail.setError( "Enter Correct Email!!" );
        }

        else if ( password.isEmpty() || password.length() < 6)  {
            inputPassword.setError( "Enter proper Password!" );
        }

        else if ( !password.equals( confirmPassword ))  {
            inputConfirmPassword.setError( "Enter same Password!" );
        }

        else  {
            progressDialog.setMessage( "Please wait while Registration..." );
            progressDialog.setTitle( "Registration" );
            progressDialog.setCanceledOnTouchOutside( false );
            progressDialog.show();


            mAuth.createUserWithEmailAndPassword( email , password ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if ( task.isSuccessful() )  {
                        progressDialog.dismiss();

                        sendUserToNextActivity();

                        Toast.makeText(SignUp.this , "SignUp Successful" , Toast.LENGTH_SHORT).show();
                    }

                    else   {
                        progressDialog.dismiss();
                        Toast.makeText( SignUp.this , "" + task.getException() , Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }

    }


    private void sendUserToNextActivity() {
        Intent intent = new Intent( SignUp.this , HomeActivity.class );
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity( intent );

    }

}


