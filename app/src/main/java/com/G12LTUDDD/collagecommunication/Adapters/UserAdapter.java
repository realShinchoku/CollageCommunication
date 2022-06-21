package com.G12LTUDDD.collagecommunication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.Models.Group;
import com.G12LTUDDD.collagecommunication.Models.User;
import com.G12LTUDDD.collagecommunication.R;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterViewHolder> {

    Context context;
    Group group;
    FirebaseFirestore Db;
    String userid;

    public UserAdapter(Context context, Group group, FirebaseFirestore db, String userid) {
        this.context = context;
        this.group = group;
        this.Db = db;
        this.userid = userid;
    }

    @NonNull
    @Override
    public UserAdapter.UserAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_adapter,parent,false);
        return new UserAdapter.UserAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserAdapterViewHolder holder, int position) {
        String uid = group.getUsers().get(position);
        if(group.getAdmins().contains(uid)){
            holder.ibMenu.setVisibility(View.GONE);
        }
        else {
            holder.ibMenu.setVisibility(View.VISIBLE);
            holder.ibMenu.setOnClickListener(v -> showMenu(v, uid));
        }

        if(uid.equals(userid)){
            holder.ibMenu.setVisibility(View.GONE);
        }

        Db.collection("Users").document(uid)
            .get()
            .addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    User u = task.getResult().toObject(User.class);
                    if(!u.getImg().equals(""))
                        Picasso.get().load(u.getImg()).into(holder.civImg);
                    holder.tvName.setText(u.getName());
                }
            });
    }

    @Override
    public int getItemCount() {
        return group.getUsers().size();
    }


    public class UserAdapterViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView civImg;
        public TextView tvName;
        public ImageButton ibMenu;
        public UserAdapterViewHolder(View itemView){
            super(itemView);
            civImg = itemView.findViewById(R.id.civUserItem);
            tvName = itemView.findViewById(R.id.tvUserItem);
            ibMenu = itemView.findViewById(R.id.ibUserItem);
        }
    }

    private void showMenu(View v, String uid) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_user, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menuAdmins) {
                Db.collection("Groups").document(group.getGid()).update("admins", FieldValue.arrayUnion(uid));
            }
            else if (item.getItemId() == R.id.menuDelete) {
                Db.collection("Groups").document(group.getGid()).update("admins", FieldValue.arrayRemove(uid));
            }
            return true;
        });
        popupMenu.show();
    }
}
