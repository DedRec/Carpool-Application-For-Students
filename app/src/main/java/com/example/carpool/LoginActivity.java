package com.example.carpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    TextView signUpLink;
    Intent intentLogin;
    EditText loginEmail, loginPassword;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginBtn = findViewById(R.id.login_btn);
        signUpLink = findViewById(R.id.signup_link);
        loginEmail = findViewById(R.id.email_input);
        loginPassword = findViewById(R.id.password_login);

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentLogin = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intentLogin);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent1 = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent1);
                finish();*/
                /*if(validateUsername() && validatePassword()){
                    checkUser();
                }*/
                String email = String.valueOf(loginEmail.getText());
                String password = String.valueOf(loginPassword.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this,"Enter your email",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"Enter your password",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("FBAuth", "signInWithEmail:success");
                                    Toast.makeText(LoginActivity.this, "Login successful.",
                                            Toast.LENGTH_SHORT).show();
                                    checkUser();
                                    Intent intent1 = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent1);
                                    finish();

                                } else {
                                    Log.w("FBAuth", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent1 = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent1);
            finish();
        }
    }

    public void checkUser(){
        FirebaseUser user = mAuth.getCurrentUser();
        final String[] username = new String[1];
        final String[] password = new String[1];
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("drivers");
        Query checkDriverDatabase = reference.orderByChild("userid").equalTo(user.getUid());
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference2.orderByChild("userid").equalTo(user.getUid());
        checkDriverDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    username[0] =  snapshot.child(user.getUid()).child("username").getValue(String.class);
                    password[0] = snapshot.child(user.getUid()).child("password").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    HelperUser helper = new HelperUser(user.getDisplayName(), user.getEmail(),username[0],password[0],user.getUid());
                    reference2.child(user.getUid()).setValue(helper);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*public Boolean validateUsername(){
        String val = loginUsername.getText().toString();
        if(val.isEmpty()){
            loginUsername.setError("Enter a valid username");
            return false;
        }else{
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if(val.isEmpty()){
            loginPassword.setError("Incorrect Password");
            return false;
        }else{
            loginPassword.setError(null);
            return true;
        }
    }*/

    /*public void checkUser(){
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)){
                        loginUsername.setError(null);

                        Intent intent1 = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent1);
                        finish();
                    }else{
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                    }
                }else{
                    loginUsername.setError("Username doesn't exist");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}