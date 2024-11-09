package com.example.smd_scheduler_app;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.List;

public class NewProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_products, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        dbHelper = new DatabaseHelper(getContext());

        // Initialize product list
        productList = dbHelper.getProductsByStatus("new");  // Only new products
        productAdapter = new ProductAdapter(getContext(), productList, new ProductAdapter.OnProductClickListener() {

            @Override
            public void onEditClick(int position) {
                Product product = productList.get(position);
                showAddEditProductDialog(product);  // Show the edit dialog for this product
            }

            @Override
            public void onDeleteClick(int position) {
                Product product = productList.get(position);
                showDeleteProductDialog(product);  // Show delete confirmation dialog
            }

            @Override
            public void onProductClick(int position) {
                Product product = productList.get(position);
                showChangeStatusDialog(product);  // Show status change dialog
            }
        });

        recyclerView.setAdapter(productAdapter);
        return rootView;
    }

    // Method to show the Edit/Add Product dialog
    private void showAddEditProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_product, null);
        builder.setView(dialogView);

        EditText titleInput = dialogView.findViewById(R.id.edit_text_title);
        EditText dateInput = dialogView.findViewById(R.id.edit_text_date);
        EditText priceInput = dialogView.findViewById(R.id.edit_text_price);

        if (product != null) {
            titleInput.setText(product.getTitle());
            dateInput.setText(product.getDate());
            priceInput.setText(String.valueOf(product.getPrice()));
        }

        builder.setPositiveButton("Save", (dialog, which) -> {
            String titleText = titleInput.getText().toString();
            String dateText = dateInput.getText().toString();
            double price = Double.parseDouble(priceInput.getText().toString());

            if (product == null) {
                Product newProduct = new Product();
                newProduct.setTitle(titleText);
                newProduct.setDate(dateText);
                newProduct.setPrice(price);
                newProduct.setStatus("new");

                dbHelper.addProduct(newProduct);
                productList.add(newProduct);  // Add to the list
                productAdapter.notifyItemInserted(productList.size() - 1);  // Notify adapter
            } else {
                product.setTitle(titleText);
                product.setDate(dateText);
                product.setPrice(price);
                dbHelper.updateProduct(product);
                productAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Method to show the Delete Product confirmation dialog
    private void showDeleteProductDialog(Product product) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dbHelper.deleteProduct(product.getId());
                    productList.remove(product);
                    productAdapter.notifyDataSetChanged();  // Refresh the list
                })
                .setNegativeButton("No", null)
                .show();
    }

    // Method to show the Change Status dialog
    private void showChangeStatusDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Status");

        final CheckBox checkBox = new CheckBox(getActivity());
        checkBox.setText("Mark as Scheduled");
        builder.setView(checkBox);

        builder.setPositiveButton("OK", (dialog, which) -> {
            if (checkBox.isChecked()) {
                product.setStatus("scheduled");
                dbHelper.updateProductStatus(product);
                productList.remove(product);
                productAdapter.notifyDataSetChanged();  // Refresh the list
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}