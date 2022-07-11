package com.example.logsign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Google_Sign_In extends MainActivity {

    private static final int RC_SIGN_IN = 101;
    GoogleSignInClient googleSignInClient;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        progressDialog = new ProgressDialog( this);
        progressDialog.setMessage( "Google SignIn...");
        progressDialog.show();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken( getString( R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        googleSignInClient = GoogleSignIn.getClient( this , googleSignInOptions );

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult( signInIntent , RC_SIGN_IN );

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN ) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
               GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle( account.getIdToken());
            }
            catch (ApiException e) {

                Toast.makeText(this, "" + e.getMessage() , Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            }

        }
    }


    private void firebaseAuthWithGoogle(String idToken)  {
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(Google_Sign_In.this, "" + task.getException() , Toast.LENGTH_SHORT).show();
                            finish();

                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent( Google_Sign_In.this , HomeActivity.class );
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity( intent );

    }

}

