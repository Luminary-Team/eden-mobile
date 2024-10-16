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
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductAdapter.ViewHolderUserProduto> {
    private List<Product> userProducts;

    public UserProductAdapter(List<Product> userProducts) {
        this.userProducts = userProducts;
    }

    @NonNull
    @Override
    public UserProductAdapter.ViewHolderUserProduto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_view_layout, parent, false);

        return new ViewHolderUserProduto(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProductAdapter.ViewHolderUserProduto holder, int position) {
        if (userProducts != null) {
            Log.d("ProductAdapter", "Position: " + position);
            Product product = userProducts.get(position);
            if (product.getTitle() != null) {
                holder.cartProductTitle.setText(product.getTitle());
            }
            if (product.getPrice() != 0) {
                holder.cartProductPrice.setText(String.format("R$ %.2f", product.getPrice()));
            }

            downloadImageFromFirebase(holder.itemView.getContext(), holder.cartProductImage, "product_" + product.getId() + ".jpg");

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), BuyProduct.class);
                intent.putExtra("id", product.getId());
                intent.putExtra("nome", product.getTitle());
                intent.putExtra("valor", product.getPrice());
                intent.putExtra("descricao", product.getDescription());
                intent.putExtra("rating", product.getRating());
                v.getContext().startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return userProducts.size();
    }

    public class ViewHolderUserProduto extends RecyclerView.ViewHolder {
        public ShapeableImageView cartProductImage;
        public TextView cartProductTitle;
        public TextView cartProductDescription;
        public TextView cartProductPrice;
        public TextView cartMaxPrice;

        public ViewHolderUserProduto(@NonNull View itemView) {
            super(itemView);
            cartProductImage = itemView.findViewById(R.id.cart_product_image);
            cartProductTitle = itemView.findViewById(R.id.cart_product_title);
            cartProductDescription = itemView.findViewById(R.id.cart_product_description);
            cartProductPrice = itemView.findViewById(R.id.cart_product_price);
            cartMaxPrice = itemView.findViewById(R.id.cart_max_price);
        }
    }
}
