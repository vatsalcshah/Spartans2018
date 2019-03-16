package com.vatsal.spartans.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vatsal.spartans.FirebaseHelper;
import com.vatsal.spartans.R;
import com.vatsal.spartans.adapter.BuyAdapter;
import com.vatsal.spartans.models.Company;


public class BuyFragment extends Fragment {
    RecyclerView recyclerView;
    EditText quantityEditText;
    public FrameLayout cardView;
    Button confirmButton;
    TextView buySellText, companyText, amountAfterText, amountBeforeText, amountText;
    FirebaseHelper helper;
    View alphaView;
    BuyAdapter adapter;

    boolean isSell = false;

    public BuyFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy, container, false);
        recyclerView = view.findViewById(R.id.buy_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cardView = view.findViewById(R.id.buy_card);
        confirmButton = view.findViewById(R.id.buy_cardConfirmButton);
        buySellText = view.findViewById(R.id.buy_cardBuySellText);
        companyText = view.findViewById(R.id.buy_cardCompanyText);
        amountText = view.findViewById(R.id.buy_cardAmountText);
        amountAfterText = view.findViewById(R.id.buy_cardAmountAfter);
        amountBeforeText = view.findViewById(R.id.buy_cardAmountBefore);
        quantityEditText = view.findViewById(R.id.buy_cardQunEditText);

        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(quantityEditText.getText().toString())) {
                    try {
                        int i = Integer.parseInt(quantityEditText.getText().toString());
                        Company company = helper.getCompany(companyID);
                        if (isSell) {
                            int count = 0;
                            for (int j = 0; j < helper.boughtStocks.size(); j++) {
                                if (helper.boughtStocks.get(j).getId().equals(company.getId()))
                                    count++;
                            }
                            if (i > count) {
                                quantityEditText.setText("" + count);

                            } else {
                                amountBeforeText.setText("" + helper.amount);
                                amountText.setText("" + helper.amount);
                                amountAfterText.setText("" + (helper.amount + (company.getRate() * i)));
                            }
                        } else {
                            if (helper.amount - (company.getRate() * i) < 0) {

                            } else {
                                amountBeforeText.setText("" + helper.amount);
                                amountText.setText("" + (company.getRate() * i));
                                amountAfterText.setText("" + (helper.amount - ((company.getRate() * i))));
                            }
                        }
                    } catch (Exception e) {
                    }
                } else {
                }
            }
        });

        alphaView = view.findViewById(R.id.buy_alphamask);
        alphaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideCard();
                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });
        init();
        helper = FirebaseHelper.getInstance();

        return view;
    }

    String companyID = "";

    @SuppressLint("SetTextI18n")
    void buy(String id) {
        isSell = false;
        companyID = id;
        Company company = helper.getCompany(id);

        if (helper.amount - company.rate < 0) {
            Toast.makeText(getContext(), "Not enough money!", Toast.LENGTH_SHORT).show();
            companyID = "";
            return;
        }
        amountText.setText(": " + company.getRate());
        companyText.setText(company.getTitle());
        amountAfterText.setText(": " + helper.amount);
        amountBeforeText.setText(": " + (helper.amount - company.rate));
        quantityEditText.setText("1");
        showCard(true);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int i = Integer.parseInt(quantityEditText.getText().toString());
                    Company company = helper.getCompany(companyID);

                    if (helper.amount - (company.getRate() * i) < 0) {
                        Toast.makeText(getContext(), "Not enough money!", Toast.LENGTH_SHORT).show();
                    } else {
                        amountBeforeText.setText("" + helper.amount);
                        amountText.setText("" + (company.getRate() * i));
                        amountAfterText.setText("" + (helper.amount - ((company.getRate() * i))));

                        for (int it = 0; it < i; it++) {
                            helper.buy(companyID, (helper.amount - ((company.getRate() * i))));
                        }
                    }
                    hideCard();
                    companyID = "";
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    quantityEditText.setText("1");
                }

                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void sell(String id) throws Exception {
        isSell = true;
        companyID = id;
        final Company company = helper.getCompany(id);

        final int qun = Integer.parseInt(quantityEditText.getText().toString());
        int rQun = 0;
        for (int z = 0; z < helper.boughtStocks.size(); z++) {
            rQun++;
        }

        if (rQun < qun) {
            Toast.makeText(getContext(), "Not enougn stocks", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < helper.boughtStocks.size(); i++) {
            if (helper.boughtStocks.get(i).getId().equals(id)) {

                amountText.setText(": " + company.getRate());
                companyText.setText(company.getTitle());
                amountAfterText.setText(": " + helper.amount);
                amountBeforeText.setText(": " + (helper.amount + company.rate));
                showCard(false);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                        try {
                            int newQun = Integer.parseInt(quantityEditText.getText().toString());
                            Log.i("Infoo_db", "Qun  : " + newQun);
                            helper.sell(companyID, ((newQun * company.getRate())) + helper.amount, newQun);
                            companyID = "";
                            hideCard();
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                    }
                });
                return;
            }
        }
        Toast.makeText(getContext(), "You have no stocks for " + company.getTitle(), Toast.LENGTH_SHORT).show();
    }

    void showCard(boolean buy) {
        cardView.setVisibility(View.VISIBLE);
        if (buy) {
            buySellText.setText("Buy");
        } else {
            buySellText.setText("Sell");
        }
        recyclerView.setEnabled(false);

    }

    public void hideCard() {
        cardView.setVisibility(View.GONE);
        recyclerView.setEnabled(true);
    }

    void init() {
        adapter = new BuyAdapter(getActivity());

        adapter.setOnBuyClick(new BuyAdapter.OnBuyClick() {
            @Override
            public void onClick(String id) {
                buy(id);
            }
        });
        adapter.setOnSellClick(new BuyAdapter.OnSellClick() {
            @Override
            public void onClick(String id) {
                try {
                    sell(id);
                } catch (Exception e) {
                }
            }
        });

        recyclerView.setAdapter(adapter);
        cardView.setVisibility(View.GONE);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
