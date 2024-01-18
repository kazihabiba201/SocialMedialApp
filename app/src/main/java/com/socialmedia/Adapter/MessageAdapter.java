package com.socialmedia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.socialmedia.Model.MessageModel;
import com.socialmedia.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {
    ArrayList<MessageModel>messageModels;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public MessageAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    Context context;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       if(viewType== SENDER_VIEW_TYPE){
           View view = LayoutInflater.from(context).inflate(R.layout.chat_text_sender, parent, false);
           return new SenderViewHolder(view);
       }else {
           View view = LayoutInflater.from(context).inflate(R.layout.chat_text_reciever, parent, false);
           return new RecieveViewHolder(view);
       }

    }

    @Override
    public int getItemViewType(int position) {
        if((messageModels).get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return  SENDER_VIEW_TYPE;
        }else {
            return  RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
MessageModel messageModel = messageModels.get(position);


if(holder.getClass()== SenderViewHolder.class){
    ((SenderViewHolder)holder).senderMsg.setText(messageModel.getMessage());
    if (messageModel.getMessage( ).equals("photo")) {

        ((SenderViewHolder) holder).chat_img.setVisibility(View.VISIBLE);
       ( (SenderViewHolder) holder ).chat_text.setVisibility(View.GONE);


        Glide.with(context)
                .load(messageModel.getImageUrl( ))
                .placeholder(R.drawable.profile_foreground)
                .into(((SenderViewHolder) holder).chat_img);
    }
}else {
    ( (RecieveViewHolder) holder ).receiverMsg.setText(messageModel.getMessage( ));

}
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class RecieveViewHolder extends RecyclerView.ViewHolder{
TextView receiverMsg, receiverTime;

        public RecieveViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMsg = itemView.findViewById(R.id.receiverMsg);


        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg, senderTime, chat_text;
        ImageView chat_img;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsg = itemView.findViewById(R.id.senderMsg);

            chat_img = itemView.findViewById(R.id.chat_img);
            chat_text = itemView.findViewById(R.id.chay_message);
        }
    }
}
