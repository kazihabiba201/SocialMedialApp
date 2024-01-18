package com.socialmedia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.socialmedia.Model.PostModel;
import com.socialmedia.Model.Users;
import com.socialmedia.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.viewHolder> {

        ArrayList<PostModel> list;
        Context context;


public SaveAdapter(ArrayList<PostModel> list, Context context) {
        this.list = list;
        this.context = context;
        }

@NonNull
@Override
public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new SaveAdapter.viewHolder(view);

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
        }

@Override
public int getItemCount() {
        return list.size( );
        }

class viewHolder extends RecyclerView.ViewHolder{
    ImageView post_image;
    TextView like, comment, share;
    CircleImageView profile_image;
    TextView profile_post_name, profile_post_text;

    LinearLayout OpenNotification;
    public viewHolder(@NonNull View itemView) {
        super(itemView);

        post_image= itemView.findViewById(R.id.post_image);
        profile_image= itemView.findViewById(R.id.profile_image);
        profile_post_name = itemView.findViewById(R.id.post_profile_name);
        profile_post_text = itemView.findViewById(R.id.post_text);
        like= itemView.findViewById(R.id.like);
        comment= itemView.findViewById(R.id.comment);
    }}}
