package com.eden.adapter;

import static com.eden.utils.AndroidUtil.downloadImageFromFirebase;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.model.Product;
import com.eden.ui.activities.BuyProduct;

import java.util.Collections;
import java.util.List;

public class ProductPremiumAdapter extends RecyclerView.Adapter<ProductPremiumAdapter.ViewHolderProductPremium>{

    private final List<Product> premiumProductsList;

    public ProductPremiumAdapter(List<Product> premiumProducts) {
        this.premiumProductsList = premiumProducts;
    }

    @NonNull
    @Override
    public ProductPremiumAdapter.ViewHolderProductPremium onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
        return new ProductPremiumAdapter.ViewHolderProductPremium(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductPremiumAdapter.ViewHolderProductPremium holder, int position) {
        Product product = premiumProductsList.get(position);
        holder.title.setText(product.getTitle());
        holder.price.setText(String.format("R$ %.2f", product.getPrice()));
        // Carregar imagem e configurar clique
    }

    @Override
    public int getItemCount() {
        return premiumProductsList.size();
    }

    public static class ViewHolderProductPremium extends RecyclerView.ViewHolder {
        private TextView usageTimeId, usageTime, conditionTypeId, user,
                title, description, price, maxPrice, senderZipCode;
        private ImageView imageView;
        private RatingBar ratingBar;
        private RecyclerView recyclerView;


        public ViewHolderProductPremium(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.product_title);
            price = itemView.findViewById(R.id.product_price);
            imageView = itemView.findViewById(R.id.product_image);
        }
    }

}
