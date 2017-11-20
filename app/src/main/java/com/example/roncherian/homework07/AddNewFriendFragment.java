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
 * {@link AddNewFriendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddNewFriendFragment extends Fragment implements AddNewFriendRVAdapter.OnClick {

    private OnFragmentInteractionListener mListener;

    ValueEventListener valueEventListener;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    String loggedInUserId = "";
    DatabaseReference mUserRef;
    ArrayList<User> users = new ArrayList<>();

    User currentUser = new User();
    public AddNewFriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserRef = FirebaseDatabase.getInstance().getReference(MainActivity.USERS);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //mUserRef.addListenerForSingleValueEvent();

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
                    if (!user.getUserId().equals(loggedInUserId) && !currentUser.getPendingFriendRequests().contains(user.getUserId())
                            && !currentUser.getRequestedFriends().contains(user.getUserId()) &&
                            !currentUser.getFriends().contains(user.getUserId()))
                        users.add(user);

                }

                try {
                    mRecyclerView=(RecyclerView)getActivity().findViewById(R.id.recyclerViewAddNewFriends);
                    mRecyclerView.setHasFixedSize(true);

                    // use a linear layout manager
                    mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
                    mRecyclerView.setLayoutManager(mLayoutManager);


                    // specify an adapter (see also next example)
                    mAdapter = new AddNewFriendRVAdapter(users,AddNewFriendFragment.this,loggedInUserId);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e){
                    return;
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mUserRef.addValueEventListener(valueEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        if (bundle!=null){
            loggedInUserId = bundle.getString(MainActivity.LOGGED_IN_USER_ID);
        }
        return inflater.inflate(R.layout.fragment_add_new_friend, container, false);
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
    public void OnItemClicked(int position, User user) {

    }

    @Override
    public void onAddNewFriendClicked(int position, User user) {

        if (!currentUser.getRequestedFriends().contains(user.getUserId())){
            currentUser.getRequestedFriends().add(user.getUserId());
            mUserRef.child(loggedInUserId).setValue(currentUser);
        }


       //Updateing the others user profile
        if (!user.getPendingFriendRequests().contains(loggedInUserId)){
            user.getPendingFriendRequests().add(loggedInUserId);
            mUserRef.child(user.getUserId()).setValue(user);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        mUserRef.removeEventListener(valueEventListener);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
