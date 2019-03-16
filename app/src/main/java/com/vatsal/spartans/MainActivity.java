package com.vatsal.spartans;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.vatsal.spartans.adapter.ViewPagerAdapter;
import com.vatsal.spartans.fragment.BuyFragment;
import com.vatsal.spartans.fragment.NewsFragment;
import com.vatsal.spartans.fragment.StockFragment;
import com.vatsal.spartans.fragment.UserFragment;

import java.lang.reflect.Field;


public class MainActivity extends AppCompatActivity {


    MenuItem prevMenuItem;
    FirebaseHelper firebaseHelper;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_stock:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_buy:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_user:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void init() {
        findViewById(R.id.message);
        final BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setPager(viewPager);
    }


    private void startFirebase() {
        firebaseHelper = FirebaseHelper.getInstance();
    }

    public BuyFragment buyFragment;

    private void setPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        buyFragment = new BuyFragment();
        adapter.addFragment(new NewsFragment());
        adapter.addFragment(new StockFragment());
        adapter.addFragment(buyFragment);
        adapter.addFragment(new UserFragment());
        viewPager.setAdapter(adapter);
    }

    public static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShifting(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }

    }

    @Override
    public void onBackPressed() {

        if (buyFragment.cardView.getVisibility() == View.VISIBLE) {
            buyFragment.hideCard();
        } else {
            super.onBackPressed();
        }

    }
}
