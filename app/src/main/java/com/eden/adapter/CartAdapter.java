package com.eden.adapter;

import static com.eden.utils.AndroidUtil.downloadImageFromFirebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.api.dto.CartResponse;
import com.eden.model.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolderItem> {
    List<CartResponse> listaCartResponses;

    public CartAdapter(List<CartResponse> arg) {
        this.listaCartResponses = arg;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_view_layout, parent, false);
        return new CartAdapter.ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolderItem holder, int position) {
        if (!listaCartResponses.isEmpty()) {
            Product item = listaCartResponses.get(position).getProduct();
            holder.title.setText(item.getTitle() != null ? item.getTitle() : "");
            holder.price.setText(item.getPrice() != 0 ? String.format("R$ %.2f", item.getPrice()) : "");
            holder.description.setText(item.getDescription() != null ? item.getDescription() : "");
            holder.maxPrice.setText(item.getMaxPrice() != 0 ? String.format("R$ %.2f", item.getMaxPrice()) : "");

            downloadImageFromFirebase(holder.itemView.getContext(), holder.imageView, "product_" + item.getId() + ".jpg");
        }
    }

    @Override
    public int getItemCount() {
        return listaCartResponses.size();
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder {
        private TextView title, description, price, maxPrice;
        private ImageView imageView;


        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cart_product_image);
            title = itemView.findViewById(R.id.cart_product_title);
            description = itemView.findViewById(R.id.cart_product_description);
            price = itemView.findViewById(R.id.cart_product_price);
            maxPrice = itemView.findViewById(R.id.cart_max_price);
        }
    }
}
