package com.G12LTUDDD.collagecommunication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.G12LTUDDD.collagecommunication.Models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore db;
    CallbackManager callbackManager;
    EditText txtEmail, txtPassword;
    LoginButton lbFacebook;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        callbackManager = CallbackManager.Factory.create();

        checkLogin();

        txtEmail = findViewById(R.id.txtLoginEmail);
        txtPassword = findViewById(R.id.txtLoginPassword);
        lbFacebook = findViewById(R.id.lbFacebook);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đăng nhập");


        loginFacebook();

    }

    void loginFacebook() {
        lbFacebook.setReadPermissions("email", "public_profile");
        lbFacebook.registerCallback(callbackManager, new FacebookCallback<>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onCancel() {
                Log.d("TAG", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("TAG", "facebook:onError", error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        progressDialog.show();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser curUser = auth.getCurrentUser();

                        User u = new User();
                        u.setUid(curUser.getUid());
                        u.setEmail(curUser.getEmail());
                        u.setName(curUser.getDisplayName());
                        u.setImg(curUser.getPhotoUrl().toString());
                        db.collection("Users")
                                .whereEqualTo("uid", u.getUid())
                                .get()
                                .addOnSuccessListener(task1 -> {
                                    if (task1.isEmpty()) {
                                        db.collection("Users").document(u.getUid()).set(u);
                                        Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_SHORT).show();
                                    }
                                    checkLogin();
                                });

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, "Lỗi xác thực.", Toast.LENGTH_SHORT).show();
                    }
                });

        progressDialog.dismiss();
    }

    void checkLogin() {
        if (auth.getCurrentUser() != null) {
            finish();
            Intent i = new Intent(MainActivity.this, GroupChatActivity.class);
            startActivity(i);
        }

    }

    public void LoginUser(View v) {

        final String email = txtEmail.getText().toString();
        final String password = txtPassword.getText().toString();

        progressDialog.show();

        if (!email.equals("") && !password.equals("")) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_SHORT).show();
                            checkLogin();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Sai email/mật khẩu. Hãy thử lại", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Vui lòng điền email/ mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void gotoRegister(View v) {
        Intent i = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    public void gotoForgotPassword(View v) {
        Intent i = new Intent(MainActivity.this, ForgotPasswordActivity.class);
        startActivity(i);
    }
}