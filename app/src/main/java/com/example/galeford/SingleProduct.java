package com.example.galeford;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.galeford.models.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;

public class SingleProduct extends AppCompatActivity {

    CarouselView productCarouselView;
    TextView productName, productPrice, productDetails, favProductCountTV;
    FirebaseFirestore firestore;
    Products productDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        productCarouselView = findViewById(R.id.productCarouselView);

        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDetails = findViewById(R.id.productDetails);
        favProductCountTV = findViewById(R.id.favProductCountTV);

        firestore = FirebaseFirestore.getInstance();


        firestore.collection(TableNames.PRODUCTS)
                .document(getIntent().getStringExtra(Products.PRODUCT_ID))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        productDisplay = task.getResult().toObject(Products.class);

                        int data = productDisplay.getProductImage().size();

                        Log.d("DataFireStore: ", String.valueOf(data));

                        carousalDisplayImage();
                        productName.setText(task.getResult().getString(Products.PRODUCT_ITEM_NAME));
                        productPrice.setText("₹"+task.getResult().getString(Products.PRODUCT_ITEM_PRICE));
                        productDetails.setText(task.getResult().getString(Products.PRODUCT_ITEM_DESCRIPTION));

                        favProductCountTV.setText(task.getResult().get(Products.USER_LIKED_COUNT).toString() + " users liked this product");
                    }
                });

    }

    public void carousalDisplayImage(){

        productCarouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Picasso.get().load(productDisplay.getProductImage().get(position)).into(imageView);
            }
        });

        productCarouselView.setPageCount(productDisplay.getProductImage().size());


    }
}
