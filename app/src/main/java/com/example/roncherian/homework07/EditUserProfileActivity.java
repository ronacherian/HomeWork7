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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditUserProfileActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private ValueEventListener valueEventListener;

    private User currentUser = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference(MainActivity.USERS);
        //mUserIds = FirebaseDatabase.getInstance().getReference(MainActivity.USER_IDS);
        mAuth = FirebaseAuth.getInstance();

        final EditText firstName = (EditText)findViewById(R.id.editTextFirstName);
        final EditText lastName = (EditText)findViewById(R.id.editTextLastName);
        final EditText dob = (EditText)findViewById(R.id.editTextDOB);
        final EditText email = (EditText)findViewById(R.id.editTextEmail);
        final EditText password = (EditText)findViewById(R.id.editTextPassword);
        final EditText confPass = (EditText)findViewById(R.id.editTextConfirmPass);

        Button updateProfileButton = (Button)findViewById(R.id.buttonUpdateUserProfile);
        Button cancelSignUp = (Button)findViewById(R.id.buttonCancelSignUp);

        email.setFocusable(false);
        email.setClickable(false);
        email.setEnabled(false);



        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstName.getText().toString().length()<=0){
                    Toast.makeText(EditUserProfileActivity.this,"Enter First Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (lastName.getText().toString().length()<=0){
                    Toast.makeText(EditUserProfileActivity.this,"Enter Last Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (dob.getText().toString().length()<=0){
                    Toast.makeText(EditUserProfileActivity.this,"Enter Date of Birth", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!MyUtils.validateDOB(dob.getText().toString())){
                    Toast.makeText(EditUserProfileActivity.this,"DOB should be (mm/dd/yyyy) and on or before today", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.getText().toString().length()<=0){
                    Toast.makeText(EditUserProfileActivity.this,"Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.getText().toString().length()<6){
                    Toast.makeText(EditUserProfileActivity.this,"Password should be minimum 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                } else if (confPass.getText().toString().length()<6){
                    Toast.makeText(EditUserProfileActivity.this,"Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!password.getText().toString().equals(confPass.getText().toString())){
                    Toast.makeText(EditUserProfileActivity.this,"Password should match confirm password", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseUser thisUser = FirebaseAuth.getInstance().getCurrentUser();
                thisUser.updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            User registeredUser = new User();
                            registeredUser.setUserId(currentUser.getUserId());
                            registeredUser.setFriends(currentUser.getFriends());
                            registeredUser.setPendingFriendRequests(currentUser.getPendingFriendRequests());
                            registeredUser.setRequestedFriends(currentUser.getPendingFriendRequests());
                            registeredUser.setName(firstName.getText().toString()+" "+lastName.getText().toString());
                            registeredUser.setDob(dob.getText().toString());

                            mDatabase.child(registeredUser.getUserId()).setValue(registeredUser);

                            Intent resultIntent = new Intent();
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();

                        } else {

                        }
                    }
                });


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final EditText firstName = (EditText)findViewById(R.id.editTextFirstName);
        final EditText lastName = (EditText)findViewById(R.id.editTextLastName);
        final EditText email = (EditText)findViewById(R.id.editTextEmail);
        final EditText dob = (EditText)findViewById(R.id.editTextDOB);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
                    if (user.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        currentUser = user;
                        firstName.setText(user.getName().substring(0,user.getName().indexOf(" ")));
                        lastName.setText(user.getName().substring(user.getName().indexOf(" ")+1));
                        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        dob.setText(user.getDob());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDatabase.removeEventListener(valueEventListener);
    }
}
