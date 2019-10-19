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
import com.example.messengerapp.model.Users_item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_user_item extends RecyclerView.Adapter<Adapter_user_item.user_item_viewHolder>{

    ArrayList<Users_item> usersItem_ArrayList;
    Context context;

    public Adapter_user_item(Context context, ArrayList<Users_item> usersItem_ArrayList){
        this.usersItem_ArrayList = usersItem_ArrayList ;
        this.context = context;
    }

    @NonNull
    @Override
    public user_item_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_item, parent, false);
        return new user_item_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull user_item_viewHolder holder, final int position) {
        final Users_item user = usersItem_ArrayList.get(position);
        holder.name_recycler.setText(usersItem_ArrayList.get(position).getName_recycler());
        holder.data_recycler.setText("Date");
        holder.last_message_recycler.setText("Last message.......");
        if (!usersItem_ArrayList.get(position).getImage_recycler().isEmpty()){
            Picasso.with(context).load(usersItem_ArrayList.get(position).getImage_recycler()).into(holder.image_recycler);
        }else{
            holder.image_recycler.setImageResource(R.drawable.ic_account_circle);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, messageActivity.class);
                intent.putExtra("name",user.getName_recycler());
                intent.putExtra("picture",user.getImage_recycler());
                intent.putExtra("Uid",user.getuId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersItem_ArrayList.size();
    }

    class user_item_viewHolder extends RecyclerView.ViewHolder{

         CircleImageView image_recycler;
         TextView name_recycler;
         TextView last_message_recycler;
         TextView data_recycler;

         public user_item_viewHolder(@NonNull View itemView) {
             super(itemView);
             image_recycler = itemView.findViewById(R.id.image_recycler);
             name_recycler = itemView.findViewById(R.id.name_recycler);
             last_message_recycler = itemView.findViewById(R.id.last_message_recycler);
             data_recycler = itemView.findViewById(R.id.data_recycler);
         }
     }
}
