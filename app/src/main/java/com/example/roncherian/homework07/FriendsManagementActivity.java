package com.example.roncherian.homework07;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import info.hoang8f.android.segmented.SegmentedGroup;

public class FriendsManagementActivity extends MenuBarActivity implements FriendsFragment.OnFragmentInteractionListener, AddNewFriendFragment.OnFragmentInteractionListener, PendingFriendsFragment.OnFragmentInteractionListener{

    static String FRIENDS_FRAGMENT = "friends_fragment";
    static String ADD_NEW_FRIEND_FRAGMENT = "add_new_friend_fragment";
    static String PENDING_FRIEND_REQUEST_FRAGMENT = "pending_friend_requests_fragment";
    public static String LOGGED_IN_USER_ID = "user_id";

    String loggedInUserId = "", loggedInUserName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_management);
        setTitle("My Social App");
        //manageButtonClicks();

        manageSegmentedControls();

        loggedInUserId = getIntent().getExtras().getString(MainActivity.LOGGED_IN_USER_ID);
        loggedInUserName = getIntent().getExtras().getString(MainActivity.LOGGED_IN_USER_NAME);
        Toolbar homeToolBar = (Toolbar)findViewById(R.id.toolbarFriends);
        homeToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Intent intent = new Intent(HomeActivity.this, FriendsManagementActivity.class);

                //intent.putExtra(MainActivity.LOGGED_IN_USER_ID,loggedInUserId);
                switch (item.getItemId()) {
                    case R.id.action_user_details:
                        //startActivityForResult(intent,FRIENDS_ACTIVITY_REQUEST_CODE);
                        break;
                    case R.id.action_home:
                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                        //startActivityForResult(intent,FRIENDS_ACTIVITY_REQUEST_CODE);
                        break;
                    default:

                        break;
                }
                return false;
            }
        });

        /*if (user == null){
            homeToolBar.setTitle(loggedInUserName);
        } else {

        }*/
        homeToolBar.setTitle("Friends");
        homeToolBar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        homeToolBar.inflateMenu(R.menu.friends_menu_bar);

        AddNewFriendFragment addNewFriendFragment = new AddNewFriendFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LOGGED_IN_USER_ID,loggedInUserId);
        addNewFriendFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.containerView, addNewFriendFragment, FRIENDS_FRAGMENT).commit();

    }

    private void manageSegmentedControls(){
        SegmentedGroup segmented2 = (SegmentedGroup)findViewById(R.id.segmented2);

        RadioButton button1 = (RadioButton)findViewById(R.id.buttonFriends);
        RadioButton button2 = (RadioButton)findViewById(R.id.buttonAddNewFriend);
        RadioButton button3 = (RadioButton)findViewById(R.id.buttonRequestPending);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("demo","On Checked button1");
                FriendsFragment friendsFragment = new FriendsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(LOGGED_IN_USER_ID,loggedInUserId);
                friendsFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.containerView, friendsFragment, FRIENDS_FRAGMENT).commit();

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("demo","On Checked button2");
                AddNewFriendFragment addNewFriendFragment = new AddNewFriendFragment();
                Bundle bundle = new Bundle();
                bundle.putString(LOGGED_IN_USER_ID,loggedInUserId);
                addNewFriendFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.containerView, addNewFriendFragment, FRIENDS_FRAGMENT).commit();

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("demo","On Checked button3");
                PendingFriendsFragment pendingFriendsFragment = new PendingFriendsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(LOGGED_IN_USER_ID,loggedInUserId);
                pendingFriendsFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.containerView, pendingFriendsFragment, FRIENDS_FRAGMENT).commit();

            }
        });
        button2.setChecked(true);

    }
    private void manageButtonClicks(){
        final Button friendsButton = (Button)findViewById(R.id.buttonFriends);
        final Button addNewFriendButton = (Button)findViewById(R.id.buttonAddNewFriend);
        final Button pendingReqButton = (Button)findViewById(R.id.buttonRequestPending);

        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FriendsFragment friendsFragment = new FriendsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(LOGGED_IN_USER_ID,loggedInUserId);
                friendsFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.containerView, friendsFragment, FRIENDS_FRAGMENT).commit();

                //getFragmentManager().beginTransaction().replace(R.id.containerView,new FriendsFragment(), FRIENDS_FRAGMENT).commit();
            }
        });

        addNewFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddNewFriendFragment addNewFriendFragment = new AddNewFriendFragment();
                Bundle bundle = new Bundle();
                bundle.putString(LOGGED_IN_USER_ID,loggedInUserId);
                addNewFriendFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.containerView, addNewFriendFragment, FRIENDS_FRAGMENT).commit();

//                getFragmentManager().beginTransaction().replace(R.id.containerView,new AddNewFriendFragment(), ADD_NEW_FRIEND_FRAGMENT).commit();
            }
        });

        pendingReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PendingFriendsFragment pendingFriendsFragment = new PendingFriendsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(LOGGED_IN_USER_ID,loggedInUserId);
                pendingFriendsFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.containerView, pendingFriendsFragment, FRIENDS_FRAGMENT).commit();

                //getFragmentManager().beginTransaction().replace(R.id.containerView,new PendingFriendsFragment(), PENDING_FRIEND_REQUEST_FRAGMENT).commit();
            }
        });
    }

    private void manageButtonClickssss(){
        final Button friendsButton = (Button)findViewById(R.id.buttonFriends);
        final Button addNewFriendButton = (Button)findViewById(R.id.buttonAddNewFriend);
        final Button pendingReqButton = (Button)findViewById(R.id.buttonRequestPending);

        //LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayoutButtons);
        //linearLayout.findViewById(R.id.buttonRequestPending).getWidth();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(240,144);
        friendsButton.setLayoutParams(layoutParams);

        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(240,144);
                friendsButton.setLayoutParams(layoutParams);

                LinearLayout.LayoutParams unselectedLayoutParams = new LinearLayout.LayoutParams(250,150);
                addNewFriendButton.setLayoutParams(unselectedLayoutParams);

                LinearLayout.LayoutParams pendReqLayoutParams = new LinearLayout.LayoutParams(250,150);
                addNewFriendButton.setLayoutParams(pendReqLayoutParams);
            }
        });

        addNewFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(240,144);
                friendsButton.setLayoutParams(layoutParams);

                LinearLayout.LayoutParams unselectedLayoutParams = new LinearLayout.LayoutParams(addNewFriendButton.getWidth(),40);
                addNewFriendButton.setLayoutParams(unselectedLayoutParams);

                LinearLayout.LayoutParams pendReqLayoutParams = new LinearLayout.LayoutParams(addNewFriendButton.getWidth(),50);
                addNewFriendButton.setLayoutParams(pendReqLayoutParams);
            }
        });

        pendingReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(friendsButton.getWidth(),50);
                friendsButton.setLayoutParams(layoutParams);

                LinearLayout.LayoutParams unselectedLayoutParams = new LinearLayout.LayoutParams(addNewFriendButton.getWidth(),50);
                addNewFriendButton.setLayoutParams(unselectedLayoutParams);

                LinearLayout.LayoutParams pendReqLayoutParams = new LinearLayout.LayoutParams(addNewFriendButton.getWidth(),40);
                addNewFriendButton.setLayoutParams(pendReqLayoutParams);
            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
