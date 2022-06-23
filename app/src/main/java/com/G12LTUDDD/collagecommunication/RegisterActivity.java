package com.G12LTUDDD.collagecommunication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.G12LTUDDD.collagecommunication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    EditText txtEmail, txtPassword, txtUserName, txtRepeatPassword;

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

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }


    public void RegisterUser(View v) {

        final String username = txtUserName.getText().toString();
        final String email = txtEmail.getText().toString();
        final String password = txtPassword.getText().toString();
        final String repassword = txtRepeatPassword.getText().toString();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đăng ký");
        progressDialog.show();

        if (!email.equals("") && !username.equals("") && !password.equals("") && !repassword.equals("")) {
            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Mật khẩu dài ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            } else if (!password.equals(repassword)) {
                Toast.makeText(getApplicationContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            } else {

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = auth.getCurrentUser();

                                User u = new User();
                                u.setUid(firebaseUser.getUid());
                                u.setEmail(email);
                                u.setName(username);

                                db.collection("Users")
                                        .document(u.getUid())
                                        .set(u)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                finish();
                                                Intent i = new Intent(RegisterActivity.this, GroupChatActivity.class);
                                                startActivity(i);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                return;
                                            }
                                        });
                            } else {
                                Toast.makeText(getApplicationContext(), "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                return;
                            }
                        });
            }
        } else {
            Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
    }

    public void gotoLogin(View v) {
        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(i);
    }
}