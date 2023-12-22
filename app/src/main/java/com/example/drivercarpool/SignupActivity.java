package com.example.drivercarpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drivercarpool.model.FirebaseDB;
import com.example.drivercarpool.model.User;
import com.example.drivercarpool.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {
    private TextView loginLink;
    private Intent intentSignUp;
    private EditText signupName, signupEmail, signupUsername, signupPassword;
    private Button signUpBtn;
    private FirebaseAuth mAuth;
    private FirebaseDB firebaseDB;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        firebaseDB = FirebaseDB.getInstance();
        mAuth = FirebaseAuth.getInstance();

        loginLink = findViewById(R.id.login_link);
        signUpBtn = findViewById(R.id.signup_button);
        signupName = findViewById(R.id.name_input);
        signupEmail = findViewById(R.id.email_input);
        signupUsername = findViewById(R.id.username_input);
        signupPassword = findViewById(R.id.password_input);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(signupName.getText());
                String email = String.valueOf(signupEmail.getText());
                String username = String.valueOf(signupUsername.getText());
                String password = String.valueOf(signupPassword.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignupActivity.this,"Enter your email",Toast.LENGTH_SHORT).show();
                    signupEmail.requestFocus();
                    return;
                } else if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignupActivity.this,"Enter your password",Toast.LENGTH_SHORT).show();
                    signupPassword.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(name)) {
                    Toast.makeText(SignupActivity.this,"Enter your name",Toast.LENGTH_SHORT).show();
                    signupName.requestFocus();
                    return;
                }else if (TextUtils.isEmpty(username)) {
                    Toast.makeText(SignupActivity.this,"Enter your username",Toast.LENGTH_SHORT).show();
                    signupUsername.requestFocus();
                    return;
                }

                if (!email.endsWith("@eng.asu.edu.eg")) {
                    Toast.makeText(SignupActivity.this, "Invalid email domain. Please use @eng.asu.edu.eg", Toast.LENGTH_LONG).show();
                    return;
                }

                firebaseDB.signUp(name, email, username, password, mAuth, getApplicationContext(), new FirebaseDB.SignUpCallback() {
                    @Override
                    public void onSignUpSuccess() {
                        User user1 = new User(mAuth.getCurrentUser().getUid(), email, username, name);
                        mUserViewModel.insert(user1);
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onSignUpFailure(String errorMessage) {
                        Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSignUp = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intentSignUp);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent1 = new Intent(SignupActivity.this,MainActivity.class);
            startActivity(intent1);
            finish();
        }
    }
}