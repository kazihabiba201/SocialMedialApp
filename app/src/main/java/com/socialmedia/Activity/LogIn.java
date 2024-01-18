package com.socialmedia.Activity;



import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.socialmedia.Model.Users;
import com.socialmedia.R;

import java.util.Objects;


public class LogIn extends AppCompatActivity {
    private static final int RC_SIGN_IN = 45;
    private TextInputEditText  EmailTxt, PasswordTxt;
    public TextView gotoSignUp, forgetPassword;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    public MaterialButton btn;

FirebaseDatabase database;
    GoogleSignInClient googleSignInClient;
    MaterialButton google;
    MaterialButton facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        forgetPassword = findViewById(R.id.login_forgetPassword);

        mAuth = FirebaseAuth.getInstance( );
        EmailTxt = findViewById(R.id.login_username_textInputEditText);
        PasswordTxt = findViewById(R.id.login_password_textInputEditText);
        gotoSignUp= findViewById(R.id.Login_signUp);
        btn = findViewById(R.id.login_btn);
        currentUser = mAuth.getCurrentUser( );
        google = findViewById(R.id.google);
        database = FirebaseDatabase.getInstance();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        forgetPassword.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( LogIn.this, ResetPassword.class ));
            }
        });

        google.setOnClickListener(v -> signIn());
        gotoSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LogIn.this , SignUp.class);
            startActivity(intent);
        });
        btn.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {

                String email = EmailTxt.getText( ).toString( );
                String password = PasswordTxt.getText( ).toString( );
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>( ) {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful( )) {
                            Intent intent = new Intent(LogIn.this, ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LogIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show( );
                        }
                    }
                });

            }
        });



            }

    @Override
    protected void onStart() {
        super.onStart( );
        if(currentUser!=null){
            Intent intent = new Intent( LogIn.this,ProfileActivity.class );
            startActivity(intent);

        }
    }
    private void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());

            }catch(ApiException e){
                Log.w(TAG, "Google sign in failed", e);
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>( ) {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())  {

                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Users users = new Users();
                    users.setUserId(user.getUid());
                    users.setUsername(user.getDisplayName());
                    users.setProfile(Objects.requireNonNull(user.getPhotoUrl( )).toString());
                    database.getReference().child("Users").child(user.getUid()).setValue(users);
                    Intent intent = new Intent( LogIn.this, ProfileActivity.class);
                    startActivity(intent);
                    Toast.makeText(LogIn.this,  "Sign in with Google", Toast.LENGTH_SHORT ).show();
                }else{
                    Log.d(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(LogIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }}
        });
    }
}

