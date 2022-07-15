package com.example.logsign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SignUp extends AppCompatActivity {

    TextView alreadyHaveAccount , otpVerification;
    EditText inputEmail , inputPassword , inputConfirmPassword;
    Button btnRegister , changeProfile;

    ImageView profileImage;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN );


        alreadyHaveAccount = findViewById( R.id.alreadyHaveAccount );

        inputEmail = findViewById( R.id.inputEmail );
        inputPassword = findViewById( R.id.inputPassword );
        inputConfirmPassword = findViewById( R.id.inputConfirmPassword );
        btnRegister = findViewById(R.id.btnSignUp);

        profileImage = findViewById( R.id.profileImage );
        changeProfile = findViewById( R.id.changeProfile );

        otpVerification = findViewById( R.id.otpVerification );


        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child( "users/" + mAuth.getCurrentUser().getUid() + "profile.jpg" );


        otpVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( SignUp.this , OtpSendActivity.class ));
            }
        });

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                 Picasso.get().load( uri ).into( profileImage );
            }
        });


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


        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent( Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                startActivityForResult( openGallery , 1000);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == 1000)  {
            if( resultCode == Activity.RESULT_OK)  {
                assert data != null;
                Uri imageUri = data.getData();
//                profileImage.setImageURI( imageUri );

                uploadImageToFirebase( imageUri );

            }
        }
    }


    private void uploadImageToFirebase( Uri imageUri ) {
        final StorageReference fileRef = storageReference.child( "users/" + mAuth.getCurrentUser().getUid() + "profile.jpg" );
        fileRef.putFile( imageUri ).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load( uri ).into( profileImage );
                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(SignUp.this, "", Toast.LENGTH_SHORT).show();
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


