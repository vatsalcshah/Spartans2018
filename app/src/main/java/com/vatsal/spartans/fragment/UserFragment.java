package com.vatsal.spartans.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vatsal.spartans.FirebaseHelper;
import com.vatsal.spartans.LoginActivity;
import com.vatsal.spartans.R;
import com.vatsal.spartans.SignupActivity;
import com.vatsal.spartans.adapter.BoughtStockAdapter;
import com.vatsal.spartans.models.Company;
import com.vatsal.spartans.models.Stock;

import java.util.Objects;


public class UserFragment extends Fragment {

    private FirebaseAuth auth;
    RecyclerView recyclerView;
    TextView cashTextView, investedTextView, amountTextView, emailTextView;
    Button signoutButton;
    FirebaseHelper helper;

    public UserFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_user, container, false);
        helper = FirebaseHelper.getInstance();
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return null;
        }
        init(myFragmentView);

        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                h.postDelayed(this, 1000);
                int cash = helper.amount;
                int stock = 0;
                for (Stock s : helper.boughtStocks) {
                    stock += helper.getCompany(s.getId()).getRate();
                }
                int total = cash + stock;

                cashTextView.setText("Cash: " + cash);
                investedTextView.setText("Invested: " + stock);
                amountTextView.setText("Total : " + total);

            }
        }, 0);

        return myFragmentView;
    }


    private void init(View view) {
        amountTextView = view.findViewById(R.id.user_amountText);
        cashTextView = view.findViewById(R.id.user_cashText);
        investedTextView = view.findViewById(R.id.user_investedText);
        emailTextView = view.findViewById(R.id.user_mailText);
        signoutButton = view.findViewById(R.id.user_signoutButton);
        recyclerView = view.findViewById(R.id.user_recyclerview);
        if (auth.getCurrentUser() != null)
            emailTextView.setText(String.valueOf(auth.getCurrentUser().getEmail()));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new BoughtStockAdapter(getContext()));
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                getActivity().finish();
            }
        });
    }

    public void signOut() {
        auth.signOut();
    }
}