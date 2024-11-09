package com.example.smd_scheduler_app;

public class Product {
    private int id;
    private String title;
    private String date;
    private double price;
    private String status;

    // Constructors
    public Product() {}

    public Product(int id, String title, String date, double price, String status) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.price = price;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
