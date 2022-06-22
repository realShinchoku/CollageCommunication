package com.G12LTUDDD.collagecommunication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.Models.Message;
import com.G12LTUDDD.collagecommunication.Models.User;
import com.G12LTUDDD.collagecommunication.R;
import com.G12LTUDDD.collagecommunication.UserActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {

    Context context;
    List<Message> messages;
    FirebaseFirestore db;
    String uid,gid;

    public MessageAdapter(Context context, List<Message> messages, FirebaseFirestore db, String uid, String gid) {
        this.context = context;
        this.messages = messages;
        this.db = db;
        this.uid = uid;
        this.gid = gid;
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


        String time = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(message.getTime());
        holder.tvTime.setText(time);

        if(message.getType().equals("text")){
            holder.ivValue.setVisibility(View.GONE);
            holder.tvValue.setText(message.getValue());
            holder.tvValue.setVisibility(View.VISIBLE);
        }
        else{
            holder.tvValue.setVisibility(View.GONE);
            if(!message.getValue().equals("")) {
                Picasso.get().load(message.getValue()).into(holder.ivValue);
                holder.ivValue.setVisibility(View.VISIBLE);
            }
        }

        if (message.getUid().equals(uid)) {
            holder.civImg.setVisibility(View.GONE);
            holder.tvValue.setBackgroundResource(R.drawable.my_msg_back);
            holder.ll.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

            holder.tvValue.setOnClickListener(v -> {
                if (holder.tvTime.getVisibility() == View.GONE) {
                    holder.tvTime.setVisibility(View.VISIBLE);
                    holder.ibDelete.setVisibility(View.VISIBLE);
                } else {
                    holder.tvTime.setVisibility(View.GONE);
                    holder.ibDelete.setVisibility(View.GONE);
                }
            });
            holder.ivValue.setOnClickListener(v -> {
                if (holder.tvTime.getVisibility() == View.GONE) {
                    holder.tvTime.setVisibility(View.VISIBLE);
                    holder.ibDelete.setVisibility(View.VISIBLE);
                } else {
                    holder.tvTime.setVisibility(View.GONE);
                    holder.ibDelete.setVisibility(View.GONE);
                }
            });
            holder.ibDelete.setOnClickListener(v -> {
                db.collection("Messages").document(message.getKey()).delete();
                db.collection("Groups").document(gid).update("lastMsg","Tin nhắn đã xóa");
            });
        }
        else {
            db.collection("Users").document(message.getUid())
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.w("TAG", "Listen failed.", error);
                            return;
                        }
                        if(value.exists()) {
                            User u = value.toObject(User.class);
                            if(!u.getImg().equals(""))
                                Picasso.get().load(u.getImg()).into(holder.civImg);
                            holder.civImg.setOnClickListener(v -> {
                                Intent i = new Intent(context, UserActivity.class);
                                i.putExtra("user", u);
                                context.startActivity(i);
                            });
                        }
                    });
            holder.civImg.setVisibility(View.VISIBLE);
            holder.tvValue.setBackgroundResource(R.drawable.opo_msg_back);
            holder.ll.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

            holder.tvValue.setOnClickListener(v -> {
                if (holder.tvTime.getVisibility() == View.GONE) {
                    holder.tvTime.setVisibility(View.VISIBLE);
                } else {
                    holder.tvTime.setVisibility(View.GONE);
                }
            });

            holder.ivValue.setOnClickListener(v -> {
                if (holder.tvTime.getVisibility() == View.GONE) {
                    holder.tvTime.setVisibility(View.VISIBLE);
                } else {
                    holder.tvTime.setVisibility(View.GONE);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView civImg;
        public TextView tvValue, tvTime;
        public ImageButton ibDelete;
        public LinearLayout ll;
        public ImageView ivValue;

        public MessageAdapterViewHolder(View itemView) {
            super(itemView);
            civImg = itemView.findViewById(R.id.civChatItem);
            tvValue = itemView.findViewById(R.id.tvValueChatItem);
            tvTime = itemView.findViewById(R.id.tvTimeChatItem);
            ibDelete = itemView.findViewById(R.id.ibChatItem);
            ivValue = itemView.findViewById(R.id.ivValueChatItem);
            ll = itemView.findViewById(R.id.llChatItem);
        }
    }
}
