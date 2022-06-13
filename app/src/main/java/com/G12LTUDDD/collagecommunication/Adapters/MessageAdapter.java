package com.G12LTUDDD.collagecommunication.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.Models.Message;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {

    Context context;
    List<Message> messages;
    DatabaseReference messageDb;


    public MessageAdapter(Context context, List<Message> messages, DatabaseReference messageDb) {
        this.context = context;
        this.messages = messages;
        this.messageDb = messageDb;
    }




    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageButton isDelete;

        public MessageAdapterViewHolder(View itemView){
            super(itemView);
        }
    }
}
