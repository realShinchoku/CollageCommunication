package com.G12LTUDDD.collagecommunication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.G12LTUDDD.collagecommunication.Models.Group;
import com.G12LTUDDD.collagecommunication.Models.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupDetailActivity extends AppCompatActivity {
    Group group;
    User u;
    FirebaseAuth auth;
    FirebaseFirestore db;
    StorageReference reference;

    CircleImageView civDetail;
    EditText etDetail;
    ImageButton ibImg,ibDetail,ibBack,ibCancel,ibSave;
    LinearLayout llList,llAdd,llLeave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        init();
    }
    void init(){
        Intent i = getIntent();
        group = (Group) i.getExtras().getSerializable("group");

        u = new User();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        reference = FirebaseStorage.getInstance().getReference().child("img/groups/"+group.getGid());

        civDetail = (CircleImageView) findViewById(R.id.civDetail);
        etDetail = (EditText) findViewById(R.id.etDetail);

        KeyListener variable = etDetail.getKeyListener();

        ibImg = (ImageButton) findViewById(R.id.ibImgDetail);
        ibDetail = (ImageButton) findViewById(R.id.ibDetail);
        ibBack = (ImageButton) findViewById(R.id.ibBackDetail);
        ibCancel = (ImageButton) findViewById(R.id.ibCancelDetail);
        ibSave = (ImageButton) findViewById(R.id.ibSaveDetail);

        llLeave = (LinearLayout) findViewById(R.id.llGroupLeave);
        llList = (LinearLayout) findViewById(R.id.lltroupUsersList);

        HideSaveAndCancel();
        final FirebaseUser curUser = auth.getCurrentUser();
        u.setUid(curUser.getUid());
        u.setEmail(curUser.getEmail());

        db.collection("Users").document(u.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        u = task.getResult().toObject(User.class);
                    }
                });

        db.collection("Groups").document(group.getGid())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("TAG", "Listen failed.", error);
                        return;
                    }
                    if(value.exists()) {
                        group = value.toObject(Group.class);
                        if(!group.getUsers().contains(u.getUid())){
                            finishAffinity();
                            startActivity(new Intent(GroupDetailActivity.this,GroupChatActivity.class));
                        }
                        if(!group.getImg().equals(""))
                            Picasso.get().load(group.getImg()).into(civDetail);
                        setEt();
                    }
                });

        ibDetail.setOnClickListener(v -> {
            etDetail.setKeyListener(variable);
            etDetail.requestFocus();
            ShowSaveAndCancel();
        });

        ibCancel.setOnClickListener(v ->{
            HideSaveAndCancel();
            setEt();
            hideKeyBroad(v);
        });
        ibSave.setOnClickListener(v -> {
            db.collection("Groups").document(group.getGid())
                    .update("name",etDetail.getText().toString());
            HideSaveAndCancel();
            hideKeyBroad(v);
        });
        ibImg.setOnClickListener(v -> ImagePicker.with(this)
                .crop()
                .start());
        ibBack.setOnClickListener(v -> finish());

        llLeave.setOnClickListener(v -> {
            db.collection("Groups").document(group.getGid()).update("users",FieldValue.arrayRemove(u.getUid()));
            finishAffinity();
            startActivity(new Intent(GroupDetailActivity.this,GroupChatActivity.class));
        });

        llList.setOnClickListener(v -> {
            startActivity(new Intent(GroupDetailActivity.this,UsersListActivity.class).putExtra("group",group));
        });
    }

    void ShowSaveAndCancel(){
        ibSave.setVisibility(View.VISIBLE);
        ibCancel.setVisibility(View.VISIBLE);
        ibBack.setVisibility(View.GONE);
        ibDetail.setVisibility(View.INVISIBLE);
        ibImg.setVisibility(View.GONE);
    }

    void HideSaveAndCancel(){
        ibSave.setVisibility(View.GONE);
        ibCancel.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibDetail.setVisibility(View.VISIBLE);
        ibImg.setVisibility(View.VISIBLE);

        etDetail.setKeyListener(null);
        etDetail.clearFocus();
    }

    void setEt(){
        etDetail.setText(group.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            // Get the url of the image from data
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                // update the preview image in the layout
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Tải ảnh lên");
                progressDialog.show();
                reference.putFile(selectedImageUri)
                        .addOnProgressListener(snapshot -> {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressDialog.setMessage("Đã tải " + (int) progress + "%");
                        })
                        .addOnCompleteListener(taskSnapshot -> {
                            reference.getDownloadUrl()
                                    .addOnSuccessListener(uri -> db.collection("Groups").document(group.getGid()).update("img",uri.toString()));
                            progressDialog.dismiss();
                        });
            }
        }
    }

    void hideKeyBroad(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}