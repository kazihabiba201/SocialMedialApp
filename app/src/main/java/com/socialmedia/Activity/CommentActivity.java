package com.socialmedia.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.socialmedia.Adapter.CommentAdapter;
import com.socialmedia.Model.CommentModel;
import com.socialmedia.Model.NotificationModel;
import com.socialmedia.Model.PostModel;
import com.socialmedia.Model.Users;
import com.socialmedia.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {
    ImageView comment_send, close, Comment_post_image;
    RecyclerView recyclerView;
    TextView counter , Comment_post_profile_name,Comment_post_text;
    SocialAutoCompleteTextView type;
    CircleImageView Comment_profile_image;
    Intent intent;
    String postId;
    String postedBy;
    FirebaseDatabase database;
    FirebaseAuth auth;
ArrayList<CommentModel>list= new ArrayList<>(  );
    Context context;
    CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Comment_post_text = findViewById(R.id.Comment_post_text);
        Comment_post_profile_name= findViewById(R.id.Comment_post_profile_name);
        comment_send = findViewById(R.id.comment_send);
        close = findViewById(R.id.comment_close);
        Comment_post_image= findViewById(R.id.Comment_post_image);
        type = findViewById(R.id.type_comment);
        recyclerView = findViewById(R.id.comment_RV);
        Comment_profile_image = findViewById(R.id.Comment_profile_image);
        intent = getIntent( );
        database = FirebaseDatabase.getInstance( );
        postId = intent.getStringExtra("postId");
        postedBy = intent.getStringExtra("postedBy");
        counter = findViewById(R.id.no_comment);

        database.getReference( ).child("posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostModel postModel = snapshot.getValue(PostModel.class);
                counter.setText(postModel.getCommentCount( ) + "");
                Picasso.get().load(postModel.getPostImage())
                        .placeholder(R.drawable.profile_foreground)
                        .into(Comment_post_image);
                Comment_post_text.setText(postModel.getPostUpdate());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.getReference().child("Users")
                .child(postedBy).addListenerForSingleValueEvent(new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users= snapshot.getValue( Users.class );
                Picasso.get().load(users.getProfile()).placeholder(R.drawable.profile_foreground)
                        .into(Comment_profile_image);
                Comment_post_profile_name.setText(users.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        comment_send.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {

                CommentModel commentModel = new CommentModel( );
                commentModel.setCommentRv(type.getText( ).toString( ));
                commentModel.setCommentedAt(new Date( ).getTime( ));
                commentModel.setCommentedBy(FirebaseAuth.getInstance( ).getUid( ));
                database.getReference( ).child("posts").child(postId).child("comments").push( ).setValue(commentModel).addOnCompleteListener(new OnCompleteListener<Void>( ) {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference( ).child("posts").child(postId).child("commentCount").addListenerForSingleValueEvent(new ValueEventListener( ) {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int commentCount = 0;
                                if (snapshot.exists( )) {
                                    commentCount = snapshot.getValue(Integer.class);
                                }
                                database.getReference( ).child("posts").child(postId).child("commentCount").setValue(commentCount + 1).addOnCompleteListener(new OnCompleteListener<Void>( ) {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        type.setText("");
                                        Toast.makeText(CommentActivity.this, "Commented", Toast.LENGTH_SHORT).show( );

                                        NotificationModel notificationModel = new NotificationModel();
                                        notificationModel.setNotificationBy(FirebaseAuth.getInstance( ).getUid( ));
                                        notificationModel.setNotificationAt(new Date().getTime());
                                        notificationModel.setPostID(postId);
                                        notificationModel.setPostedBy(postedBy);
                                        notificationModel.setType("comments");
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("notification")
                                                .child(postedBy)
                                                .push()
                                                .setValue(notificationModel);

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
        });
      commentAdapter = new CommentAdapter(this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

       recyclerView.setAdapter(commentAdapter);
       database.getReference().child("posts").child(postId).child("comments").addValueEventListener(new ValueEventListener( ) {
           @SuppressLint("NotifyDataSetChanged")
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               list.clear();
               for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                   CommentModel commentModel = dataSnapshot.getValue( CommentModel.class );
                   list.add(commentModel);
               }
               commentAdapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
       close.setOnClickListener(new View.OnClickListener( ) {
           @Override
           public void onClick(View v) {
               Intent intent =  new Intent( CommentActivity.this, Home.class );
                       startActivity(intent);
           }
       });

    }
}