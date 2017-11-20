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

public class AddNewFriendRVAdapter  extends RecyclerView.Adapter<AddNewFriendRVAdapter.ViewHolder>{

    AddNewFriendRVAdapter.OnClick onClick;

    static ArrayList<User> mData;
    static String userId;
    // Context context;

    static MainActivity mainActivity;

    public AddNewFriendRVAdapter(ArrayList<User> mData, AddNewFriendRVAdapter.OnClick context, String loggedInUserId) {
        this.mData = mData;
        this.onClick = context;
        this.userId = loggedInUserId;
    }


    @Override
    public AddNewFriendRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_new_friend_list_layout, parent, false);
        AddNewFriendRVAdapter.ViewHolder vh = new AddNewFriendRVAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AddNewFriendRVAdapter.ViewHolder holder, int position) {


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

        ImageButton removeFirend;


        int position;
        User user;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.textViewUserName);
            removeFirend = (ImageButton)itemView.findViewById(R.id.imageButtonRemoveFriend);

            removeFirend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClick!=null){
                        onClick.onAddNewFriendClicked(position,user);
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
        public void onAddNewFriendClicked(int position, User user);
    }


}