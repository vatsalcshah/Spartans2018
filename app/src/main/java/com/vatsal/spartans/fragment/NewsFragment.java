package com.vatsal.spartans.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.vatsal.spartans.adapter.News;
import com.vatsal.spartans.R;


public class NewsFragment extends Fragment {

    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v;
        v = inflater.inflate(R.layout.fragment_news, container, false);
        FirebaseApp.initializeApp(getActivity());

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Global");
        mDatabase.keepSynced(true);

        mBlogList = v.findViewById(R.id.news_recycler);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<News, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, BlogViewHolder>
                (News.class, R.layout.blog_row, BlogViewHolder.class, mDatabase) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, News model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getActivity(), model.getImage());

            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setTitle(String title) {
            TextView news_title = mView.findViewById(R.id.stockrow_company_name);
            news_title.setText(title);
        }

        public void setDesc(String desc) {
            TextView news_desc = mView.findViewById(R.id.news_description);
            news_desc.setText(desc);
        }

        public void setImage(Context ctx, String image) {
            ImageView news_image = mView.findViewById(R.id.stockrow_imageView);
            Picasso.with(ctx).load(image).into(news_image);
        }
    }

    public NewsFragment() {
        // Required empty public constructor
    }
}

