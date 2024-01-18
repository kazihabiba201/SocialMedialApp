package com.socialmedia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.socialmedia.Activity.CommentActivity;
import com.socialmedia.Model.NotificationModel;
import com.socialmedia.Model.Users;
import com.socialmedia.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder> {

    ArrayList<NotificationModel>list;
    Context context;


    public NotificationAdapter(ArrayList<NotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new NotificationAdapter.viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        NotificationModel model= list.get(position);
        String time = TimeAgo.using(model.getNotificationAt());
        holder.Time.setText(time);
        String type = model.getType();
        FirebaseDatabase.getInstance().getReference().child("Users").child(model.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue( Users.class );
                        Picasso.get().load(users.getProfile())
                                .placeholder(R.drawable.profile_foreground)
                                .into(holder.profile_img);
                        if(type.equals("reacts")){
                            holder.Notification.setText(Html.fromHtml("<b>"+ users.getUsername( )+"</b>" + " Liked your post"));
                        }else{
                            holder.Notification.setText(Html.fromHtml("<b>"+ users.getUsername()+"</b>"+" commented on your post"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

holder.OpenNotification.setOnClickListener(new View.OnClickListener( ) {
    @Override
    public void onClick(View v) {
        FirebaseDatabase.getInstance().getReference().child("notification").child(model.getPostedBy()).child(model.getNotificationID())
                .child("checkOpen").setValue(true);
      holder.OpenNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
        Intent intent = new Intent( context, CommentActivity.class);
        intent.putExtra("postId", model.getPostID());
        intent.putExtra("postedBy", model.getPostedBy());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
});
Boolean checkOpen = model.isCheckOpen();
        if (( checkOpen == true )) {
            holder.OpenNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else{}
    }

    @Override
    public int getItemCount() {
        return list.size( );
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView profile_img;
        TextView Notification, Time;

     LinearLayout OpenNotification;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            profile_img= itemView.findViewById(R.id.notification_profile_image);
            Notification =itemView.findViewById(R.id.notification_text);
            Time =itemView.findViewById(R.id.notification_time);

            OpenNotification = itemView.findViewById(R.id.openNotification);
        }
    }
}
