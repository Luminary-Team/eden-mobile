package com.eden.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.BuyProduct;
import com.eden.api.dto.ConditionType;
import com.eden.api.dto.UsageTime;
import com.eden.api.dto.UserSchema;
import com.eden.model.Product;
import com.eden.R;

import org.w3c.dom.Text;

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

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), BuyProduct.class);
                if (product.getTitle() != null) {
                    intent.putExtra("name", product.getTitle());
                }
                if (product.getPrice() != 0) {
                    intent.putExtra("value", product.getPrice());
                }
                if (product.getDescription() != null) {
                    intent.putExtra("description", product.getDescription());
                }

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
                title, description, price, maxPrice, senderZipCode, rating;


        public ViewHolderProduto(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titulo_produto);
            price = itemView.findViewById(R.id.valor_produto);
        }
    }
}
