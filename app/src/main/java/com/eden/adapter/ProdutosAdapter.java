package com.eden.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.BuyProduct;
import com.eden.model.Produto;
import com.eden.R;

import java.util.List;

public class ProdutosAdapter extends RecyclerView.Adapter<ProdutosAdapter.ViewHolderProduto> {
    private List<Produto> listaProdutos;

    public ProdutosAdapter(List<Produto> arg) {
        this.listaProdutos = arg;
    }

    @NonNull
    @Override
    public ProdutosAdapter.ViewHolderProduto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.produto_layout, parent, false);
        return new ViewHolderProduto(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutosAdapter.ViewHolderProduto holder, int position) {
        holder.nome.setText(listaProdutos.get(position).getNome());
//        holder.descricao.setText(listaProdutos.get(position).getDescricao());
        holder.valor.setText("R$ " + (listaProdutos.get(position).getValor()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), BuyProduct.class);
            intent.putExtra("nome", listaProdutos.get(position).getNome());
            intent.putExtra("valor", listaProdutos.get(position).getValor());
            intent.putExtra("imagem", listaProdutos.get(position).getImagem());
            intent.putExtra("descricao", listaProdutos.get(position).getDescricao());
            intent.putExtra("tipoEntrega", listaProdutos.get(position).getTipoEntrega());

            v.getContext().startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public class ViewHolderProduto extends RecyclerView.ViewHolder {
        int imagem;
        TextView nome, descricao, valor;
        Boolean curtido, adcCarrinho;

        public ViewHolderProduto(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.titulo_produto);
//            descricao = itemView.findViewById(R.id.descricao_produto);
            valor = itemView.findViewById(R.id.valor_produto);
        }
    }
}
