package com.example.smd_scheduler_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {
    private Context context;
    private DatabaseHelper dbHelper;

    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.context = fragmentActivity;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new NewProductsFragment();
            case 1:
                return new ScheduledProductsFragment(context);
            case 2:
                return new DeliveredProductsFragment(context);
            default:
                return new NewProductsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public View getTabView(int position) {
        View tabView = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tabTitle = tabView.findViewById(R.id.tab_title);
        TextView tabBadge = tabView.findViewById(R.id.tab_badge);

        switch (position) {
            case 0:
                tabTitle.setText("New Products");
                tabBadge.setText(String.valueOf(dbHelper.getProductsCount("new")));
                break;
            case 1:
                tabTitle.setText("Scheduled Products");
                tabBadge.setText(String.valueOf(dbHelper.getProductsCount("scheduled")));
                break;
            case 2:
                tabTitle.setText("Delivered Products");
                tabBadge.setText(String.valueOf(dbHelper.getProductsCount("delivered")));
                break;
        }

        return tabView;
    }

    public void updateFragments() {
        notifyDataSetChanged();
    }
}
