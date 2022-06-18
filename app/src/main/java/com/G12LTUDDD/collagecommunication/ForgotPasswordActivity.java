package com.G12LTUDDD.collagecommunication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText txtEmail;
    ProgressBar progressBar;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_forgotpassword);
        txtEmail = findViewById(R.id.txtForgotPasswordEmail);
        progressBar = findViewById(R.id.pgbForgotPasswordLoading);
    }

    public void forgotPassword(View v){
        progressBar.setVisibility(View.VISIBLE);

        final String email = txtEmail.getText().toString();
        if(!email.equals("")){
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Thành công, vui lòng kiểm tra hộp thư của bạn",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ForgotPasswordActivity.this,MainActivity.class);
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Lỗi, vui lòng kiểm tra email của bạn",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
        else {
            Toast.makeText(getApplicationContext(),"Vui lòng nhập email của bạn",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }
}