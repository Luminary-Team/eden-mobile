package com.eden.adapter;


import static com.eden.utils.AndroidUtil.downloadImageFromFirebase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.api.RetrofitClient;
import com.eden.api.services.ProductService;
import com.eden.ui.activities.BuyProduct;
import com.eden.model.Product;
import com.eden.R;
import com.eden.ui.activities.EditProduct;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductAdapter.ViewHolderUserProduto> {

    private final List<Product> userProducts;

    public UserProductAdapter(List<Product> args) {
        Collections.reverse(args);
        this.userProducts = args;
    }

    @NonNull
    @Override
    public UserProductAdapter.ViewHolderUserProduto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_view_layout, parent, false);
        return new ViewHolderUserProduto(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProductAdapter.ViewHolderUserProduto holder, @SuppressLint("RecyclerView") int position) {
        Log.d("Hellow", "Position: " + position);
        if (userProducts != null) {
            Log.d("ProductAdapter", "Position: " + position);
            Product product = userProducts.get(position);
            if (product.getTitle() != null) {
                holder.cartProductTitle.setText(product.getTitle());
            }
            if (product.getDescription() != null) {
                holder.cartProductDescription.setText(product.getDescription());
            }
            if (product.getPrice() != 0) {
                holder.cartProductPrice.setText(String.format("R$ %.2f", product.getPrice()));
            }

            downloadImageFromFirebase(holder.itemView.getContext(), holder.cartProductImage, "product_" + product.getId() + ".jpg");

            // Popup Menu for edit and delete
            holder.overflowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                    popupMenu.getMenuInflater().inflate(R.menu.product_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.edit_option) {
                                Intent intent = new Intent(view.getContext(), EditProduct.class);
                                intent.putExtra("id", product.getId());
                                intent.putExtra("nome", product.getTitle());
                                intent.putExtra("valor", product.getPrice());
                                intent.putExtra("descricao", product.getDescription());
                                intent.putExtra("premium", product.isPremium());
                                view.getContext().startActivity(intent);
                                return true;
                            } else if (item.getItemId() == R.id.delete_option) {
                                // Delete the product
                                ProductService productService = RetrofitClient.getClient().create(ProductService.class);
                                Call<Void> call = productService.deleteProduct(product.getId());
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(view.getContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                                            notifyItemRemoved(position);
                                            userProducts.remove(position);
                                            notifyItemRangeChanged(position, userProducts.size());
                                        } else {
                                            Toast.makeText(view.getContext(), "Product not deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable throwable) {
                                        Toast.makeText(view.getContext(), "Product deleted", Toast.LENGTH_SHORT).show();

                                    }
                                });

                                return true;
                            }
                            return false;
                        }
                    });

                    popupMenu.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userProducts.size();
    }

    public static class ViewHolderUserProduto extends RecyclerView.ViewHolder {
        public ShapeableImageView cartProductImage;
        public TextView cartProductTitle;
        public TextView cartProductDescription;
        public TextView cartProductPrice;
        public TextView cartMaxPrice;
        public ImageButton overflowMenu;

        public ViewHolderUserProduto(@NonNull View itemView) {
            super(itemView);
            cartProductImage = itemView.findViewById(R.id.cart_product_image);
            cartProductTitle = itemView.findViewById(R.id.cart_product_title);
            cartProductDescription = itemView.findViewById(R.id.cart_product_description);
            cartProductPrice = itemView.findViewById(R.id.cart_product_price);
            cartMaxPrice = itemView.findViewById(R.id.cart_max_price);
            overflowMenu = itemView.findViewById(R.id.overflow_menu);

        }
    }
}
