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

public class ProductPremiumAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolderPremium>{

    private final List<Product> premiumProductsList;

    public ProductPremiumAdapter(List<Product> premiumProducts) {
        this.premiumProductsList = premiumProducts;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolderPremium onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.premium_product_layout, parent, false);
        return new ProductAdapter.ViewHolderPremium(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolderPremium holder, int position) {

        if (premiumProductsList != null) {
            Product product = premiumProductsList.get(position);

            holder.premiumOverflowMenu.setVisibility(View.GONE);

            if (product.getTitle() != null) {
                holder.premiumTitle.setText(product.getTitle());
            }
            if (product.getPrice() != 0) {
                holder.premiumPrice.setText(String.format("R$ %.2f", product.getPrice()));
            }

            downloadImageFromFirebase(holder.itemView.getContext(), holder.premiumImageView, "product_" + product.getId() + ".jpg");

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
        return premiumProductsList.size();
    }

//    public static class ViewHolderProductPremium extends RecyclerView.ViewHolder {
//        private TextView usageTimeId, usageTime, conditionTypeId, user,
//                title, description, price, maxPrice, senderZipCode;
//        private ImageView imageView;
//        private RatingBar ratingBar;
//        private RecyclerView recyclerView;
//
//
//        public ViewHolderProductPremium(@NonNull View itemView) {
//            super(itemView);
//            title = itemView.findViewById(R.id.product_title);
//            price = itemView.findViewById(R.id.product_price);
//            imageView = itemView.findViewById(R.id.product_image);
//        }
//    }

}
