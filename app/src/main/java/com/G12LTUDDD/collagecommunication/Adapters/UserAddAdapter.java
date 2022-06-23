package com.G12LTUDDD.collagecommunication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.Models.User;
import com.G12LTUDDD.collagecommunication.R;
import com.G12LTUDDD.collagecommunication.UserActivity;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAddAdapter extends RecyclerView.Adapter<UserAddAdapter.UserAddAdapterViewHolder> {
    Context context;
    List<User> users;
    FirebaseFirestore Db;
    String gid;

    public UserAddAdapter(Context context, List<User> users, FirebaseFirestore db, String gid) {
        this.context = context;
        this.users = users;
        Db = db;
        this.gid = gid;
    }

    @NonNull
    @Override
    public UserAddAdapter.UserAddAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_adapter, parent, false);
        return new UserAddAdapter.UserAddAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAddAdapter.UserAddAdapterViewHolder holder, int position) {
        User u = users.get(position);

        if (u.getImg().equals(""))
            Picasso.get().load(u.getImg()).into(holder.civImg);
        holder.tvName.setText(u.getName());

        holder.tvName.setOnClickListener(v -> {
            Intent i = new Intent(context, UserActivity.class);
            i.putExtra("user", u);
            context.startActivity(i);
        });

        holder.civImg.setOnClickListener(v -> {
            Intent i = new Intent(context, UserActivity.class);
            i.putExtra("user", u);
            context.startActivity(i);
        });

        holder.ibAdd.setOnClickListener(v -> {
            Db.collection("Groups").document(gid).update("users", FieldValue.arrayUnion(u.getUid()));
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class UserAddAdapterViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView civImg;
        public TextView tvName;
        public ImageButton ibMenu, ibAdd;

        public UserAddAdapterViewHolder(View itemView) {
            super(itemView);
            civImg = itemView.findViewById(R.id.civUserItem);
            tvName = itemView.findViewById(R.id.tvUserItem);
            ibMenu = itemView.findViewById(R.id.ibMenuUserItem);
            ibAdd = itemView.findViewById(R.id.ibAddUserItem);
            ibMenu.setVisibility(View.GONE);
        }
    }

}
