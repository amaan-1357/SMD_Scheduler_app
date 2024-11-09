package com.example.smd_scheduler_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PRODUCTS = "products";

    // Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_STATUS + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    // Add product to database
    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, product.getTitle());
        values.put(COLUMN_DATE, product.getDate());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_STATUS, product.getStatus());

        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    // Update product in database
    public void updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, product.getTitle());
        values.put(COLUMN_DATE, product.getDate());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_STATUS, product.getStatus());

        db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())});
        db.close();
    }

    // Delete product from database
    public void deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    // Method to update the status of a product
    public void updateProductStatus(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, product.getStatus());  // Update status

        // Update the product in the database based on the ID
        db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())});
        db.close();
    }


    // Get count of products based on status
    public int getProductsCount(String status) {
        String countQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, new String[]{status});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // Get all products based on status
    public List<Product> getProductsByStatus(String status) {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{status});

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                product.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                product.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)));
                product.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));

                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return productList;
    }
}
