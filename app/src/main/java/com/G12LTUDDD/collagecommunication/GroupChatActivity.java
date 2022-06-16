package com.G12LTUDDD.collagecommunication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.Adapters.GroupAdapter;
import com.G12LTUDDD.collagecommunication.Models.Group;
import com.G12LTUDDD.collagecommunication.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroupChatActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;
    User u;
    GroupAdapter groupAdapter;
    List<Group> groups;
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


    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser curUser = auth.getCurrentUser();
        u.setUid(curUser.getUid());
        u.setEmail(curUser.getEmail());


        db.collection("Users").document(u.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    u = documentSnapshot.toObject(User.class);
            }
        });

        db.collection("Groups")
                .orderBy("modTime", Query.Direction.DESCENDING)
                .whereArrayContains("users",u.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("TAG", "Listen failed.", error);
                            return;
                        }

                        groups = new ArrayList<>();
                        for(QueryDocumentSnapshot doc : value){
                            Group group = doc.toObject(Group.class);
                            groups.add(group);
                        }
                        displayGroups(groups);
                    }
                });
    }





    private void displayGroups(List<Group> groups){
        rvGroup.setLayoutManager(new LinearLayoutManager(GroupChatActivity.this));
        groupAdapter = new GroupAdapter(GroupChatActivity.this,groups,db);
        rvGroup.setAdapter(groupAdapter);
    }

    // menu
    private void showMenu(View v){
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
                        Group group = new Group();
                        group.setGid(generateGID());
                        group.setName("Nhóm của bạn");
                        List<String> users = new ArrayList<String>();
                        users.add(u.getUid());
                        group.setUsers(users);
                        group.setAdmins(users);
                        group.setModTime(Timestamp.now().toDate());
                        db.collection("Groups").add(group);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private String generateGID(){
        int leftLimit = 65; // letter 'A'
        int rightLimit = 90; // letter 'Z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}