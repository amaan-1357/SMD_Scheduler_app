package com.example.smd_scheduler_app;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class ScheduledProductsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private DatabaseHelper dbHelper;
    private Context context;
    public ScheduledProductsFragment(Context context){
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_products, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DatabaseHelper(getContext());
        productList = dbHelper.getProductsByStatus("scheduled");

        adapter = new ProductAdapter(context, productList, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onEditClick(int position) {
                // Show edit dialog
            }

            @Override
            public void onDeleteClick(int position) {
                // Show delete confirmation dialog
            }

            @Override
            public void onProductClick(int position) {
                // Show status update dialog
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }
}
