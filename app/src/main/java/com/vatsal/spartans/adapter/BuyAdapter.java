package com.vatsal.spartans.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vatsal.spartans.FirebaseHelper;
import com.vatsal.spartans.R;
import com.vatsal.spartans.models.Company;
import com.vatsal.spartans.models.Stock;

import java.util.List;


public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.ViewHolder> {

    List<Company> list;
    Activity activity;
    FirebaseHelper helper;

    public BuyAdapter(Activity activity) {
        this.activity = activity;
         helper = FirebaseHelper.getInstance();
        list = helper.getCompanyList();
        helper.setBuyAdapter(this);
        notifyDataSetChanged();
    }

    public interface OnBuyClick {
        void onClick(String id);
    }

    public interface OnSellClick {
        void onClick(String id);
    }

    private OnBuyClick onBuyClick;
    private OnSellClick onSellClick;

    public OnBuyClick getOnBuyClick() {
        return onBuyClick;
    }

    public void setOnBuyClick(OnBuyClick onBuyClick) {
        this.onBuyClick = onBuyClick;
    }

    public OnSellClick getOnSellClick() {
        return onSellClick;
    }

    public void setOnSellClick(OnSellClick onSellClick) {
        this.onSellClick = onSellClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_row, parent, false);
        return new BuyAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Company company = list.get(i);
        viewHolder.titleTextView.setText(company.getTitle());
        viewHolder.titleTextView.setTag(company.getId());
        int count = 0;
        for (Stock stock :helper.boughtStocks ){
            if (stock.getId().equals(company.getId())){
                count++;
            }
        }
        if (count == 0){
            viewHolder.qunTextView.setText("");
        } else {
            viewHolder.qunTextView.setText("Quantity: " + count);
        }
        viewHolder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBuyClick.onClick(company.id);
            }
        });
        viewHolder.sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSellClick.onClick(company.id);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
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
        TextView titleTextView, qunTextView;
        Button sellButton, buyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.buyrow_nameText);
            sellButton = itemView.findViewById(R.id.buyrow_sellButton);
            buyButton = itemView.findViewById(R.id.buyrow_buyButton);
            qunTextView = itemView.findViewById(R.id.buyrow_quantityText);
        }
    }
}
