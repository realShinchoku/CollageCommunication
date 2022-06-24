package com.G12LTUDDD.collagecommunication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.G12LTUDDD.collagecommunication.Adapters.GroupAdapter;
import com.G12LTUDDD.collagecommunication.Models.Group;
import com.G12LTUDDD.collagecommunication.Models.User;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class GroupChatActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;
    User u;
    GroupAdapter groupAdapter;
    List<Group> groups;
    RecyclerView rvGroup;
    SearchView svGroup;
    ImageButton ibMenu;
    CircleImageView civUser;
    FloatingActionButton fabJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        init();

    }

    void init() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        u = new User();
        groups = new ArrayList<>();

        rvGroup = findViewById(R.id.rvGroup);
        svGroup = findViewById(R.id.svGroup);
        ibMenu = findViewById(R.id.ibGroupMenu);
        civUser = findViewById(R.id.civUserGroup);
        fabJoin = findViewById(R.id.fabJoin);

        final FirebaseUser curUser = auth.getCurrentUser();
        u.setUid(curUser.getUid());
        u.setEmail(curUser.getEmail());
        db.collection("Users").document(u.getUid())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("TAG", "Listen failed.", error);
                        return;
                    }
                    if (value.exists()) {
                        u = value.toObject(User.class);
                        if (!u.getImg().equals(""))
                            Picasso.get().load(u.getImg()).into(civUser);
                    }
                });

        db.collection("Groups")
                .orderBy("modTime", Query.Direction.DESCENDING)
                .whereArrayContains("users", u.getUid())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("TAG", "Listen failed.", error);
                        return;
                    }
                    if (!value.isEmpty()) {
                        groups = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            Group group = doc.toObject(Group.class);
                            groups.add(group);
                        }
                        displayGroups(groups);
                    }
                });

        civUser.setOnClickListener(v -> {
            Intent i = new Intent(GroupChatActivity.this, UserActivity.class);
            i.putExtra("user", u);
            startActivity(i);
        });

        ibMenu.setOnClickListener(v -> showMenu(v));

        svGroup.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {
                    List<Group> searchGroup = new ArrayList<>();
                    for (Group g : groups) {
                        if (g.getName().toLowerCase().contains(query.toLowerCase())) {
                            searchGroup.add(g);
                        }
                    }
                    displayGroups(searchGroup);
                } else
                    displayGroups(groups);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    List<Group> searchGroup = new ArrayList<>();
                    for (Group g : groups) {
                        if (g.getName().toLowerCase().contains(newText.toLowerCase())) {
                            searchGroup.add(g);
                        }
                    }
                    displayGroups(searchGroup);
                } else
                    displayGroups(groups);

                return true;
            }

        });

        fabJoin.attachToRecyclerView(rvGroup);
        fabJoin.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(GroupChatActivity.this);
            alertDialog.setTitle("Tham Gia Nhóm");
            alertDialog.setMessage("Nhập mã nhóm");

            final EditText input = new EditText(GroupChatActivity.this);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);

            alertDialog.setPositiveButton("Tham gia", (dialog, which) -> {
                String gid = input.getText().toString().toUpperCase();
                db.collection("Groups").document(gid).get().addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {

                        db.collection("Groups").document(gid).update("users", FieldValue.arrayUnion(u.getUid()));
                    } else {
                        Toast.makeText(getApplicationContext(), "Không tìm thấy nhóm có mã " + gid, Toast.LENGTH_SHORT).show();
                    }
                });
            });

            alertDialog.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
            alertDialog.show();
        });
    }


    private void displayGroups(List<Group> groups) {
        rvGroup.setLayoutManager(new LinearLayoutManager(GroupChatActivity.this));
        groupAdapter = new GroupAdapter(GroupChatActivity.this, groups, db);
        rvGroup.setAdapter(groupAdapter);
    }

    // menu
    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(GroupChatActivity.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menuLogout) {
                auth.signOut();
                finish();
                startActivity(new Intent(GroupChatActivity.this, MainActivity.class));
            } else if (item.getItemId() == R.id.menuAdd) {
                Group group = new Group();

                group.setName("Nhóm của bạn");
                List<String> users = new ArrayList<>();
                users.add(u.getUid());
                group.setUsers(users);
                group.setAdmins(users);
                group.setModTime(Timestamp.now().toDate());
                group.setLastMsg(u.getName() + " đã tạo nhóm");
                group.setGid(generateGID());

                db.collection("Groups").document(group.getGid()).set(group);
            }
            return true;
        });
        popupMenu.show();
    }


    private String generateGID() {
        int leftLimit = 65; // letter 'A'
        int rightLimit = 90; // letter 'Z'
        int targetStringLength = 5;
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