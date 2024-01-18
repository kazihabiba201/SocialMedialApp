package com.socialmedia.Activity;

import static com.socialmedia.R.id.Inbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.socialmedia.Adapter.UserAdapter;
import com.socialmedia.Model.Users;
import com.socialmedia.R;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ArrayList<Users> list = new ArrayList<>(  );
    FirebaseDatabase database;
    RecyclerView chatRecyclerView;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        database= FirebaseDatabase.getInstance();
        chatRecyclerView= findViewById(R.id.chatRecyclerView);


        adapter = new UserAdapter(ChatActivity.this, getBaseContext(), list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(adapter);
        database.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    if (!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())){
                        list.add(users);
                    }

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
    bottomNavigationView.setSelectedItemId(Inbox);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener( ) {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home:
                        startActivities(new Intent[]{new Intent(getApplicationContext( ), Home.class)});
                        overridePendingTransition(0,0);
                        return true;

                    case Inbox:
                       return true;

                    case R.id.Page:
                        startActivities(new Intent[]{new Intent(getApplicationContext(), LogIn.class)});
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

}