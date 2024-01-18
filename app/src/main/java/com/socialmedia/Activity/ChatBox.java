package com.socialmedia.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.socialmedia.Adapter.MessageAdapter;
import com.socialmedia.Model.MessageModel;
import com.socialmedia.R;
import com.socialmedia.databinding.ActivityChatBoxBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatBox extends AppCompatActivity {
    ImageView chatBack, msgSend;
    CircleImageView chatPic;
    TextView chatUserName;
    SocialAutoCompleteTextView chatBox;
    RecyclerView chat_recyclerView;
    String senderUid;
    String receiverUid;
 String senderId;
    String senderRoom, receiverRoom;
ActivityChatBoxBinding binding;
FirebaseDatabase database;
FirebaseAuth auth;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ActivityChatBoxBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth= FirebaseAuth.getInstance();
        chatBack = findViewById(R.id.chatBack);
        msgSend = findViewById(R.id.msg_send);
        chatPic = findViewById(R.id.img_msg);
        chatUserName= findViewById(R.id.msg_profile_name);
        chatBox= findViewById(R.id.type_msg);
        chat_recyclerView= findViewById(R.id.chat_recyclerView);
        storage = FirebaseStorage.getInstance();


        final String senderId = auth.getUid();
        String recieverId = getIntent().getStringExtra("userId");
String username = getIntent().getStringExtra("username");
String  profile = getIntent().getStringExtra("profile");
binding.msgProfileName.setText(username);
        Picasso.get().load(profile).placeholder(R.drawable.profile_foreground).into(binding.imgMsg);
binding.chatBack.setOnClickListener(new View.OnClickListener( ) {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent( ChatBox.this, ChatActivity.class );
        startActivity(intent);
    }
});


final ArrayList<MessageModel>msgModel= new ArrayList<>(  );
final MessageAdapter chatAdapter = new MessageAdapter(msgModel, this);
chat_recyclerView.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chat_recyclerView.setLayoutManager(layoutManager);

        final String senderRoom = senderId + recieverId;
        final  String receiverRoom = recieverId + senderId;

        database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener( ) {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msgModel.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    MessageModel messageModel= snapshot1.getValue( MessageModel.class );
                    msgModel.add(messageModel);
                }
chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




                msgSend.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Date date = new Date( );
          final   String message  = chatBox.getText().toString();
         final MessageModel messageModel = new MessageModel(senderId,message );
                messageModel.setTimestamp(new Date().getTime());
         chatBox.setText("");

         database.getReference().child("chats").child(senderRoom).push()
                 .setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>( ) {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 database.getReference().child("chats").child(receiverRoom)
                         .push().setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>( ) {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {

                     }
                 });
             }
         });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==25){
            if(data!=null){
                if(data.getData()!=null){
                    Uri selectedImage = data.getData();
                    Calendar calendar = Calendar.getInstance();

                    final StorageReference  reference= storage.getReference().child("post").child(FirebaseAuth.getInstance( ).getUid( )).child(new Date(  ).getTime()+"");
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull  Task<UploadTask.TaskSnapshot> task) {

                            if(task.isSuccessful()){
                                final Task<Uri> uriTask = reference.getDownloadUrl( ).addOnSuccessListener(new OnSuccessListener<Uri>( ) {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filePath = uri.toString( );

                                        String message = chatBox.getText( ).toString( );
                                        Date date = new Date( );
                                        MessageModel messages = new MessageModel(message, senderId, date.getTime( ));
                                        messages.setImageUrl(filePath);
                                        chatBox.setText("");



                                        Toast.makeText(ChatBox.this, filePath, Toast.LENGTH_SHORT).show( );
                                    }
                                });
                            }



                }});}}}


}}