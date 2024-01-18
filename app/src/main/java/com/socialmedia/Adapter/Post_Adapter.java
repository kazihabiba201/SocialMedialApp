package com.socialmedia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.socialmedia.Activity.CommentActivity;
import com.socialmedia.Model.NotificationModel;
import com.socialmedia.Model.PostModel;
import com.socialmedia.Model.Users;
import com.socialmedia.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Post_Adapter extends  RecyclerView.Adapter<Post_Adapter.viewHolder> {

    ArrayList<PostModel>list;

    Context context;
FirebaseUser firebaseUser =FirebaseAuth.getInstance().getCurrentUser();
    public Post_Adapter(ArrayList<PostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        PostModel model= list.get(position);

        Picasso.get().load(model.getPostImage()).placeholder(R.drawable.profile_foreground).
        into(holder.post_image);
        holder.profile_post_text.setText(model.getPostUpdate());
        holder.like.setText(model.getReact()+"");


        holder.comment.setText(model.getCommentCount()+"");

        String postText = model.getPostUpdate();
        if(postText.equals("")){
            holder.profile_post_text.setVisibility(View.GONE);

        }else{
            holder.profile_post_text.setText(model.getPostUpdate());
            holder.profile_post_text.setVisibility(View.VISIBLE);
        }

        FirebaseDatabase.getInstance().getReference().child("Users").child(model.getPostedBy()).addValueEventListener(new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                     Users users = snapshot.getValue( Users.class ) ;
                     Picasso.get()
                             .load(users.getProfile())
                             .placeholder(R.drawable.profile_foreground).into(holder.profile_image);

                holder.profile_post_name.setText(users.getUsername( ));
           }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(model.getPostId())
                .child("reacts").child(FirebaseAuth.getInstance( ).getUid( )).addListenerForSingleValueEvent(new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_love, 0, 0, 0);

                }else{
                    holder.like.setOnClickListener(new View.OnClickListener( ) {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("posts").child(model.getPostId()).child("reacts").child(FirebaseAuth.getInstance( ).getUid( )).setValue(true)
                                    .addOnCompleteListener(new OnCompleteListener<Void>( ) {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("posts").child(model.getPostId())
                                                    .child("react").setValue(model.getReact() + 1).addOnCompleteListener(new OnCompleteListener<Void>( ) {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_love, 0, 0, 0);
                                                    NotificationModel notificationModel = new NotificationModel();
                                                    notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                    notificationModel.setNotificationAt(new Date().getTime());
                                                    notificationModel.setPostID(model.getPostId());
                                                    notificationModel.setPostedBy(model.getPostedBy());
                                                    notificationModel.setType("reacts");
                                                    FirebaseDatabase.getInstance().getReference().child("notification")
                                                            .child(model.getPostedBy()).push().setValue(notificationModel);

                                                }
                                            });
                                        }
                                    });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


holder.comment.setOnClickListener(new View.OnClickListener( ) {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context , CommentActivity.class);
        intent.putExtra("postId", model.getPostId());
        intent.putExtra("postedBy", model.getPostedBy());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
});

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class  viewHolder extends RecyclerView.ViewHolder{
        ImageView post_image;
        TextView like, comment, saveItem;
        CircleImageView profile_image;
        TextView profile_post_name, profile_post_text;


        public viewHolder(View view) {
            super(view);

            post_image= itemView.findViewById(R.id.post_image);
            profile_image= itemView.findViewById(R.id.profile_image);
            profile_post_name = itemView.findViewById(R.id.post_profile_name);
            profile_post_text = itemView.findViewById(R.id.post_text);
            like= itemView.findViewById(R.id.like);
            comment= itemView.findViewById(R.id.comment);



        }

    }

    }



