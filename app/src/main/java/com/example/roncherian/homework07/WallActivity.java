package com.example.roncherian.homework07;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WallActivity extends MenuBarActivity implements WallRecyclerAdapter.OnClick{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    DatabaseReference mPostRef;
    FirebaseAuth mAuth;

    ValueEventListener valueEventListener;

    ArrayList<Post> posts = new ArrayList<>();
    String currentUserId, currentUserName, loggedInUserId;
    boolean isGoogleUser = false;
    ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        setTitle("My Social App");

        currentUserId = getIntent().getExtras().getString("userIdClicked");
        currentUserName = getIntent().getExtras().getString("userNameClicked");

        loggedInUserId = getIntent().getExtras().getString(MainActivity.LOGGED_IN_USER_ID);
        isGoogleUser = getIntent().getExtras().getBoolean(MainActivity.IS_GOOGLE_USER);

        mPostRef = FirebaseDatabase.getInstance().getReference("posts");
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        //DatabaseReference mChildRef=mUserRef.child(user.getUid());

        final EditText post = (EditText)findViewById(R.id.editTextPost);
        ImageButton imageButton = (ImageButton)findViewById(R.id.imageButtonSendPost);

        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerViewWallPosts);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(WallActivity.this, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        mAdapter = new WallRecyclerAdapter(posts,WallActivity.this,loggedInUserId);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        Toolbar homeToolBar = (Toolbar)findViewById(R.id.toolbarWall);


        homeToolBar.setTitle(currentUserName);

        homeToolBar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        if (currentUserId.equals(loggedInUserId) && isGoogleUser==false){
            homeToolBar.inflateMenu(R.menu.home_menu_bar_with_edit);
            homeToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.action_user_details:
                            break;

                        case R.id.action_edit_friends:

                            Intent editUserProfileIntent = new Intent(WallActivity.this, EditUserProfileActivity.class);
                            editUserProfileIntent.putExtra(MainActivity.LOGGED_IN_USER_NAME,currentUserName);
                            editUserProfileIntent.putExtra(MainActivity.LOGGED_IN_USER_ID,currentUserId);
                            startActivityForResult(editUserProfileIntent,HomeActivity.EDIT_PROFILE_ACTIVITY_REQUEST_CODE);
                            break;
                        case R.id.action_manage_friends:
                            Intent intent = new Intent(WallActivity.this, FriendsManagementActivity.class);
                            intent.putExtra(MainActivity.LOGGED_IN_USER_NAME,currentUserName);
                            intent.putExtra(MainActivity.LOGGED_IN_USER_ID,currentUserId);
                            startActivityForResult(intent,HomeActivity.FRIENDS_ACTIVITY_REQUEST_CODE);
                            break;
                        default:

                            break;
                    }
                    return false;
                }
            });
        }else {
            homeToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(WallActivity.this, FriendsManagementActivity.class);
                    intent.putExtra(MainActivity.LOGGED_IN_USER_NAME,currentUserName);
                    intent.putExtra(MainActivity.LOGGED_IN_USER_ID,currentUserId);
                    switch (item.getItemId()) {
                        case R.id.action_user_details:
                            break;
                        case R.id.action_manage_friends:


                            startActivityForResult(intent,HomeActivity.FRIENDS_ACTIVITY_REQUEST_CODE);
                            break;
                        default:

                            break;
                    }
                    return false;
                }
            });
            homeToolBar.inflateMenu(R.menu.home_menu_bar);
        }



    }

    @Override
    protected void onStart() {
        super.onStart();

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshots: dataSnapshot.getChildren()) {
                    Post post = postSnapshots.getValue(Post.class);
                    if (post.getUserId().equals(currentUserId)) {
                        posts.add(post);
                    }
                }
                mAdapter = new WallRecyclerAdapter(posts,WallActivity.this,loggedInUserId);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mPostRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPostRef.removeEventListener(valueEventListener);
    }

    @Override
    public void onUserNameClicked(int position, Post post) {

    }

    @Override
    public void OnDeleteItemClicked(int position, final Post post) {
        mPostRef.child(post.getPostId()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                posts.remove(post);
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==HomeActivity.FRIENDS_ACTIVITY_REQUEST_CODE){
            if (resultCode==RESULT_OK){
                if (data!=null && data.getExtras()!= null && data.getExtras().getBoolean("logout")){
                    Intent logoutIntent = new Intent();
                    logoutIntent.putExtra("logout",true);
                    setResult(RESULT_OK,logoutIntent);
                    finish();
                }else {
                    finish();
                }

            }
        }
    }
}
