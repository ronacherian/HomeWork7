package com.example.roncherian.homework07;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PendingFriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PendingFriendsFragment extends Fragment implements PendingFriendsRVAdapter.OnClick{

    private OnFragmentInteractionListener mListener;

    String loggedInUserId = "";

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    ValueEventListener valueEventListener;

    DatabaseReference mRef;

    ArrayList<User> users = new ArrayList<>();

    User currentUser = new User();

    public PendingFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle!=null){
            loggedInUserId = bundle.getString(MainActivity.LOGGED_IN_USER_ID);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending_friends, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRef = FirebaseDatabase.getInstance().getReference(MainActivity.USERS);
    }

    @Override
    public void onStart() {
        super.onStart();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();


                for (DataSnapshot userSnapshot:dataSnapshot.getChildren()) {

                    User user=userSnapshot.getValue(User.class);
                    if (user.getUserId().equals(loggedInUserId)){
                        currentUser = user;
                        break;
                    }
                }

                for (DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                    User user=userSnapshot.getValue(User.class);
                    if (currentUser.getPendingFriendRequests().contains(user.getUserId()))
                        users.add(user);
                    else if (currentUser.getRequestedFriends().contains(user.getUserId())){
                        user.setiRequested(true);
                        users.add(user);
                    }

                }

                try{
                    mRecyclerView=(RecyclerView)getActivity().findViewById(R.id.recyclerViewPendingFriends);

                    mRecyclerView.setHasFixedSize(true);

                    // use a linear layout manager
                    mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
                    mRecyclerView.setLayoutManager(mLayoutManager);


                    // specify an adapter (see also next example)
                    mAdapter = new PendingFriendsRVAdapter(users,PendingFriendsFragment.this,loggedInUserId);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    return;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mRef.removeEventListener(valueEventListener);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFriendAcceptedClicked(int position, User user) {

        if (currentUser.getPendingFriendRequests().contains(user.getUserId())){
            currentUser.getPendingFriendRequests().remove(user.getUserId());
            currentUser.getFriends().add(user.getUserId());
            mRef.child(loggedInUserId).setValue(currentUser);
        }


        //Updateing the others user profile
        if (user.getRequestedFriends().contains(loggedInUserId)){
            user.getRequestedFriends().remove(loggedInUserId);
            user.getFriends().add(loggedInUserId);
            mRef.child(user.getUserId()).setValue(user);
        }
    }

    @Override
    public void onFriendDeclineClicked(int position, User user) {

        if (user.getiRequested()){
            if (currentUser.getRequestedFriends().contains(user.getUserId())){
                currentUser.getRequestedFriends().remove(user.getUserId());
                mRef.child(loggedInUserId).setValue(currentUser);
            }

            if (user.getPendingFriendRequests().contains(loggedInUserId)){
                user.getPendingFriendRequests().remove(loggedInUserId);
                mRef.child(user.getUserId()).setValue(user);
            }

            return;
        }
        if (currentUser.getPendingFriendRequests().contains(user.getUserId())){
            currentUser.getPendingFriendRequests().remove(user.getUserId());
            mRef.child(loggedInUserId).setValue(currentUser);
        }

        if (user.getRequestedFriends().contains(loggedInUserId)){
            user.getRequestedFriends().remove(loggedInUserId);
            mRef.child(user.getUserId()).setValue(user);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
