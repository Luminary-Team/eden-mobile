package com.eden.adapter;

import static com.eden.utils.AndroidUtil.downloadImageFromFirebase;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.api.dto.CartItemResponse;
import com.eden.model.Product;
import com.eden.ui.activities.BuyProduct;

import java.util.Collections;
import java.util.List;

public class ProductAdapterNoMenu extends RecyclerView.Adapter<ProductAdapterNoMenu.ViewHolderItem> {

    private final List<Product> productList;

    public ProductAdapterNoMenu(List<Product> args) {
        Collections.reverse(args);
        this.productList = args;
    }

    @NonNull
    @Override
    public ProductAdapterNoMenu.ViewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_view_layout, parent, false);
        return new ProductAdapterNoMenu.ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapterNoMenu.ViewHolderItem holder, int position) {
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

    public class ViewHolderItem extends RecyclerView.ViewHolder {
        private TextView title, description, price, maxPrice;
        private ImageView imageView;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            itemView.findViewById(R.id.overflow_menu).setVisibility(View.GONE);
            imageView = itemView.findViewById(R.id.cart_product_image);
            title = itemView.findViewById(R.id.cart_product_title);
            description = itemView.findViewById(R.id.cart_product_description);
            price = itemView.findViewById(R.id.cart_product_price);
            maxPrice = itemView.findViewById(R.id.cart_max_price);
        }
    }
}
