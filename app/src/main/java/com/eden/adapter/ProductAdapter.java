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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.ui.activities.BuyProduct;
import com.eden.model.Product;
import com.eden.R;

import java.util.Collections;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PREMIUM = 0;
    private static final int TYPE_NORMAL = 1;

    private final List<Product> normalProductList;
    private final List<Product> premiumProductList;

    public ProductAdapter(List<Product> normalProducts, List<Product> premiumProducts) {
        Collections.shuffle(normalProducts);
        this.normalProductList = normalProducts;
        this.premiumProductList = premiumProducts;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_PREMIUM; // Tipo para o carrossel
        }
        return TYPE_NORMAL; // Tipo para produtos normais
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PREMIUM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_premium_product, parent, false);
            return new ViewHolderPremium(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
            return new ViewHolderProduct(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderPremium) {
            // Configurar o carrossel
            RecyclerView recyclerView = holder.itemView.findViewById(R.id.recyclerView_premium_product);
            recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.post(() -> recyclerView.scrollToPosition(1));

            LinearSnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);

            recyclerView.setAdapter(new ProductPremiumAdapter(premiumProductList)); // Passar a lista de produtos premium

        } else if (holder instanceof ViewHolderProduct) {
            // Normal Product
            ViewHolderProduct viewHolderProduct = (ViewHolderProduct) holder;
            if (normalProductList != null) {
                Log.d("ProductAdapter", "Position: " + position);
                Product product = normalProductList.get(position - 1); // Ajustar a posição para ignorar o carrossel
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
        }
    }

    @Override
    public int getItemCount() {
        return normalProductList.size() + 1; // Adiciona 1 para o carrossel
    }

    public static class ViewHolderProduct extends RecyclerView.ViewHolder {
        private TextView title, price;
        private ImageView imageView;

        public ViewHolderProduct(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.product_title);
            price = itemView.findViewById(R.id.product_price);
            imageView = itemView.findViewById(R.id.product_image);
        }
    }

    public static class ViewHolderPremium extends RecyclerView.ViewHolder {
        public ViewHolderPremium(@NonNull View itemView) {
            super(itemView);
            // Inicialize outros componentes do layout do carrossel, se necessário
        }
    }
}