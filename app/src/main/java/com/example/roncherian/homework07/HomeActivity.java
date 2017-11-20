package com.example.roncherian.homework07;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends MenuBarActivity implements PostsRecyclerAdapter.OnClick{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static int FRIENDS_ACTIVITY_REQUEST_CODE = 200;
    public static int EDIT_PROFILE_ACTIVITY_REQUEST_CODE = 250;

    DatabaseReference mPostRef;
    FirebaseAuth mAuth;

    ArrayList<Post>posts = new ArrayList<>();

    ChildEventListener childEventListener;
    ValueEventListener valueEventListener;

    String loggedInUserId, loggedInUserName;
    boolean isGoogleUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("My Social App");

        mPostRef = FirebaseDatabase.getInstance().getReference("posts");
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        //DatabaseReference mChildRef=mUserRef.child(user.getUid());

        final EditText post = (EditText)findViewById(R.id.editTextPost);
        ImageButton imageButton = (ImageButton)findViewById(R.id.imageButtonSendPost);

        loggedInUserId = getIntent().getExtras().getString(MainActivity.LOGGED_IN_USER_ID);
        loggedInUserName = getIntent().getExtras().getString(MainActivity.LOGGED_IN_USER_NAME);
        isGoogleUser = getIntent().getExtras().getBoolean(MainActivity.IS_GOOGLE_USER);

        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerViewPosts);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        mAdapter = new PostsRecyclerAdapter(posts,HomeActivity.this,loggedInUserId);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        /*mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                posts.clear();

                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()) {

                    Post post=postSnapshot.getValue(Post.class);
                    posts.add(post);

                }

                mRecyclerView=(RecyclerView)findViewById(R.id.recyclerViewPosts);

                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL,false);
                mRecyclerView.setLayoutManager(mLayoutManager);


                // specify an adapter (see also next example)
                mAdapter = new PostsRecyclerAdapter(posts,HomeActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (post.getText().toString().length() == 0){
                    Toast.makeText(HomeActivity.this,"Enter value in text",Toast.LENGTH_SHORT).show();
                } else {
                    String postId=mPostRef.push().getKey();
                    Post post1 = new Post();
                    post1.setPostId(postId);
                    post1.setPost(post.getText().toString());
                    post1.setTime(getMyDateString());
                    post1.setUserName(loggedInUserName);
                    post1.setUserId(loggedInUserId);
                    mPostRef.child(postId).setValue(post1);
                    post.setText("");
                }
            }
        });

        Toolbar homeToolBar = (Toolbar)findViewById(R.id.toolbarHome);
        homeToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(HomeActivity.this, FriendsManagementActivity.class);
                intent.putExtra(MainActivity.LOGGED_IN_USER_NAME,loggedInUserName);
                intent.putExtra(MainActivity.LOGGED_IN_USER_ID,loggedInUserId);
                switch (item.getItemId()) {
                    case R.id.action_user_details:
                        //startActivityForResult(intent,FRIENDS_ACTIVITY_REQUEST_CODE);
                        break;
                    case R.id.action_manage_friends:


                        startActivityForResult(intent,FRIENDS_ACTIVITY_REQUEST_CODE);
                        break;
                    default:

                        break;
                }
                return false;
            }
        });

        if (user == null){
            homeToolBar.setTitle(loggedInUserName);
        } else {
            homeToolBar.setTitle(user.getDisplayName());
        }

        homeToolBar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        homeToolBar.inflateMenu(R.menu.home_menu_bar);


    }

    @Override
    protected void onStart() {
        super.onStart();
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post post = dataSnapshot.getValue(Post.class);
                posts.add(post);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                posts.remove(post);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mPostRef.addChildEventListener(childEventListener);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posts.clear();

                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()) {

                    Post post=postSnapshot.getValue(Post.class);
                    posts.add(post);

                }

                mRecyclerView=(RecyclerView)findViewById(R.id.recyclerViewPosts);

                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL,false);
                mRecyclerView.setLayoutManager(mLayoutManager);


                // specify an adapter (see also next example)
                mAdapter = new PostsRecyclerAdapter(posts,HomeActivity.this,loggedInUserId);
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
    public void onUserNameClicked(int position, final Post passedPost) {

        Intent intent = new Intent(HomeActivity.this, WallActivity.class);
        intent.putExtra("userIdClicked",passedPost.getUserId());
        intent.putExtra("userNameClicked",passedPost.getUserName());
        intent.putExtra(MainActivity.LOGGED_IN_USER_ID,loggedInUserId);
        intent.putExtra(MainActivity.IS_GOOGLE_USER,isGoogleUser);
        startActivityForResult(intent,501);

        /*posts.clear();
        mPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshots: dataSnapshot.getChildren()) {
                    Post post = postSnapshots.getValue(Post.class);
                    if (post.getUserId().equals(passedPost.getUserId())) {
                        posts.add(post);
                    }
                }
                mAdapter = new PostsRecyclerAdapter(posts,HomeActivity.this,loggedInUserId);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }

    @Override
    public void OnDeleteItemClicked(final int position, final Post post) {
        mPostRef.child(post.getPostId()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                posts.remove(post);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private String getMyDateString(){
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);


    }

    @Override
    protected void onStop() {
        super.onStop();
        mPostRef.removeEventListener(childEventListener);
        mPostRef.removeEventListener(valueEventListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (data!=null && data.getExtras()!=null){
                Boolean isLogout = data.getExtras().getBoolean("logout");
                if (isLogout!=null && isLogout){
                    finish();
                }
            }

        }

    }

}
