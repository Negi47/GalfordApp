package com.example.galeford;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.galeford.adapters.ProductAdapter;
import com.example.galeford.adapters.WishlistAdapter;
import com.example.galeford.models.Products;
import com.example.galeford.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    RecyclerView wishlistRecyclerView;
    WishlistAdapter wishlistAdapter;
    LinearLayout wishlistProgressBar;

    List<Products> wishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        firestore = FirebaseFirestore.getInstance();
        wishList = new ArrayList<>();

        wishlistRecyclerView = findViewById(R.id.wishlistRecyclerView);
        wishlistRecyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2, LinearLayoutManager.VERTICAL,false);
        wishlistRecyclerView.setLayoutManager(gridLayoutManager);

        wishlistProgressBar = findViewById(R.id.wishlistLinearProgressBar);

        firestore.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            final HashMap<String, Products> allProducts = new HashMap<>();

                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Products products = new Products();
                                products._setProductId(doc.getId());
                                products.setProductItemName(doc.getString(Products.PRODUCT_ITEM_NAME));
                                products.setProductImage((List<String>)doc.get(Products.PRODUCT_IMAGE));
                                products.setProductItemPrice(doc.getString(Products.PRODUCT_ITEM_PRICE));
                                products.setProductItemDescription(doc.getString(Products.PRODUCT_ITEM_DESCRIPTION));

                                allProducts.put(doc.getId(), products);
                            }

                            firestore.collection(TableNames.USERS)
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.getResult() != null) {
                                                Users user = task.getResult().toObject(Users.class);
                                                for (String id : allProducts.keySet()) {
                                                    if (user.getFavProducts().contains(id)) {
                                                        wishList.add(allProducts.get(id));
                                                    }
                                                }

                                                wishlistAdapter = new WishlistAdapter(WishlistActivity.this, wishList, user);
                                                wishlistRecyclerView.setAdapter(wishlistAdapter);

                                                wishlistProgressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });

                        }
                    }
                });
    }
}
