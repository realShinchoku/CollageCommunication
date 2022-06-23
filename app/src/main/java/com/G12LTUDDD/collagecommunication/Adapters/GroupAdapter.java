package com.G12LTUDDD.collagecommunication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.ChatActivity;
import com.G12LTUDDD.collagecommunication.Models.Group;
import com.G12LTUDDD.collagecommunication.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupAdapterViewHolder> {

    Context context;
    List<Group> groups;
    FirebaseFirestore Db;

    public GroupAdapter(Context context, List<Group> groups, FirebaseFirestore Db) {
        this.context = context;
        this.groups = groups;
        this.Db = Db;
    }

    @NonNull
    @Override
    public GroupAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.group_adapter, parent, false);
        return new GroupAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapterViewHolder holder, int position) {

        Group group = groups.get(position);
        if (!group.getImg().equals(""))
            Picasso.get().load(group.getImg()).into(holder.civImg);
        holder.tvName.setText(group.getName());
        holder.tvMsg.setText(group.getLastMsg());
        String time = new java.text.SimpleDateFormat("HH:mm").format(group.getModTime());
        holder.tvTime.setText(time);

        holder.llItem.setOnClickListener(v -> {
            Intent i = new Intent(context, ChatActivity.class);
            i.putExtra("group", group);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class GroupAdapterViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView civImg;
        public TextView tvName;
        public TextView tvMsg;
        public TextView tvTime;
        public LinearLayout llItem;

        public GroupAdapterViewHolder(View itemView) {
            super(itemView);
            this.civImg = (CircleImageView) itemView.findViewById(R.id.civGroupItem);
            this.tvName = (TextView) itemView.findViewById(R.id.tvNameGroupItem);
            this.tvMsg = (TextView) itemView.findViewById(R.id.tvMsgGroupItem);
            this.tvTime = (TextView) itemView.findViewById(R.id.tvTimeGroupItem);
            this.llItem = (LinearLayout) itemView.findViewById(R.id.llGroupItem);
        }
    }
}
