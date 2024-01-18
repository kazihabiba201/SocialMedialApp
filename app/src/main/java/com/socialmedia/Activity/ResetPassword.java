package com.socialmedia.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.socialmedia.R;

public class ResetPassword extends AppCompatActivity {
TextInputEditText resetText;
MaterialButton ResetButton, ResetLogin;
private  String email;
private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        resetText = findViewById( R.id.admin_newpasword_username_textInputEditText);
        ResetButton= findViewById(R.id.SubmitLink);
        ResetLogin= findViewById(R.id.Reset);
        auth= FirebaseAuth.getInstance();
        ResetLogin.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ResetPassword.this, LogIn.class);
                startActivity(intent);
            }
        });
        ResetButton.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                valdateDate();
            }
        });
    }
    private void valdateDate(){
        email= resetText.getText().toString();
        if(email.isEmpty()){
            resetText.setError("Required");
        }else{
            forgetPass();
        }
    }

    private void forgetPass() {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>( ) {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                      if(task.isSuccessful()){
                          Toast.makeText(ResetPassword.this, "Check your Email", Toast.LENGTH_SHORT).show( );
                          startActivity(new Intent( ResetPassword.this, LogIn.class ));
                          finish();
                      }  else{
                          Toast.makeText(ResetPassword.this, "Error"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show( );
                      }
                    }
                });
    }
}