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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView createNewAccount , forgotPassword;

    EditText inputEmail , inputPassword;
    Button btnLogin;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    ImageView btnGoogle;

    ImageView btnGithub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN );


        createNewAccount = findViewById( R.id.alreadyHaveAccount);

        inputEmail = findViewById( R.id.inputEmail );
        inputPassword = findViewById( R.id.inputPassword );
        btnLogin = findViewById(R.id.btnSignUp);

        btnGoogle = findViewById(R.id.btnGoogle);
        btnGithub = findViewById( R.id.btnGithub );

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        forgotPassword = ( TextView) findViewById( R.id.forgotPassword );

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( MainActivity.this , Forget_Password.class ));
            }
        });


        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( MainActivity.this , SignUp.class ) );
            }

        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });


        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent( MainActivity.this , Google_Sign_In.class );
                startActivity( intent );
            }
        });


        btnGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent( MainActivity.this , Github_Activity.class );
                intent.setFlags( Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity( intent );

            }
        });

    }


    private void performLogin() {

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (!email.matches(emailPattern)) {
            inputEmail.setError("Enter Correct Email!!");
        }
        else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Enter proper Password!");
        }
        else {
            progressDialog.setMessage("Please wait while Login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword( email , password ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if ( task.isSuccessful() )  {
                        progressDialog.dismiss();

                        sendUserToNextActivity();

                        Toast.makeText(MainActivity.this , "LOGIN Successful" , Toast.LENGTH_SHORT).show();
                    }

                    else   {
                        progressDialog.dismiss();
                        Toast.makeText( MainActivity.this , "" + task.getException() , Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }


    private void sendUserToNextActivity() {
        Intent intent = new Intent( MainActivity.this , HomeActivity.class );
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity( intent );

    }
}


