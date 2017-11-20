package com.example.roncherian.homework07;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private DatabaseReference mDatabase, mUserIds;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabase = FirebaseDatabase.getInstance().getReference(MainActivity.USERS);
        mUserIds = FirebaseDatabase.getInstance().getReference(MainActivity.USER_IDS);
        mAuth = FirebaseAuth.getInstance();

        final EditText firstName = (EditText)findViewById(R.id.editTextFirstName);
        final EditText lastName = (EditText)findViewById(R.id.editTextLastName);
        final EditText dob = (EditText)findViewById(R.id.editTextDOB);
        final EditText email = (EditText)findViewById(R.id.editTextEmail);
        final EditText password = (EditText)findViewById(R.id.editTextPassword);
        final EditText confPass = (EditText)findViewById(R.id.editTextConfirmPass);

        Button signUpButton = (Button)findViewById(R.id.buttonUpdateUserProfile);
        Button cancelSignUp = (Button)findViewById(R.id.buttonCancelSignUp);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstName.getText().toString().length()<=0){
                    Toast.makeText(SignUpActivity.this,"Enter First Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (lastName.getText().toString().length()<=0){
                    Toast.makeText(SignUpActivity.this,"Enter Last Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.getText().toString().length()<=0){
                    Toast.makeText(SignUpActivity.this,"Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!MyUtils.validateEmail(email.getText().toString())){
                    Toast.makeText(SignUpActivity.this,"Enter Valid Email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (dob.getText().toString().length()<=0){
                    Toast.makeText(SignUpActivity.this,"Enter Date of Birth", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!MyUtils.validateDOB(dob.getText().toString())){
                    Toast.makeText(SignUpActivity.this,"DOB should be (mm/dd/yyyy) and on or before today", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.getText().toString().length()<=0){
                    Toast.makeText(SignUpActivity.this,"Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.getText().toString().length()<6){
                    Toast.makeText(SignUpActivity.this,"Password should be minimum 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                } else if (confPass.getText().toString().length()<6){
                    Toast.makeText(SignUpActivity.this,"Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!password.getText().toString().equals(confPass.getText().toString())){
                    Toast.makeText(SignUpActivity.this,"Password should match confirm password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(SignUpActivity.this,"User Successfully created",Toast.LENGTH_SHORT).show();
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("signupstatus", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(firstName.getText().toString()+" "+lastName.getText().toString()).build();
                                    user.updateProfile(profileUpdates);


                                    User registeredUser = new User();
                                    registeredUser.setUserId(user.getUid());
                                    registeredUser.setName(firstName.getText().toString()+" "+lastName.getText().toString());
                                    mDatabase.child(registeredUser.getUserId()).setValue(registeredUser);
                                    mUserIds.child(registeredUser.getUserId()).setValue(true);

                                    Intent resultIntent = new Intent();
                                    setResult(Activity.RESULT_OK, resultIntent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("signupstatus", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


            }
        });
        cancelSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
