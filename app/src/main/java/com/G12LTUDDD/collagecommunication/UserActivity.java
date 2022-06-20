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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.G12LTUDDD.collagecommunication.Models.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {
    User u;
    FirebaseAuth auth;
    FirebaseFirestore db;
    StorageReference reference;

    CircleImageView civUser;
    ImageButton ibImg,ibName,ibInfo,ibBack,ibCancel,ibSave;
    EditText etName,etTen,etLop,etMsv;
    TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
    }
    void init(){
        Intent i = getIntent();
        u = (User) i.getExtras().getSerializable("user");

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        reference = FirebaseStorage.getInstance().getReference().child("img/users/"+u.getUid());

        civUser = (CircleImageView) findViewById(R.id.civUser);

        etName = (EditText) findViewById(R.id.etName);
        etTen = (EditText) findViewById(R.id.etTen);
        etLop = (EditText) findViewById(R.id.etLop);
        etMsv = (EditText) findViewById(R.id.etMsv);

        KeyListener variable = etName.getKeyListener();

        tvEmail = (TextView) findViewById(R.id.tvEmailUser);
        tvEmail.setText(u.getEmail());

        ibImg = (ImageButton) findViewById(R.id.ibEditImgUser);
        ibName = (ImageButton) findViewById(R.id.ibName);
        ibInfo = (ImageButton) findViewById(R.id.ibInfo);
        ibBack = (ImageButton) findViewById(R.id.ibBackUser);
        ibCancel = (ImageButton) findViewById(R.id.ibCancelUser);
        ibSave = (ImageButton) findViewById(R.id.ibSaveUser);

        HideSaveAndCancel();
        final FirebaseUser curUser = auth.getCurrentUser();
        if(!u.getUid().equals(curUser.getUid())){
            ibImg.setVisibility(View.GONE);
            ibName.setVisibility(View.GONE);
            ibInfo.setVisibility(View.GONE);
        }

        db.collection("Users").document(u.getUid())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("TAG", "Listen failed.", error);
                        return;
                    }
                    if(value.exists()) {
                        u = value.toObject(User.class);
                        if(!u.getImg().equals(""))
                            Picasso.get().load(u.getImg()).into(civUser);
                        setEt();
                    }
                });

        ibName.setOnClickListener(v -> {
            etName.setKeyListener(variable);
            etName.requestFocus();
            ShowSaveAndCancel();
        });
        ibInfo.setOnClickListener(v ->{
            etTen.setKeyListener(variable);
            etLop.setKeyListener(variable);
            etMsv.setKeyListener(variable);
            etTen.requestFocus();
            ShowSaveAndCancel();
        });
        ibCancel.setOnClickListener(v ->{
            HideSaveAndCancel();
            setEt();
            hideKeyBroad(v);
        });
        ibSave.setOnClickListener(v -> {
            db.collection("Users").document(u.getUid())
                            .update("name",etName.getText().toString(),"ten",etTen.getText().toString(),"lop",etLop.getText().toString(),"msv",etMsv.getText().toString());
            HideSaveAndCancel();
            hideKeyBroad(v);
        });
        ibImg.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .crop()
                    .start();
        });
        ibBack.setOnClickListener(v ->{
            finish();
        });
    }

    void ShowSaveAndCancel(){
        ibSave.setVisibility(View.VISIBLE);
        ibCancel.setVisibility(View.VISIBLE);
        ibBack.setVisibility(View.GONE);
        ibInfo.setVisibility(View.GONE);
        ibName.setVisibility(View.INVISIBLE);
        ibImg.setVisibility(View.GONE);
    }

    void HideSaveAndCancel(){
        ibSave.setVisibility(View.GONE);
        ibCancel.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibInfo.setVisibility(View.VISIBLE);
        ibName.setVisibility(View.VISIBLE);
        ibImg.setVisibility(View.VISIBLE);

        etName.setKeyListener(null);
        etName.clearFocus();
        etTen.setKeyListener(null);
        etTen.clearFocus();
        etLop.setKeyListener(null);
        etLop.clearFocus();
        etMsv.setKeyListener(null);
        etMsv.clearFocus();
    }

    void setEt(){
        etName.setText(u.getName());
        etLop.setText(u.getLop());
        etTen.setText(u.getTen());
        etMsv.setText(u.getMsv());
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
                                    .addOnSuccessListener(uri -> {
                                        db.collection("Users").document(u.getUid()).update("img",uri.toString());
                                    });
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