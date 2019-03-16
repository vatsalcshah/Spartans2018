package com.vatsal.spartans;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vatsal.spartans.adapter.BoughtStockAdapter;
import com.vatsal.spartans.adapter.BuyAdapter;
import com.vatsal.spartans.adapter.StockAdapter;
import com.vatsal.spartans.models.Company;
import com.vatsal.spartans.models.Stock;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseHelper {

    ArrayList<Company> companyList;
    public int amount;
    int stockValue = 0;

    private static FirebaseHelper firebaseHelper;
    private StockAdapter stockAdapter;
    private BuyAdapter buyAdapter;
    public BoughtStockAdapter boughtStockAdapter;

    public void setBuyAdapter(BuyAdapter buyAdapter) {
        this.buyAdapter = buyAdapter;
    }

    public ArrayList<Stock> boughtStocks;

    String getUID() {
        return FirebaseAuth.getInstance().getUid();
    }

    private FirebaseHelper() {
        boughtStocks = new ArrayList<>();
        companyList = new ArrayList<>();
        loadNews();
        loadCompanyData();
        try {
            loadAmount();
        } catch (Exception e) {
            e.printStackTrace();
        }

        stockListener();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", "1");
//        FirebaseFunctions functions = FirebaseFunctions.getInstance();
//        functions.getHttpsCallable("buy").call(hashMap).continueWith(new Continuation<HttpsCallableResult, Object>() {
//            @Override
//            public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
//                String result = (String) task.getResult().getData();
//                return result;
//            }
//        });
    }

    private void loadAmount() throws Exception {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i("Infoo", uid);
        FirebaseDatabase.getInstance().getReference("users")
                .child(uid)
                .child("amount")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String s = dataSnapshot.getValue().toString();
                        try {
                            amount = Integer.parseInt(s);
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    public static FirebaseHelper getInstance() {
        synchronized (FirebaseHelper.class) {
            if (firebaseHelper == null) {
                firebaseHelper = new FirebaseHelper();
            }
            return firebaseHelper;
        }
    }


    public void stockListener() {
        FirebaseDatabase.getInstance().getReference("users/" + getUID() + "/stocks").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String id = (String) dataSnapshot.child("id").getValue();
                String key = dataSnapshot.getKey();
                Stock stock = new Stock(id);
                stock.setKey(key);
                boughtStocks.add(stock);
                if (buyAdapter != null)
                    buyAdapter.notifyDataSetChanged();
                Log.i("Infooo_db", boughtStocks.size() + " Added " + dataSnapshot.toString());
                if (boughtStockAdapter != null)
                    boughtStockAdapter.update();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                for (int i = 0; i < boughtStocks.size(); i++) {
                    if (boughtStocks.get(i).getKey().equals(key)) {
                        Log.i("Infooo_db", boughtStocks.size() + " Deleted " + dataSnapshot.toString());
                        boughtStocks.remove(i);
                        if (buyAdapter != null)
                            buyAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                if (boughtStockAdapter != null)
                    boughtStockAdapter.update();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void buy(final String id) {

        FirebaseDatabase.getInstance().getReference("enable").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((boolean) dataSnapshot.getValue()) {
                    HashMap<String, String> uploadData = new HashMap<>();
                    uploadData.put("id", id);
                    int newAmount = amount - getCompany(id).rate;
                    if (newAmount < 0) {
                        return;
                    }
                    FirebaseDatabase.getInstance().getReference("users/" + getUID() + "/amount").setValue(newAmount);
                    // FirebaseDatabase.getInstance().getReference("users/" + getUID() + "/stocks")
                    // .push().setValue(uploadData);

                    DatabaseReference buyDB = FirebaseDatabase
                            .getInstance()
                            .getReference("users/" + getUID() + "/stocks")
                            .push();
                    buyDB.setValue(uploadData);
                    if (buyAdapter != null)
                        buyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void buy(final String id, final int zA) {

        FirebaseDatabase.getInstance().getReference("enable").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((boolean) dataSnapshot.getValue()) {
                    HashMap<String, String> uploadData = new HashMap<>();
                    uploadData.put("id", id);
                    int newAmount = amount - getCompany(id).rate;
                    if (newAmount < 0) {
                        return;
                    }
                    FirebaseDatabase.getInstance().getReference("users/" + getUID() + "/amount").setValue(zA);
                    // FirebaseDatabase.getInstance().getReference("users/" + getUID() + "/stocks")
                    // .push().setValue(uploadData);

                    DatabaseReference buyDB = FirebaseDatabase
                            .getInstance()
                            .getReference("users/" + getUID() + "/stocks")
                            .push();
                    buyDB.setValue(uploadData);
                    if (buyAdapter != null)
                        buyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void sell(String id) {
        Log.i("Infoo_db", "Size " + boughtStocks.size());

        for (int i = boughtStocks.size() - 1; i >= 0; i--) {
            if (boughtStocks.get(i).getId().equals(id)) {
                Log.i("Infoo_db", boughtStocks.get(i).getId() + " " + boughtStocks.get(i).getKey());
                int newAmount = amount + getCompany(id).getRate();

                FirebaseDatabase.getInstance()
                        .getReference("users/" + getUID() + "/stocks/" + boughtStocks.get(i).getKey())
                        .removeValue();
                FirebaseDatabase.getInstance().getReference("users/" + getUID() + "/amount").setValue(newAmount);
                if (buyAdapter != null)
                    buyAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    public void sell(String id, int zAmount, int qun) {
        Log.i("Infoo_db", "Size " + boughtStocks.size());



        try {

            ArrayList<String> keysToRemoveList = new ArrayList<>();
            int lastIndex = 0;

            for (int i = lastIndex, x = 0; i < boughtStocks.size() && x < qun; i++) {
                if (boughtStocks.get(i).getId().equals(id)) {
                    x++;
                    keysToRemoveList.add(boughtStocks.get(i).getKey());
                    Log.i("Infoo_db",  qun + " " + boughtStocks.get(i).getKey());
                }
            }
            for (String key : keysToRemoveList) {
                FirebaseDatabase.getInstance()
                        .getReference("users/" + getUID() + "/stocks/" + key)
                        .removeValue();
            }
            if (buyAdapter != null)
                buyAdapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
        FirebaseDatabase.getInstance().getReference("users/" + getUID() + "/amount").setValue(zAmount);
    }


    public ArrayList<Company> getCompanyList() {
        return companyList;
    }


    public Company getCompany(String id) {
        for (Company c : companyList) {
            if (c.id.equals(id)) {
                return c;
            }
        }
        return null;
    }

    public void setStockAdapter(StockAdapter adapter) {
        this.stockAdapter = adapter;
    }

    private void loadCompanyData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Charts");
        databaseReference.keepSynced(true);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String id = dataSnapshot.getKey();
                Company company = dataSnapshot.getValue(Company.class);
                company.setId(id);
                companyList.add(company);

                if (stockAdapter != null) {
                    stockAdapter.update();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String id = dataSnapshot.getKey();
                Company company = dataSnapshot.getValue(Company.class);
                for (int i = 0; i < companyList.size(); i++) {
                    if (companyList.get(i).getId().equals(id)) {
                        company.setId(id);
                        companyList.set(i, company);
                        companyDataUpdated(id, i, companyList.get(i));
                        if (stockAdapter != null) {
                            stockAdapter.update();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String id = dataSnapshot.getKey();
                for (int i = 0; i < companyList.size(); i++) {
                    if (companyList.get(i).getId().equals(id)) {
                        companyList.remove(i);
                        companyDataUpdated(id, i, null);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void companyDataUpdated(String id, int pos, Company company) {
        if (onCompanyDataChangedListener != null) {
            onCompanyDataChangedListener.onDataChange(companyList, id);
        }
        if (stockAdapter != null) {
            stockAdapter.notifyDataSetChanged();
            stockAdapter.update(pos, company);
        }
        if (buyAdapter != null) {
            buyAdapter.notifyDataSetChanged();
            buyAdapter.update(pos, company);
        }
    }

    public interface onCompanyDataChanged {
        void onDataChange(ArrayList<Company> data, String... ids);
    }

    private onCompanyDataChanged onCompanyDataChangedListener;

    public void setOnCompnyDataChangeListener(onCompanyDataChanged listener) {
        onCompanyDataChangedListener = listener;
    }


    // -------------------------------------------------------------------------------
    public interface onNewsDataChanged {

    }

    public void loadNews() {

    }
    // -------------------------------------------------------------------------------


}
