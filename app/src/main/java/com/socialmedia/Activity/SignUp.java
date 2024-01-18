package com.socialmedia.Activity;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.socialmedia.Model.Users;
import com.socialmedia.R;


public class SignUp extends AppCompatActivity {

    private TextInputEditText Username, Bio, EmailTxt, PasswordTxt, ConPassword;
  private  MaterialButton btn;
private TextView SignBtn;
    private FirebaseDatabase mRootRef;
    private FirebaseAuth mAuth;


    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Username = findViewById(R.id.signup_username_textInputEditText);
       Bio = findViewById(R.id.signup_fullname_textInputEditText);
        EmailTxt = findViewById(R.id.signup_Email);
        PasswordTxt = findViewById(R.id.signup_password);

        SignBtn= findViewById(R.id.signup_textView);
        btn = findViewById(R.id.SignUp_Button);

        mRootRef = FirebaseDatabase.getInstance( );
        mAuth = FirebaseAuth.getInstance( );
        pd = new ProgressDialog(this);
SignBtn.setOnClickListener(new View.OnClickListener( ) {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SignUp.this , LogIn.class);
        startActivity(intent);

    }
});
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = EmailTxt.getText().toString();
                String password = PasswordTxt.getText().toString();


               mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>( ) {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()) {
                          Users user = new Users(Username.getText().toString(), Bio.getText().toString(),email,password);
                     String id= task.getResult().getUser().getUid();
                          mRootRef.getReference().child("Users").child(id).setValue(user);

                          Intent intent = new Intent( SignUp.this, LogIn.class );
                          Toast.makeText(SignUp.this, "Data Saved", Toast.LENGTH_SHORT).show();

                         startActivity(intent);
                      }
                      else{
                          Toast.makeText(SignUp.this, "Data not Saved", Toast.LENGTH_SHORT).show();

                      }
                   }
               });
            }
        });
    }


}




