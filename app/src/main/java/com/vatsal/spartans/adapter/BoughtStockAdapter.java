package com.vatsal.spartans.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vatsal.spartans.FirebaseHelper;
import com.vatsal.spartans.R;
import com.vatsal.spartans.models.Company;
import com.vatsal.spartans.models.Stock;

/**
 * Mock Stock
 * Created by Yash on 9/15/2018.
 */
public class BoughtStockAdapter extends RecyclerView.Adapter<BoughtStockAdapter.ViewHolder> {
    Context context;
    FirebaseHelper helper;

    public BoughtStockAdapter(Context context) {
        helper = FirebaseHelper.getInstance();
        notifyDataSetChanged();
        this.context = context;
        helper.boughtStockAdapter = this;
    }

    public void update(){
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stockvalue_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Stock stock = helper.boughtStocks.get(i);
        Company company = helper.getCompany(stock.getId());
        if (company != null) {
            viewHolder.nameText.setText(company.getTitle());
            viewHolder.amountText.setText(String.valueOf(company.getRate()));
        }
    }

    public int convertDpToPixel(int dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    @Override
    public int getItemCount() {
        return helper.boughtStocks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, amountText;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            nameText = itemView.findViewById(R.id.stockvalue_name);
            amountText = itemView.findViewById(R.id.stockvalue_amount);
        }
    }
}
