package com.dillonmccoy.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dillonmccoy.nytimessearch.R;
import com.dillonmccoy.nytimessearch.models.Article;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {
    private WebView wvArticle;
    private ShareActionProvider miShareAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wvArticle = (WebView) findViewById(R.id.wvArticle);

        Article article = Parcels.unwrap(getIntent().getParcelableExtra("article"));

        toolbar.setTitle(article.getHeadline());

        wvArticle.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wvArticle.loadUrl(article.getWebUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article, menu);

        MenuItem item = menu.findItem(R.id.miShare);
        // Fetch reference to the share action provider
        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        Log.e("ASDF" , "setting up sharing");
        shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());

        miShareAction.setShareIntent(shareIntent);


        return true;
    }

}
