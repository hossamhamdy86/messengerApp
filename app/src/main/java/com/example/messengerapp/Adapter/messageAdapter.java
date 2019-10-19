package com.example.messengerapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messengerapp.R;
import com.example.messengerapp.messageActivity;
import com.example.messengerapp.model.chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.user_item_viewHolder>{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    ArrayList<chat> chat_ArrayList;
    Context context;
    String sender_imageUrl;
    String receiver_imageUrl;
    FirebaseUser fuser ;

    public messageAdapter(Context context, ArrayList<chat> chat_ArrayList , String sender_imageUrl , String receiver_imageUrl){
        this.chat_ArrayList = chat_ArrayList ;
        this.context = context;
        this.sender_imageUrl = sender_imageUrl;
        this.receiver_imageUrl = receiver_imageUrl;
    }

    @NonNull
    @Override
    public messageAdapter.user_item_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.chat_item_right, parent, false);
            return new messageAdapter.user_item_viewHolder(view);
        }else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.chat_item_left, parent, false);
            return new messageAdapter.user_item_viewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull messageAdapter.user_item_viewHolder holder, final int position) {

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        chat chat = chat_ArrayList.get(position);
        holder.show_message.setText(chat.getMessage());

        if (!sender_imageUrl.isEmpty()&& chat.getSender().equals(fuser.getUid())){
            Picasso.with(context).load(sender_imageUrl).into(holder.profile_image_chat);
        }else if (sender_imageUrl.isEmpty()){
            holder.profile_image_chat.setImageResource(R.drawable.ic_account_circle);
        }else {
            Picasso.with(context).load(receiver_imageUrl).into(holder.profile_image_chat);
        }
    }

    @Override
    public int getItemCount() {
        return chat_ArrayList.size();
    }

    class user_item_viewHolder extends RecyclerView.ViewHolder{

        CircleImageView profile_image_chat;
        TextView show_message;

        public user_item_viewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image_chat = itemView.findViewById(R.id.profile_image_chat);
            show_message = itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (chat_ArrayList.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
