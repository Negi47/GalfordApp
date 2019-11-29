package com.example.galeford;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.galeford.adapters.ProductAdapter;
import com.example.galeford.models.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductDisplay extends AppCompatActivity {

    FirebaseFirestore firestore;

    RecyclerView productRecyclerView;
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        firestore = FirebaseFirestore.getInstance();

        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2,LinearLayoutManager.VERTICAL,false);
        productRecyclerView.setLayoutManager(gridLayoutManager);


        firestore.collection("products").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        List<Products> productsList = task.getResult().toObjects(Products.class);

                        productAdapter = new ProductAdapter(ProductDisplay.this, productsList);
                        productRecyclerView.setAdapter(productAdapter);

                    }
                });





    }
}
