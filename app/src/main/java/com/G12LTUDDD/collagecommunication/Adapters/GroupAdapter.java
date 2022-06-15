package com.G12LTUDDD.collagecommunication.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.Models.Group;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class GroupAdapterViewHolder extends RecyclerView.ViewHolder {
        public GroupAdapterViewHolder(View itemView){
            super(itemView);
        }
    }
}
