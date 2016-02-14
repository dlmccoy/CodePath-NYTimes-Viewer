package com.dillonmccoy.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.dillonmccoy.nytimessearch.R;
import com.dillonmccoy.nytimessearch.adapters.ArticleArrayAdapter;
import com.dillonmccoy.nytimessearch.listeners.EndlessScrollListener;
import com.dillonmccoy.nytimessearch.models.Article;
import com.dillonmccoy.nytimessearch.models.Settings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    private EditText etQuery;
    private GridView gvResults;
    private Button bSearch;
    private ArticleArrayAdapter aArticles;
    private Settings settings;

    private ArrayList<Article> articles;

    private static final String API_BASE = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private static final String API_KEY = "749c0209def6af2f429675d14232454a:5:74407211";
    static final int SETTINGS_REQUEST = 0;
    static final int SAVE_SETTINGS_RESULT = 1;
    static final int CANCEL_SETTINGS_RESULT = 2;
    static final String SETTINGS_EXTRA = "settings";
    private static final int FIRST_PAGE = 0;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        bSearch = (Button) findViewById(R.id.bSearchButton);

        articles = new ArrayList<>();
        aArticles = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(aArticles);

        settings = new Settings();

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create an intent to display the article.
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);

                // Get the article to display
                Article article = articles.get(position);
                i.putExtra("article", Parcels.wrap(article));

                // Launch the activity
                startActivity(i);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                searchForArticles(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            switchToSettingsActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchToSettingsActivity() {
        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        i.putExtra(SETTINGS_EXTRA, Parcels.wrap(settings));

        startActivityForResult(i, SETTINGS_REQUEST);
    }

    // Callback for the search button. Start with the first page.
    public void onArticleSearch(View view) {
        searchForArticles(FIRST_PAGE);
    }

    private void searchForArticles(final int page) {

        // Construct the request params.
        RequestParams params = new RequestParams();
        params.put("api-key", API_KEY);
        params.put("page", page);
        params.put("q", etQuery.getText().toString());

        // Figure out the begin_date param.
        if (settings.beginDate != null) {
            params.put("begin_date", dateFormat.format(settings.beginDate));
        }

        // Figure out the sort param.
        String SORT_PARAM = "sort";
        switch (settings.sortOrder) {
            case Settings.SORT_NEWEST:
                params.put(SORT_PARAM, "newest");
                break;
            case Settings.SORT_OLDEST:
                params.put(SORT_PARAM, "oldest");
                break;
            default:
                break;
        }

        // Figure out the news desk values string.
        ArrayList<String> newsDeskValues = new ArrayList<>();
        if (settings.showArt) {
            newsDeskValues.add(getString(R.string.artsTxt));
        }
        if (settings.showFashion) {
            newsDeskValues.add(getString(R.string.fashionTxt));
        }
        if (settings.showSports) {
            newsDeskValues.add(getString(R.string.sportsTxt));
        }

        StringBuilder builder = new StringBuilder();
        builder.append("news_desk:(");
        for (String value : newsDeskValues) {
            builder.append("\"" + value + "\" ");
        }
        builder.append(")");
        if (!newsDeskValues.isEmpty()) {
            params.add("fq", builder.toString());
        }

        Log.e(TAG, "SEARCHING FOR ARTICLES: " + params.toString());

        // Actually make the json request.
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(API_BASE, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, "response: " + response.toString());

                JSONArray docs = null;

                if (page == 0) {
                    aArticles.clear();
                }
                try {
                    docs = response.getJSONObject("response").getJSONArray("docs");

                    aArticles.addAll(Article.getFromJSONArray(docs));

                } catch (JSONException e) {
                    Log.e(TAG, e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e(TAG, errorResponse.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != SAVE_SETTINGS_RESULT) {
            return;
        }
        Parcelable settingsParcel = data.getParcelableExtra(SETTINGS_EXTRA);

        // Only attempt to unwrap the parcelable if a result was returned.
        if (settingsParcel == null) {
            return;
        }

        settings = Parcels.unwrap(settingsParcel);
        searchForArticles(FIRST_PAGE);
    }
}
