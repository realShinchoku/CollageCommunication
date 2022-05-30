package com.G12LTUDDD.collagecommunication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.G12LTUDDD.collagecommunication.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText txtEmail, txtPassword, txtUserName, txtRepeatPassword;
    ProgressBar progressBar;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtUserName = findViewById(R.id.txtRegisterUsername);
        txtEmail = findViewById(R.id.txtRegisterEmail);
        txtPassword = findViewById(R.id.txtRegisterPassword);
        txtRepeatPassword = findViewById(R.id.txtRegisterRepeatPassword);
        progressBar = findViewById(R.id.pgbRegisterLoading);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }


    public void RegisterUser(View v){

        final String username = txtUserName.getText().toString();
        final String email = txtEmail.getText().toString();
        final String password = txtPassword.getText().toString();
        final String repassword = txtRepeatPassword.getText().toString();

        progressBar.setVisibility(View.VISIBLE);

        if(!email.equals("") && !username.equals("") && !password.equals("") && !repassword.equals("")){
            if(password.length() <6){
                Toast.makeText(getApplicationContext(),"Mật khẩu dài ít nhất 6 ký tự",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            else if(!password.equals(repassword)){
                Toast.makeText(getApplicationContext(),"Mật khẩu không khớp",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            else {

                auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    FirebaseUser firebaseUser = auth.getCurrentUser();

                                    User u = new User();
                                    u.setEmail(email);
                                    u.setName(username);

                                    db.collection("Users").add(new HashMap<String, Object>(){{put(firebaseUser.getUid(),u);}})
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    if(task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);
                                                        finish();
                                                        Intent i = new Intent(RegisterActivity.this, GroupChatActivity.class);
                                                        startActivity(i);
                                                    }
                                                    else{
                                                        Toast.makeText(getApplicationContext(),"Có lỗi xảy ra",Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);
                                                        return;
                                                    }
                                                }
                                            });
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Email đã tồn tại",Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    return;
                                }
                            }
                        });
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"Vui lòng điền đầy đủ thông tin",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
    }

    public void gotoLogin(View v){
        Intent i = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(i);
    }
}