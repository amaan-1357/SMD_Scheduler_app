package com.example.smd_scheduler_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onEditClick(int position);  // Edit button clicked
        void onDeleteClick(int position);  // Delete button clicked
        void onProductClick(int position);  // Item clicked to change status
    }

    public ProductAdapter(Context context, List<Product> productList, OnProductClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.new_product_item, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product, position);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        ImageView editButton, deleteButton;

        public ProductViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.product_title);
            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }

        public void bind(Product product, final int position) {
            titleText.setText(product.getTitle());

            // Edit button click listener
            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(position);  // Call onEditClick method
                }
            });

            // Delete button click listener
            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(position);  // Call onDeleteClick method
                }
            });

            // Product item click listener for changing the status
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClick(position);  // Call onProductClick method
                }
            });
        }
    }
}
