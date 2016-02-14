package com.dillonmccoy.nytimessearch.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dillonmccoy.nytimessearch.models.Article;
import com.dillonmccoy.nytimessearch.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_article_result, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.headline = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.headline.setText(article.getHeadline());

        String thumbnail = article.getThumbnail();

        viewHolder.image.setImageResource(0);
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(viewHolder.image);

        }
        return convertView;
    }
}
