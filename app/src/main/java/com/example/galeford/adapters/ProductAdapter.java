package com.example.galeford.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galeford.ProductDisplay;
import com.example.galeford.R;
import com.example.galeford.models.Products;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ProductDisplay productDisplay;
    private List<Products> productsList;

    public ProductAdapter (ProductDisplay pd, List<Products> p) {
        this.productDisplay = pd;
        this.productsList = p;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(productDisplay).inflate(R.layout.product_items_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // nnnn
        holder.productItemName.setText(productsList.get(position).getProductItemName());

        Picasso.get().load(productsList.get(position).getProductImage()).into(holder.productItemImage);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productItemImage;
        TextView productItemName;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productItemImage = itemView.findViewById(R.id.productItemImage);
            productItemName = itemView.findViewById(R.id.productItemName);
        }
    }
}
