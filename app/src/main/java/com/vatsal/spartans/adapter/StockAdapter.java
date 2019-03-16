package com.vatsal.spartans.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vatsal.spartans.FirebaseHelper;
import com.vatsal.spartans.R;
import com.vatsal.spartans.models.Company;

import java.util.List;


public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {
    private Activity activity;
    private List<Company> list;
    private FirebaseHelper helper;

    public StockAdapter(Activity activity) {
        this.activity = activity;
        helper = FirebaseHelper.getInstance();
        list = helper.getCompanyList();
        helper.setStockAdapter(this);
        notifyDataSetChanged();
    }
    public void update(){
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_row, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        try {
            Company company = list.get(i);
            viewHolder.nameText.setText(company.getTitle());
            viewHolder.rateText.setText("" + company.getRate());
            if (company.image.equals("down")) {
                viewHolder.imageView.setImageResource(R.drawable.down_arrow);
            } else {
                viewHolder.imageView.setImageResource(R.drawable.up_arrow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return  0;
        return list.size();
    }

    public void update(int pos, Company company) {
        list.set(pos, company);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rateText, nameText;
        ImageView imageView;
        public ViewHolder(@NonNull View view) {
            super(view);
            rateText = view.findViewById(R.id.stockrow_rate);
            nameText = view.findViewById(R.id.stockrow_company_name);
            imageView = view.findViewById(R.id.stockrow_imageView);
        }
    }
}
