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
 * {@link FriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FriendsFragment extends Fragment implements FriendsListRVAdapter.OnClick {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    DatabaseReference mRef;
    ArrayList<User>users = new ArrayList<>();
    String loggedInUserId = "";

    User currentUser = new User();

    private OnFragmentInteractionListener mListener;

    public FriendsFragment() {
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
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mRef = FirebaseDatabase.getInstance().getReference(MainActivity.USERS);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRef.addValueEventListener(new ValueEventListener() {
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

                for (DataSnapshot userSnapshot:dataSnapshot.getChildren()) {

                    User user=userSnapshot.getValue(User.class);
                    if (currentUser.getFriends().contains(user.getUserId()))
                        users.add(user);

                }
                try {
                    mRecyclerView=(RecyclerView)getActivity().findViewById(R.id.recyclerViewFriends);

                    mRecyclerView.setHasFixedSize(true);

                    // use a linear layout manager
                    mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
                    mRecyclerView.setLayoutManager(mLayoutManager);


                    // specify an adapter (see also next example)
                    mAdapter = new FriendsListRVAdapter(users,FriendsFragment.this,loggedInUserId);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    return;
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView=(RecyclerView)getActivity().findViewById(R.id.recyclerViewFriends);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        //ingredientList.add("");
        mAdapter = new FriendsListRVAdapter(new ArrayList<User>(),FriendsFragment.this, loggedInUserId);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
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
    public void OnRemoveFriendClicked(int position, User user) {

        if (currentUser.getFriends().contains(user.getUserId())){
            currentUser.getFriends().remove(user.getUserId());
            mRef.child(loggedInUserId).setValue(currentUser);
        }

        if (user.getFriends().contains(loggedInUserId)){
            user.getFriends().remove(loggedInUserId);
            mRef.child(user.getUserId()).setValue(user);
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
