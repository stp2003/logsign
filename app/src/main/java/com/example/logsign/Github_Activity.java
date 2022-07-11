package com.example.logsign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.OAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class Github_Activity extends AppCompatActivity {

    EditText inputEmail;
    Button btnLogin;

    FirebaseAuth mAuth;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN );


        inputEmail = findViewById( R.id.inputEmail );
        btnLogin = findViewById( R.id.btnLogin );

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                if ( !email.matches( emailPattern ))  {
                    Toast.makeText(Github_Activity.this, "Enter valid Email", Toast.LENGTH_SHORT).show();
                }
                else {
                    OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
                    // Target specific email with login hint.
                    provider.addCustomParameter("login", email );

                    // Request read access to a user's email addresses.
                   // This must be preconfigured in the app's API permissions.
                    List<String> scopes =
                            new ArrayList<String>() {
                                {
                                    add("user:email");
                                }
                            };
                    provider.setScopes(scopes);

                    Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
                    if (pendingResultTask != null) {
                        // There's something already here! Finish the sign-in for your user.
                        pendingResultTask
                                .addOnSuccessListener(
                                        new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                // User is signed in.
                                                // IdP data available in
                                                // authResult.getAdditionalUserInfo().getProfile().
                                                // The OAuth access token can also be retrieved:
                                                // authResult.getCredential().getAccessToken().
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Github_Activity.this, "" + e.getMessage() , Toast.LENGTH_SHORT).show();
                                            }
                                        });
                    } else {
                        // There's no pending result so you need to start the sign-in flow.
                        // See below.
                        mAuth
                                .startActivityForSignInWithProvider(/* activity= */ Github_Activity.this, provider.build())
                                .addOnSuccessListener(
                                        new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                               openNextActivity();
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Github_Activity.this, "" + e.getMessage() , Toast.LENGTH_SHORT).show();
                                            }
                                        });
                    }

                }
            }
        });

    }

    private void openNextActivity() {
        Intent intent = new Intent( Github_Activity.this , HomeActivity.class );
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity( intent );
        finish();
    }
}

