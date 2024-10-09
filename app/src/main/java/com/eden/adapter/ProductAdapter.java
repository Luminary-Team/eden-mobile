package com.eden.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.BuyProduct;
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
//        holder.name.setText(listaProducts.get(position).getName());
////        holder.descricao.setText(listaProdutos.get(position).getDescricao());
//        holder.value.setText("R$ " + (listaProducts.get(position).getValue()));
//
//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(v.getContext(), BuyProduct.class);
//            intent.putExtra("name", listaProducts.get(position).getName());
//            intent.putExtra("value", listaProducts.get(position).getValue());
//            intent.putExtra("description", listaProducts.get(position).getDescription());
//
//            v.getContext().startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return listaProducts.size();
    }

    public class ViewHolderProduto extends RecyclerView.ViewHolder {
        private long id;
        private long conditionTypeId;
        private long usersId;
        private TextView name;
        private TextView value;
        private String description;
        private String urlImage;
        private float avaliation;
        private int stock;

        public ViewHolderProduto(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.titulo_produto);
//            descricao = itemView.findViewById(R.id.descricao_produto);
            value = itemView.findViewById(R.id.valor_produto);
        }
    }
}
