package com.socialmedia.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.socialmedia.Adapter.NotificationAdapter;
import com.socialmedia.Model.NotificationModel;
import com.socialmedia.R;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<NotificationModel>list;
FirebaseDatabase database;
CardView OpenNotification;
ImageView notificationBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        database = FirebaseDatabase.getInstance();
        recyclerView =findViewById(R.id.NotificationsRv);


        list= new ArrayList<>(  );

         notificationBack= findViewById(R.id.notificationBack);
         notificationBack.setOnClickListener(new View.OnClickListener( ) {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent( NotificationActivity.this, Home.class );
        startActivity(intent);
    }
});
        NotificationAdapter adapter= new NotificationAdapter(list, getBaseContext());
        LinearLayoutManager layoutManager1= new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        database.getReference().child("notification").child(FirebaseAuth.getInstance( ).getUid( ))
        .addValueEventListener(new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NotificationModel notificationModel = dataSnapshot.getValue(  NotificationModel.class);
                    notificationModel.setNotificationID(dataSnapshot.getKey());
                    list.add(notificationModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}