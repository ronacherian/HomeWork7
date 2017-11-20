package com.example.roncherian.homework07;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by roncherian on 18/11/17.
 */

public class PendingFriendsRVAdapter extends RecyclerView.Adapter<PendingFriendsRVAdapter.ViewHolder>{

    PendingFriendsRVAdapter.OnClick onClick;

    static ArrayList<User> mData;
    static String userId;
    // Context context;

    static MainActivity mainActivity;

    public PendingFriendsRVAdapter(ArrayList<User> mData, PendingFriendsRVAdapter.OnClick context, String loggedInUserId) {
        this.mData = mData;
        this.onClick = context;
        this.userId = loggedInUserId;
    }


    @Override
    public PendingFriendsRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pending_friends_list_layout, parent, false);
        PendingFriendsRVAdapter.ViewHolder vh = new PendingFriendsRVAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PendingFriendsRVAdapter.ViewHolder holder, int position) {


        User userObj = mData.get(position);
        holder.user = userObj;
        holder.name.setText(userObj.getName());
        holder.position = position;

        if (userObj.getiRequested()){
            holder.acceptRequest.setVisibility(View.INVISIBLE);
        } else {
            holder.acceptRequest.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        ImageButton acceptRequest, declineRequest;


        int position;
        User user;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.textViewUserName);
            acceptRequest = (ImageButton)itemView.findViewById(R.id.imageButtonAccept);
            declineRequest = (ImageButton)itemView.findViewById(R.id.imageButtonDecline);

            acceptRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClick!=null){
                        onClick.onFriendAcceptedClicked(position,user);
                    }
                }
            });

            declineRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClick!=null){
                        onClick.onFriendDeclineClicked(position,user);
                    }
                }
            });
            /*itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onClick.onFriendAcceptedClicked(position, user);
                    return false;
                }
            });*/



        }
    }

    public interface OnClick{
        public void onFriendAcceptedClicked(int position, User user);
        public void onFriendDeclineClicked(int position, User user);
    }


}
