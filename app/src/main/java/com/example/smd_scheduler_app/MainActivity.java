package com.example.smd_scheduler_app;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private DatabaseHelper dbHelper;
    private FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        fab = findViewById(R.id.fab_add_product);

        fragmentAdapter = new FragmentAdapter(this);
        viewPager.setAdapter(fragmentAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("New Products");
                            break;
                        case 1:
                            tab.setText("Scheduled Products");
                            break;
                        case 2:
                            tab.setText("Delivered Products");
                            break;
                    }
                }).attach();

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(fragmentAdapter.getTabView(i));
            }
        }

        updateTabBadges();

        fab.setOnClickListener(v -> showAddProductDialog());
    }

    private void showDatePickerDialog(final EditText dateInput) {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Format the date to a readable format (e.g., "dd/MM/yyyy")
                        String date = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year);
                        dateInput.setText(date);  // Set the selected date to the EditText
                    }
                },
                year, month, day);  // Initial date set to the current date

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        EditText titleInput = dialogView.findViewById(R.id.add_text_title);
        EditText dateInput = dialogView.findViewById(R.id.add_text_date);
        EditText priceInput = dialogView.findViewById(R.id.add_text_price);
        Button cancelButton = dialogView.findViewById(R.id.button_cancel);
        Button okButton = dialogView.findViewById(R.id.button_ok);

        // Set up DatePicker for Date Field
        dateInput.setOnClickListener(v -> showDatePickerDialog(dateInput));

        // Price Field validation (only allow positive numbers)
        priceInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String input = charSequence.toString();
                if (!input.matches("^(\\d+)(\\.\\d+)?$")) {
                    priceInput.setError("Enter a valid positive number");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // OK button click handler
        okButton.setOnClickListener(v -> {
            String titleText = titleInput.getText().toString();
            String dateText = dateInput.getText().toString();
            String priceText = priceInput.getText().toString();

            if (titleText.isEmpty() || dateText.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceText);

            // Add new product
            Product newProduct = new Product();
            newProduct.setTitle(titleText);
            newProduct.setDate(dateText);
            newProduct.setPrice(price);
            newProduct.setStatus("new");

            dbHelper.addProduct(newProduct);
        });

        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", null);  // In case the user presses the back button
        builder.show();
    }


    private void updateTabBadges() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null && tab.getCustomView() != null) {
                TextView tabBadge = tab.getCustomView().findViewById(R.id.tab_badge);
                switch (i) {
                    case 0:
                        tabBadge.setText(String.valueOf(dbHelper.getProductsCount("new")));
                        break;
                    case 1:
                        tabBadge.setText(String.valueOf(dbHelper.getProductsCount("scheduled")));
                        break;
                    case 2:
                        tabBadge.setText(String.valueOf(dbHelper.getProductsCount("delivered")));
                        break;
                }
            }
        }
    }
}
