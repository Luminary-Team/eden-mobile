package com.eden.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eden.R;
import com.eden.model.Article;
import com.eden.ui.activities.ArticlesView;
import com.eden.ui.activities.RestrictedArea;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolderArticle> {

    private final List<Article> articlesList;

    public ArticlesAdapter(List<Article> args) {
        this.articlesList = args;
    }

    @NonNull
    @Override
    public ArticlesAdapter.ViewHolderArticle onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_layout, parent, false);
        return new ArticlesAdapter.ViewHolderArticle(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesAdapter.ViewHolderArticle holder, int position) {
        if (articlesList != null) {
            Article article = articlesList.get(position);
//            Glide.with(holder.article_photo.getContext()).load(article.getUrl()).into(holder.article_photo);
            holder.article_title.setText(article.getTitle());
            holder.article_content.setText(article.getDescription());

            loadArticleImage(holder, article.getUrl());

            Intent intent = new Intent(holder.itemView.getContext(), ArticlesView.class);
            intent.putExtra("url", article.getUrl());

            holder.itemView.setOnClickListener(v -> {
                holder.itemView.getContext().startActivity(intent);
            });

            holder.article_image.setOnClickListener(v -> {
                holder.itemView.getContext().startActivity(intent);
            });

        }
    }

    private void loadArticleImage(ViewHolderArticle holder, String url) {
//        holder.article_photo.setVisibility(View.GONE);

        // Configurar o WebView
        holder.article_image.setVisibility(View.VISIBLE);
        holder.article_image.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Captura a imagem do WebView
                view.setDrawingCacheEnabled(true);
                holder.article_image.setDrawingCacheEnabled(false);

                // Exibir a imagem capturada na ImageView
                holder.article_image.setVisibility(View.VISIBLE);
            }
        });

        // Desabilitar interação do WebView
        holder.article_image.setFocusable(false);
        holder.article_image.setClickable(false);
        holder.article_image.setOnTouchListener((v, event) -> true); // Ignorar toques

        // Carregar a URL no WebView
        holder.article_image.loadUrl(url);

    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }

    public static class ViewHolderArticle extends RecyclerView.ViewHolder {
        private final WebView article_image;
//        private final ImageView article_photo;
        private final TextView article_title;
        private final TextView article_content;

        public ViewHolderArticle(@NonNull View itemView) {
            super(itemView);
            this.article_image = itemView.findViewById(R.id.article_image);
//            this.article_photo = itemView.findViewById(R.id.article_photo);
            this.article_content = itemView.findViewById(R.id.article_title);
            this.article_title = itemView.findViewById(R.id.article_content);
        }
    }

}
