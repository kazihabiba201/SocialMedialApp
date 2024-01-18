package com.socialmedia.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.socialmedia.Model.CommentModel;
import com.socialmedia.Model.Users;
import com.socialmedia.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewHolder>  {

Context context;

    public CommentAdapter(Context context, ArrayList<CommentModel> models) {
        this.context = context;
        this.models = models;
    }

    ArrayList<CommentModel>models;
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
CommentModel  commentModel =models.get(position);
        String time = TimeAgo.using(commentModel.getCommentedAt());
holder.cmtTime.setText(time);
        FirebaseDatabase.getInstance().getReference()
                .child("Users").child(commentModel.getCommentedBy()).addListenerForSingleValueEvent(new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                Picasso.get().load(user.getProfile()).placeholder(R.drawable.profile_foreground)
                        .into(holder.cmtImg);
                holder.cmtTxt.setText(Html.fromHtml("<b>" +user.getUsername() + "</b>" +"   "+ commentModel.getCommentRv()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class viewHolder extends  RecyclerView.ViewHolder{
ImageView cmtImg;
TextView cmtTxt, cmtTime;

    public viewHolder(@NonNull View itemView) {
        super(itemView);
        cmtImg = itemView.findViewById(R.id.Comment_img_search);
        cmtTxt = itemView.findViewById(R.id.CommentUsernameSearch);
        cmtTime= itemView.findViewById(R.id.CommentSearch);

    }
}

    }

