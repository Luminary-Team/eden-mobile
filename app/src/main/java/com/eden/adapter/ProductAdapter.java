package com.eden.adapter;

import static com.eden.utils.AndroidUtil.downloadImageFromFirebase;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eden.BuyProduct;
import com.eden.api.dto.ConditionType;
import com.eden.api.dto.UsageTime;
import com.eden.api.dto.UserSchema;
import com.eden.model.Product;
import com.eden.R;
import com.eden.utils.AndroidUtil;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolderProduto> {
    private List<Product> listaProducts;

    public ProductAdapter(List<Product> arg) {
        this.listaProducts = arg;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolderProduto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.produto_layout, parent, false);
        return new ViewHolderProduto(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolderProduto holder, int position) {
        if (listaProducts != null) {
            Log.d("ProductAdapter", "Position: " + position);
            Product product = listaProducts.get(position);
            if (product.getTitle() != null) {
                holder.title.setText(product.getTitle());
            }
            if (product.getPrice() != 0) {
                holder.price.setText("R$ " + product.getPrice());
            }
            if (product.getRating() >= 0 && product.getRating() <= 5) {
                holder.ratingBar.setRating(product.getRating());
            }

            downloadImageFromFirebase(holder.itemView.getContext(), holder.imageView, "product_" + product.getId() + ".jpg");

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
        return listaProducts.size();
    }

    public class ViewHolderProduto extends RecyclerView.ViewHolder {
        private TextView usageTimeId, usageTime, conditionTypeId, user,
                title, description, price, maxPrice, senderZipCode;
        private ImageView imageView;
        private RatingBar ratingBar;


        public ViewHolderProduto(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titulo_produto);
            price = itemView.findViewById(R.id.valor_produto);
            ratingBar = itemView.findViewById(R.id.rating_produto);
            imageView = itemView.findViewById(R.id.imagem_produto);
        }
    }
}
