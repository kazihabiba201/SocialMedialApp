package com.socialmedia.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.socialmedia.Model.PostModel;
import com.socialmedia.Model.Users;
import com.socialmedia.R;
import com.squareup.picasso.Picasso;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreatePost extends AppCompatActivity {

    Button postButton;
    TextView postName, PostBio;
    SocialAutoCompleteTextView PostUpdate;
    ImageView postImage, UpdateImg;
    CircleImageView postProfile;
    Uri uri;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        postButton = findViewById(R.id.postButton);
        postName= findViewById(R.id.postUser);
        PostBio = findViewById(R.id.postLocation);
        postImage= findViewById(R.id.postImageButton);
        postProfile =findViewById(R.id.postProfile);
        PostUpdate= findViewById(R.id.postUpdate);
        UpdateImg= findViewById(R.id.postImage);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage= FirebaseStorage.getInstance();
        database.getReference().child("Users").child(FirebaseAuth.getInstance( ).getUid( )).addListenerForSingleValueEvent(new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                  Users user = snapshot.getValue( Users.class );
                    Picasso.get().load(user.getProfile()).placeholder(R.drawable.profile_foreground).into(postProfile);
                    postName.setText(user.getUsername());
                    PostBio.setText(user.getBio());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        PostUpdate.addTextChangedListener(new TextWatcher( ) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
String postUpdate =PostUpdate.getText().toString();
if(!postUpdate.isEmpty()){
    postButton.setBackgroundColor(getBaseContext().getResources().getColor(R.color.sky));
    postButton.setTextColor(getBaseContext().getResources().getColor(R.color.white));
    postButton.setEnabled(true);
}else{
    postButton.setBackgroundColor(getBaseContext().getResources().getColor(R.color.grey));
    postButton.setTextColor(getBaseContext().getResources().getColor(R.color.white));
    postButton.setEnabled(false);

}
postImage.setOnClickListener(new View.OnClickListener(){

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(  );
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 10);
    }
});
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
            uri = data.getData();
            UpdateImg.setImageURI(uri);
            UpdateImg.setVisibility(View.VISIBLE);

            postButton.setBackgroundColor(getBaseContext().getResources().getColor(R.color.sky));
            postButton.setTextColor(getBaseContext().getResources().getColor(R.color.white));
            postButton.setEnabled(true);
        }
        postButton.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                final StorageReference reference = storage.getReference().child("posts").child(FirebaseAuth.getInstance( ).getUid( ))
                        .child(new Date(  ).getTime()+"");
          reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>( ) {
              @Override
              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>( ) {
                      @Override
                      public void onSuccess(Uri uri) {
                          PostModel postModel = new PostModel() ;
                          postModel.setPostImage(uri.toString());
                          postModel.setPostedBy(FirebaseAuth.getInstance().getUid());
                          postModel.setPostUpdate(PostUpdate.getText().toString());
                          postModel.setPostedAt(new Date(  ).getTime());

                          database.getReference().child("posts")
                                  .push()
                                  .setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>( ) {
                              @Override
                              public void onSuccess(Void unused) {
                                  Toast.makeText(getBaseContext(), "Posted Successfully", Toast.LENGTH_SHORT).show( );
                                  Intent intent = new Intent(CreatePost.this, Home.class);
                                  startActivity(intent);
                              }
                          });
                      }
                  });
              }
          });
            }
        });
    }
}