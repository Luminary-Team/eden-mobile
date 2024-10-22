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

import com.eden.ui.activities.BuyProduct;
import com.eden.model.Product;
import com.eden.R;
import com.eden.adapter.ProductPremiumAdapter;

import java.util.Collections;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_PREMIUM = 1;

    private final List<Product> listaProducts;

    public ProductAdapter(List<Product> arg) {
        Collections.shuffle(arg);
        this.listaProducts = arg;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 10 == 0) {
            return TYPE_PREMIUM;
        }
        return TYPE_NORMAL; 
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
            return new ViewHolderProduct(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_premium_product, parent, false);
            return new ProductPremiumAdapter.ViewHolderProductPremium(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Normal Product
        if (holder instanceof ViewHolderProduct) {
            ViewHolderProduct viewHolderProduct = (ViewHolderProduct) holder;
            if (listaProducts != null) {
                Log.d("ProductAdapter", "Position: " + position);
                Product product = listaProducts.get(position);
                if (product.getTitle() != null) {
                    viewHolderProduct.title.setText(product.getTitle());
                }
                if (product.getPrice() != 0) {
                    viewHolderProduct.price.setText(String.format("R$ %.2f", product.getPrice()));
                }

                downloadImageFromFirebase(viewHolderProduct.itemView.getContext(), viewHolderProduct.imageView, "product_" + product.getId() + ".jpg");

                viewHolderProduct.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), BuyProduct.class);
                    intent.putExtra("id", product.getId());
                    intent.putExtra("nome", product.getTitle());
                    intent.putExtra("valor", product.getPrice());
                    intent.putExtra("descricao", product.getDescription());
                    v.getContext().startActivity(intent);
                });
            }
        // Premium Product
        } else {
            ProductPremiumAdapter.ViewHolderProductPremium viewHolderProductPremium = (ProductPremiumAdapter.ViewHolderProductPremium) holder;
            if (listaProducts != null) {
                Product product = listaProducts.get(position);

//                viewHolderProductPremium.title.setText(product.getTitle());
//                viewHolderProductPremium.price.setText(String.format("R$ %.2f", product.getPrice()));

//                downloadImageFromFirebase(viewHolderProductPremium.itemView.getContext(), viewHolderProductPremium., "product_" + product.getId() + ".jpg");

                viewHolderProductPremium.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), BuyProduct.class);
                    intent.putExtra("id", product.getId());
                    intent.putExtra("nome", product.getTitle());
                    intent.putExtra("valor", product.getPrice());
                    intent.putExtra("descricao", product.getDescription());
                    v.getContext().startActivity(intent);
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaProducts.size();
    }

    public static class ViewHolderProduct extends RecyclerView.ViewHolder {
        private TextView usageTimeId, usageTime, conditionTypeId, user,
                title, description, price, maxPrice, senderZipCode;
        private ImageView imageView;
        private RatingBar ratingBar;


        public ViewHolderProduct(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.product_title);
            price = itemView.findViewById(R.id.product_price);
            imageView = itemView.findViewById(R.id.product_image);
        }
    }
}