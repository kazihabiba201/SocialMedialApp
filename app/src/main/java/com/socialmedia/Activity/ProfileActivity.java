package com.socialmedia.Activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.socialmedia.Adapter.Post_Adapter;
import com.socialmedia.Model.PostModel;
import com.socialmedia.Model.Users;
import com.socialmedia.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    RecyclerView profilePostRecyclerView;
    ArrayList<PostModel>list;
    TextView saveItem;
    FirebaseDatabase database;
    FirebaseAuth auth;

    FirebaseStorage storage;
    ImageView ProfileCoverImage, profileImage, button3, button;
    CircleImageView ClickCover, clickProfilePic;

    Button  button2,  SignOut;

    private TextView UsernameTxt, BioTxt;
    ActivityResultLauncher<String>StoryLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage= findViewById(R.id.profile_image);
        clickProfilePic = findViewById(R.id.ProfileImageClick);
        button= findViewById(R.id.Back);
        button2= findViewById(R.id.Post);
    
        SignOut = findViewById(R.id.settingSignOut);



        // setSupportActionBar(toolbar);
        // ProfileActivity.this.setTitle("Profile");

        auth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        UsernameTxt = findViewById(R.id.profileUsername);
        BioTxt = findViewById(R.id.About);

        storage=FirebaseStorage.getInstance();


        button.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ProfileActivity.this, Home.class );
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ProfileActivity.this, CreatePost.class );
                startActivity(intent);
            }
        });

        SignOut.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent( ProfileActivity.this,LogIn.class );
                startActivity(intent);
            }
        });

        list= new ArrayList<>(  );


        profilePostRecyclerView = findViewById(R.id.SavePostRV);
        list = new ArrayList<>( );
        Post_Adapter postAdapter = new Post_Adapter(list, getBaseContext( ));
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getBaseContext( ));
        profilePostRecyclerView.setLayoutManager(layoutManager1);
        profilePostRecyclerView.setNestedScrollingEnabled(false);
        profilePostRecyclerView.setAdapter(postAdapter);
        database.getReference( ).child("posts").addListenerForSingleValueEvent(new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear( );
                for (DataSnapshot dataSnapshot : snapshot.getChildren( )) {

                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    postModel.setPostId(dataSnapshot.getKey( ));
                    database.getReference("posts").child(auth.getCurrentUser().getUid());
                    if( postModel.getPostedBy().equals(FirebaseAuth.getInstance().getUid())){
                    list.add(postModel);}
                }
                postAdapter.notifyDataSetChanged( );
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


  database.getReference("Users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener( ) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            Users user;
                            user = snapshot.getValue(Users.class);

                            Picasso.get()
                                    .load(user.getProfile())
                                    .placeholder(R.drawable.profile_foreground)
                                    .into(profileImage);

                            UsernameTxt.setText(user.getUsername());
                            BioTxt.setText(user.getBio());




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        clickProfilePic.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(  );
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                startActivityForResult(intent,22);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 22) {
            if (data.getData( ) != null) ;
            Uri uri = data.getData( );
            profileImage.setImageURI(uri);

            final StorageReference reference = storage.getReference( ).child("profile_Image").child(FirebaseAuth.getInstance( ).getUid( ));
            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>( ) {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getBaseContext( ), "Sava Image Successfully", Toast.LENGTH_SHORT).show( );
                    reference.getDownloadUrl( ).addOnSuccessListener(new OnSuccessListener<Uri>( ) {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference( ).child("Users").child(auth.getUid( )).child("profile").setValue(uri.toString( ));
                        }
                    });
                }
            });

        }
    }
    }




