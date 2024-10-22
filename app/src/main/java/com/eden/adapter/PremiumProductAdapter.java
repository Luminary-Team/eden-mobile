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

public class PremiumProductAdapter extends RecyclerView.Adapter<PremiumProductAdapter.ViewHolderPremiumProduct> {

    private final List<Product> productList;


    public PremiumProductAdapter(List<Product> arg) {
        Collections.shuffle(arg);
        this.productList = arg;
    }

    @NonNull
    @Override
    public PremiumProductAdapter.ViewHolderPremiumProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
        return new PremiumProductAdapter.ViewHolderPremiumProduct(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PremiumProductAdapter.ViewHolderPremiumProduct holder, int position) {
        if (productList != null) {
            Log.d("ProductAdapter", "Position: " + position);
            Product product = productList.get(position);
            if (product.getTitle() != null) {
                holder.title.setText(product.getTitle());
            }
            if (product.getPrice() != 0) {
                holder.price.setText(String.format("R$ %.2f", product.getPrice()));
            }

            downloadImageFromFirebase(holder.itemView.getContext(), holder.imageView, "product_" + product.getId() + ".jpg");

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), BuyProduct.class);
                intent.putExtra("id", product.getId());
                intent.putExtra("nome", product.getTitle());
                intent.putExtra("valor", product.getPrice());
                intent.putExtra("descricao", product.getDescription());
                v.getContext().startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolderPremiumProduct extends RecyclerView.ViewHolder {
            private TextView usageTimeId, usageTime, conditionTypeId, user,
                    title, description, price, maxPrice, senderZipCode;
            private ImageView imageView;
            private RatingBar ratingBar;


            public ViewHolderPremiumProduct(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.product_title);
                price = itemView.findViewById(R.id.product_price);
                imageView = itemView.findViewById(R.id.product_image);
            }
        }
}