package com.example.roncherian.homework07;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostsRecyclerAdapter.ViewHolder>{

    OnClick onClick;

    static ArrayList<Post> mData;
    static String userId;
    // Context context;

    static MainActivity mainActivity;

    public PostsRecyclerAdapter(ArrayList<Post> mData, OnClick context, String loggedInUserId) {
        this.mData = mData;
        this.onClick = context;
        this.userId = loggedInUserId;
    }


    @Override
    public PostsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posts_layout_home, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PostsRecyclerAdapter.ViewHolder holder, int position) {


        Post postObj = mData.get(position);
        holder.post = postObj;
        holder.name.setText(postObj.getUserName());
        holder.time.setText(getMyPrettyPostTime(postObj.getTime()));
        holder.postDetails.setText(postObj.getPost());
        holder.position = position;

        holder.deletePost.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView time;
        TextView postDetails;
        ImageButton deletePost;


        int position;
        Post post;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.textViewPosterName);
            time = (TextView)itemView.findViewById(R.id.textViewPostedTime);
            postDetails = (TextView)itemView.findViewById(R.id.textViewPost);
            deletePost = (ImageButton)itemView.findViewById(R.id.imageButtonDecline);

            deletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClick!=null){
                        onClick.OnDeleteItemClicked(position,post);
                    }
                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("demo","Clicked on user name");
                    if (onClick!=null){
                        onClick.onUserNameClicked(position,post);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //onClick.OnItemClicked(position, post);
                    return false;
                }
            });



        }
    }

    public interface OnClick{
        public void onUserNameClicked(int position, Post post);
        public void OnDeleteItemClicked(int position, Post post);
    }

    private String getMyPrettyPostTime(String dateString){
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new PrettyTime().format(date);
    }

}
