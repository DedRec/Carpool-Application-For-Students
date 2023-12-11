package com.example.drivercarpool;

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
    TextView loginLink;
    Intent intentSignUp;
    EditText signupName, signupEmail, signupUsername, signupPassword;
    Button signUpBtn;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("drivers");

                String name = String.valueOf(signupName.getText());
                String email = String.valueOf(signupEmail.getText());
                String username = String.valueOf(signupUsername.getText());
                String password = String.valueOf(signupPassword.getText());


                Query checkUserDatabase = reference.orderByChild("username").equalTo(username);

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

                checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            signupUsername.setError("Username already exists");
                        }else{
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = mAuth.getCurrentUser();

                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(name)
                                                        .build();

                                                user.updateProfile(profileUpdates);

                                                Log.d("FBAuth", "createUserWithEmail:success");
                                                Toast.makeText(SignupActivity.this, "Account created successfully.",
                                                        Toast.LENGTH_SHORT).show();
                                                Helper helper = new Helper(name,email,username,password,user.getUid());
                                                reference.child(user.getUid()).setValue(helper);
                                                intentSignUp = new Intent(SignupActivity.this,LoginActivity.class);
                                                startActivity(intentSignUp);
                                                finish();
                                            } else {
                                                Log.w("FBAuth", "createUserWithEmail:failure", task.getException());
                                                Toast.makeText(SignupActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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