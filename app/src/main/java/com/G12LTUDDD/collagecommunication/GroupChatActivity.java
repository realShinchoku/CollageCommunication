package com.G12LTUDDD.collagecommunication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.Adapters.GroupAdapter;
import com.G12LTUDDD.collagecommunication.Models.Group;
import com.G12LTUDDD.collagecommunication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;
    User u;
    List<Group> groups;
    GroupAdapter groupAdapter;

    RecyclerView rvGroup;
    SearchView svGroup;
    ImageButton ibMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        init();

    }

    void init(){
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        u = new User();
        groups = new ArrayList<>();
        rvGroup = (RecyclerView) findViewById(R.id.rvGroup);
        svGroup = (SearchView) findViewById(R.id.svGroup);

        ibMenu = (ImageButton) findViewById(R.id.ibGroupMenu);
        ibMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });
    }




    // menu
    void showMenu(View v){
        PopupMenu popupMenu = new PopupMenu(GroupChatActivity.this,v);
        popupMenu.getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuLogout:
                        auth.signOut();
                        finish();
                        startActivity(new Intent(GroupChatActivity.this,MainActivity.class));
                        break;
                    case R.id.menuFind:

                        break;
                    case R.id.menuAdd:

                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

}