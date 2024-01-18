package com.socialmedia.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.socialmedia.Activity.ChatBox;
import com.socialmedia.Model.Users;
import com.socialmedia.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends  RecyclerView.Adapter<UserAdapter.viewHolder>{
Activity activity;



    Context context;
    ArrayList<Users>lists;
    View view;

    public UserAdapter(Activity activity, Context context, ArrayList<Users> lists) {
        this.activity = activity;
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      view = LayoutInflater.from(context).inflate(R.layout.chat_list,parent,false);
        return  new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Users users = lists.get(position);

        Picasso.get().load(users.getProfile()).placeholder(R.drawable.profile_foreground).into(holder.chatImg);
        holder.chatName.setText(users.getUsername());
        FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance( ).getUid( )+ users.getUserId())
                .orderByChild("timestamp").limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            for (DataSnapshot snapshot1: snapshot.getChildren()){
                                holder.Msg.setText(snapshot1.child("message").getValue( ).toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.itemView.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent( activity, ChatBox.class);
                intent.putExtra("userId", users.getUserId());
                intent.putExtra("profile", users.getProfile());
                intent.putExtra("username", users.getUsername());

                    activity.startActivity(intent);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);}

        });





    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        public TextView username, chatName, Msg;
        public  TextView bio;
        public CircleImageView image_profile, FollowImg, chatImg;
        public AppCompatButton follow_btn;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            chatName = itemView.findViewById(R.id.Chat_username);
         Msg = itemView.findViewById(R.id.Chat_text);
         chatImg = itemView.findViewById(R.id.chat_img);


        }
    }
    }







