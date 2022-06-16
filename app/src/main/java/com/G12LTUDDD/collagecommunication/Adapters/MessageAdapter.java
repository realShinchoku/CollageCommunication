package com.G12LTUDDD.collagecommunication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.Models.Message;
import com.G12LTUDDD.collagecommunication.Models.User;
import com.G12LTUDDD.collagecommunication.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {

    Context context;
    List<Message> messages;
    FirebaseFirestore db;
    User u;

    public MessageAdapter(Context context, List<Message> messages, FirebaseFirestore db, User u) {
        this.context = context;
        this.messages = messages;
        this.db = db;
        this.u = u;
    }

    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.chat_adapter, parent, false);
        return new MessageAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.tvValue.setText(message.getValue());
        String time = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(message.getTime());
        holder.tvTime.setText(time);

        if (message.getUid().equals(u.getUid())) {
            holder.tvValue.setBackgroundResource(R.drawable.my_msg_back);
            holder.ll.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

            holder.tvValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.tvTime.getVisibility() == View.GONE) {
                        holder.tvTime.setVisibility(View.VISIBLE);
                        holder.ibDelete.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvTime.setVisibility(View.GONE);
                        holder.ibDelete.setVisibility(View.GONE);
                    }
                }
            });
            holder.ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("Messages").document(message.getKey()).delete();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView tvValue, tvTime;
        ImageButton ibDelete;
        LinearLayout ll;

        public MessageAdapterViewHolder(View itemView) {
            super(itemView);
            tvValue = itemView.findViewById(R.id.tvValueChatItem);
            tvTime = itemView.findViewById(R.id.tvTimeChatItem);
            ibDelete = itemView.findViewById(R.id.ibChatItem);
            ll = itemView.findViewById(R.id.llChatItem);
        }
    }
}
