package com.vatsal.spartans.fragment;


import android.annotation.SuppressLint;
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
import com.vatsal.spartans.R;
import com.vatsal.spartans.adapter.Stock;
import com.vatsal.spartans.adapter.StockAdapter;


public class StockFragment extends Fragment {

    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v;
        v = inflater.inflate(R.layout.fragment_stock, container, false);
//        FirebaseApp.initializeApp(getActivity());

//        mDatabase = FirebaseDatabase.getInstance().getReference().child("Charts");
//        mDatabase.keepSynced(true);

        mBlogList = v.findViewById(R.id.stock_recycler);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBlogList.setAdapter(new StockAdapter(getActivity()));
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
//        FirebaseRecyclerAdapter<Stock, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Stock, BlogViewHolder>
//                (Stock.class, R.layout.stock_row, BlogViewHolder.class, mDatabase) {
//            @Override
//            protected void populateViewHolder(BlogViewHolder viewHolder, Stock model, int position) {
//                viewHolder.setTitle(model.getTitle());
//                viewHolder.setRate(model.getRate());
//                viewHolder.setImage(getActivity(), model.getImage());
//            }
//        };
//        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView company_name = mView.findViewById(R.id.stockrow_company_name);
            company_name.setText(title);
        }

        @SuppressLint("SetTextI18n")
        public void setRate(int rate) {
            TextView company_rate = mView.findViewById(R.id.stockrow_rate);
            company_rate.setText("" + rate);
        }

        public void setImage(Context ctx, String image) {
            ImageView up_down_image = mView.findViewById(R.id.stockrow_imageView);
            if (image.equals("down")) {
                up_down_image.setImageResource(R.drawable.down_arrow);
            } else {
                up_down_image.setImageResource(R.drawable.up_arrow);
            }
        }
    }

    public StockFragment() {
        // Required empty public constructor
    }
}
