package com.G12LTUDDD.collagecommunication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText txtEmail, txtPassword;
    ProgressBar progressBar;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            Intent i = new Intent(MainActivity.this,GroupChatActivity.class);
            startActivity(i);
        }
        else{
            setContentView(R.layout.activity_login);

            txtEmail = (EditText) findViewById(R.id.txtLoginEmail);
            txtPassword = (EditText) findViewById(R.id.txtLoginPassword);
            progressBar = (ProgressBar) findViewById(R.id.pgbLoginLoading);
            reference = FirebaseDatabase.getInstance().getReference().child("Users");
        }

    }

    public void LoginUser(View v){
        progressBar.setVisibility(View.VISIBLE);

        final String email = txtEmail.getText().toString();
        final String password = txtPassword.getText().toString();

        if(!email.equals("") && !password.equals("")){
            auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Thành công",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivity.this,GroupChatActivity.class);
                                startActivity(i);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Sai email/mật khẩu. Hãy thử lại",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                return;
                            }
                        }
                    });
        }
        else {
            Toast.makeText(getApplicationContext(),"Vui lòng điền email/ mật khẩu",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
    }

    public  void gotoRegister(View v){
        Intent i = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(i);
    }
    public void gotoForgotPassword(View v){
        Intent i = new Intent(MainActivity.this,ForgotPasswordActivity.class);
        startActivity(i);
    }
}