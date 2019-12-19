package com.example.galeford.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galeford.LoginActivity;
import com.example.galeford.ProductDisplay;
import com.example.galeford.R;
import com.example.galeford.SingleProduct;
import com.example.galeford.TableNames;
import com.example.galeford.models.Products;
import com.example.galeford.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {

    private ProductDisplay productDisplay;
    private List<Products> productsList;
    private List<Products> allproductList;
    private Users loggedInuser;

    FirebaseFirestore firestore;

    public ProductAdapter (ProductDisplay pd, List<Products> p, Users loggedInuser) {
        this.productDisplay = pd;
        this.productsList = p;
        this.allproductList = new ArrayList<>(p);
        this.firestore = FirebaseFirestore.getInstance();
        this.loggedInuser = loggedInuser;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(productDisplay).inflate(R.layout.product_items_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {

        if (loggedInuser.getFavProducts().contains(productsList.get(position)._getProductId())) {
            holder.productItemLikeBtn.setVisibility(View.VISIBLE);
            holder.productItemUnlikeBtn.setVisibility(View.GONE);
        }

        // nnnn
        holder.productItemName.setText(productsList.get(position).getProductItemName());
        holder.productItemPrice.setText("₹" +productsList.get(position).getProductItemPrice());
        holder.productItemDetails.setText(productsList.get(position).getProductItemDescription());

        Picasso.get().load(productsList.get(position).getProductImage().get(0)).into(holder.productItemImage);

        holder.productLInearTagId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productDisplay.startActivity(new Intent(productDisplay, SingleProduct.class).putExtra(Products.PRODUCT_ID, productsList.get(position)._getProductId()));

            }
        });

        holder.productItemUnlikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser() == null){
                    productDisplay.startActivity(new Intent(productDisplay, LoginActivity.class));
                }
                else {
                    WriteBatch batch = firestore.batch();
                    DocumentReference userDoc = firestore.collection(TableNames.USERS).document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    DocumentReference prodDoc = firestore.collection(TableNames.PRODUCTS).document(productsList.get(position)._getProductId());

                    batch.update(userDoc, Users.FAV_PRODUCTS, FieldValue.arrayUnion(productsList.get(position)._getProductId()));
                    batch.update(prodDoc, Products.USER_LIKED_COUNT, FieldValue.increment(1));

                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            holder.productItemLikeBtn.setVisibility(View.VISIBLE);
                            holder.productItemUnlikeBtn.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        holder.productItemLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    productDisplay.startActivity(new Intent(productDisplay, LoginActivity.class));
                } else {
                    WriteBatch batch = firestore.batch();

                    DocumentReference userDoc = firestore.collection(TableNames.USERS).document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    DocumentReference prodDoc = firestore.collection(TableNames.PRODUCTS).document(productsList.get(position)._getProductId());

                    batch.update(userDoc, Users.FAV_PRODUCTS, FieldValue.arrayRemove(productsList.get(position)._getProductId()));
                    batch.update(prodDoc, Products.USER_LIKED_COUNT, FieldValue.increment(-1));

                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            holder.productItemLikeBtn.setVisibility(View.GONE);
                            holder.productItemUnlikeBtn.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Products> filteredProducts = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    filteredProducts.addAll(allproductList);
                }
                else {
                    String filteredPattern = charSequence.toString().toLowerCase().trim();
                    for (Products u : allproductList) {
                        if (u.getProductItemName().toLowerCase().contains(filteredPattern) || u.getProductItemDescription().toLowerCase().contains(filteredPattern)) {
                            filteredProducts.add(u);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredProducts;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productsList.clear();

                productsList.addAll((List) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productItemImage;
        TextView productItemName, productItemPrice, productItemDetails;
        ImageButton productItemUnlikeBtn,productItemLikeBtn;
        LinearLayout productLInearTagId;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productItemImage = itemView.findViewById(R.id.productItemImage);
            productItemName = itemView.findViewById(R.id.productItemName);
            productItemPrice = itemView.findViewById(R.id.productItemPrice);
            productItemDetails = itemView.findViewById(R.id.productItemDetails);
            productItemLikeBtn = itemView.findViewById(R.id.productItemLikeBtn);
            productItemUnlikeBtn = itemView.findViewById(R.id.productItemUnlikeBtn);
            productLInearTagId = itemView.findViewById(R.id.productLInearTagId);
        }
    }
}
