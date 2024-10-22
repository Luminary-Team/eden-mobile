package com.eden.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eden.R;
import com.eden.model.Article;

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
//            Glide.with(holder.article_image.getContext()).load(article.getUrl()).into(holder.article_image);
//            Glide.with(holder.article_photo.getContext()).load(article.getUrl()).into(holder.article_photo);
            holder.article_title.setText(article.getTitle());
            holder.article_content.setText(article.getDescription());

            holder.itemView.setOnClickListener(v -> {

            });

        }
    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }

    public static class ViewHolderArticle extends RecyclerView.ViewHolder {
        private ImageView article_image;
        private ImageView article_photo;
        private TextView article_title;
        private TextView article_content;

        public ViewHolderArticle(@NonNull View itemView) {
            super(itemView);
            this.article_image = itemView.findViewById(R.id.article_image);
            this.article_photo = itemView.findViewById(R.id.article_photo);
            this.article_title = itemView.findViewById(R.id.article_title);
            this.article_content = itemView.findViewById(R.id.article_content);
        }
    }

}
