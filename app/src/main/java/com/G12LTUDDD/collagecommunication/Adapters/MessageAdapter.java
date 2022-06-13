package com.G12LTUDDD.collagecommunication.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.Models.AllMethods;
import com.G12LTUDDD.collagecommunication.Models.Message;
import com.G12LTUDDD.collagecommunication.R;
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
        View v = LayoutInflater.from(context).inflate(R.layout.chat_adapter,parent,false);
        return new MessageAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {
        Message message = messages.get(position);

        if(message.getName().equals(AllMethods.name)){
            holder.tvTitle.setText(message.getMessage());
            holder.tvTitle.setGravity(Gravity.CENTER);
            holder.tvTitle.setBackgroundResource(R.drawable.my_msg_back);
            holder.ll.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        else{
            holder.tvTitle.setText(message.getMessage());
            holder.tvTitle.setGravity(Gravity.CENTER);
            holder.tvTitle.setBackgroundResource(R.drawable.opo_msg_back);
            holder.ll.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageButton ibDelete;
        LinearLayout ll;

        public MessageAdapterViewHolder(View itemView){
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvChatMessage);
            ibDelete = (ImageButton) itemView.findViewById(R.id.btnChatDelete);
            ll = (LinearLayout) itemView.findViewById(R.id.llChatItems);

            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageDb.child(messages.get(getAdapterPosition()).getKey()).removeValue();
                }
            });
        }
    }
}
