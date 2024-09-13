package com.eden;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eden.adapter.ProdutosAdapter;
import com.eden.model.Produto;
import com.eden.utils.FirebaseProdutoUtil;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FirebaseProdutoUtil db = new FirebaseProdutoUtil();

        List<Produto> produtos = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_produtos);

        // Settando o adapter da RecyclerView
        ProdutosAdapter adapter = new ProdutosAdapter(produtos);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 2));

        // TEMP
        db.listarProdutos(produtos, adapter);

        return view;
    }
}