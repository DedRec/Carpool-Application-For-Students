package com.example.drivercarpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drivercarpool.model.FirebaseDB;
import com.example.drivercarpool.viewmodel.UserViewModel;
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
    private Button loginBtn;
    private TextView signUpLink;
    private Intent intentLogin;
    private EditText loginEmail, loginPassword;
    private FirebaseAuth mAuth;
    private FirebaseDB firebaseDB;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        firebaseDB = FirebaseDB.getInstance();
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
                                    firebaseDB.checkUser(mAuth);
                                    mUserViewModel.checkUserExistsInLocalDB(mAuth.getCurrentUser().getUid());
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
}