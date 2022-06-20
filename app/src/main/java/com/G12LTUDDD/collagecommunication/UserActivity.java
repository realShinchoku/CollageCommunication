package com.G12LTUDDD.collagecommunication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.G12LTUDDD.collagecommunication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import lv.chi.photopicker.ChiliPhotoPicker;

public class UserActivity extends AppCompatActivity {
    User u;
    FirebaseAuth auth;
    FirebaseFirestore db;

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

        civUser = (CircleImageView) findViewById(R.id.civUser);

        etName = (EditText) findViewById(R.id.etName);
        etTen = (EditText) findViewById(R.id.etTen);
        etLop = (EditText) findViewById(R.id.etLop);
        etMsv = (EditText) findViewById(R.id.etMsv);
        setEt();

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
                    }
                });

        ibName.setOnClickListener(v -> {
            ShowSaveAndCancel();
            etName.setClickable(true);
            etName.setFocusable(true);
            etName.setFocusableInTouchMode(true);
            etName.requestFocus();

        });
        ibInfo.setOnClickListener(v ->{
            etTen.setClickable(true);
            etTen.setFocusable(true);
            etTen.setFocusableInTouchMode(true);
            etLop.setClickable(true);
            etLop.setFocusable(true);
            etLop.setFocusableInTouchMode(true);
            etMsv.setClickable(true);
            etMsv.setFocusable(true);
            etMsv.setFocusableInTouchMode(true);
            etTen.requestFocus();
            ShowSaveAndCancel();


        });
        ibCancel.setOnClickListener(v ->{
            HideSaveAndCancel();
            setEt();
        });

        ibImg.setOnClickListener(v -> {
            Picasso.with(context)
                    .load(uri)
                    .placeholder(R.drawable.bg_placeholder)
                    .fit()
                    .centerCrop()
                    .into(view)
                    
        });
    }

    void ShowSaveAndCancel(){
        ibSave.setVisibility(View.VISIBLE);
        ibCancel.setVisibility(View.VISIBLE);
        ibBack.setVisibility(View.GONE);
        ibInfo.setVisibility(View.GONE);
        ibName.setVisibility(View.GONE);
    }

    void HideSaveAndCancel(){
        ibSave.setVisibility(View.GONE);
        ibCancel.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibInfo.setVisibility(View.VISIBLE);
        ibName.setVisibility(View.VISIBLE);
        etTen.setClickable(false);
        etTen.setFocusable(false);
        etTen.setFocusableInTouchMode(false);
        etLop.setClickable(false);
        etLop.setFocusable(false);
        etLop.setFocusableInTouchMode(false);
        etMsv.setClickable(false);
        etMsv.setFocusable(false);
        etMsv.setFocusableInTouchMode(false);
        etName.setClickable(false);
        etName.setFocusable(false);
        etName.setFocusableInTouchMode(false);
    }

    void setEt(){
        etName.setText(u.getName());
        etLop.setText(u.getLop());
        etTen.setText(u.getTen());
        etMsv.setText(u.getMsv());
    }
}