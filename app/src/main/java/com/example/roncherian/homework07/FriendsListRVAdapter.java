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

public class FriendsListRVAdapter extends RecyclerView.Adapter<FriendsListRVAdapter.ViewHolder>{

    FriendsListRVAdapter.OnClick onClick;

    static ArrayList<User> mData;
    static String userId;
    // Context context;

    static MainActivity mainActivity;

    public FriendsListRVAdapter(ArrayList<User> mData, FriendsListRVAdapter.OnClick context, String loggedInUserId) {
        this.mData = mData;
        this.onClick = context;
        this.userId = loggedInUserId;
    }


    @Override
    public FriendsListRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_list_layout, parent, false);
        FriendsListRVAdapter.ViewHolder vh = new FriendsListRVAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FriendsListRVAdapter.ViewHolder holder, int position) {


        User userObj = mData.get(position);
        holder.user = userObj;
        holder.name.setText(userObj.getName());
        holder.position = position;


    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        ImageButton removeFriend;


        int position;
        User user;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.textViewUserName);
            removeFriend = (ImageButton)itemView.findViewById(R.id.imageButtonRemoveFriend);

            removeFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClick!=null){
                        onClick.OnRemoveFriendClicked(position,user);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onClick.OnItemClicked(position, user);
                    return false;
                }
            });



        }
    }

    public interface OnClick{
        public void OnItemClicked(int position, User user);
        public void OnRemoveFriendClicked(int position, User user);
    }


}