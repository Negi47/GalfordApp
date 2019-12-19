package com.example.galeford.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galeford.R;
import com.example.galeford.SingleProduct;
import com.example.galeford.WishlistActivity;
import com.example.galeford.models.Products;
import com.example.galeford.models.Users;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ProductViewHolder> {

    private WishlistActivity wishlistActivity;
    private List<Products> wishlist;
    private Users loggedInUser;


    public WishlistAdapter(WishlistActivity wishlistActivity, List<Products> wishlist, Users loggedInUser) {
        this.wishlistActivity = wishlistActivity;
        this.wishlist = wishlist;
        this.loggedInUser = loggedInUser;
    }

    @NonNull
    @Override
    public WishlistAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(wishlistActivity).inflate(R.layout.product_items_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ProductViewHolder holder, int position) {

        holder.productItemName.setText(wishlist.get(position).getProductItemName());
        holder.productItemPrice.setText("₹" +wishlist.get(position).getProductItemPrice());
        holder.productItemDetails.setText(wishlist.get(position).getProductItemDescription());

        Picasso.get().load(wishlist.get(position).getProductImage().get(0)).into(holder.productItemImage);


        if (loggedInUser.getFavProducts().contains(wishlist.get(position)._getProductId())) {
            holder.productItemLikeBtn.setVisibility(View.VISIBLE);
            holder.productItemUnlikeBtn.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return wishlist.size();
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
