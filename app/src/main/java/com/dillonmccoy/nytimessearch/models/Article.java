package com.dillonmccoy.nytimessearch.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Article {
    private String webUrl;
    private String headline;

    public String getThumbnail() {
        return thumbnail;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    private String thumbnail;

    // empty constructor needed by the Parceler library
    public Article() {}

    public Article (JSONObject article) {
        try {
            this.webUrl = article.getString("web_url");
            this.headline = article.getJSONObject("headline").getString("main");

            JSONArray multimedia = article.getJSONArray("multimedia");

            if (multimedia.length() > 0) {
                JSONObject firstObject = multimedia.getJSONObject(0);
                this.thumbnail = "https://nytimes.com/" + firstObject.getString("url");
            } else {
                this.thumbnail = "";
            }
            this.thumbnail = article.getString("thumbnail");
        } catch (JSONException e) {

        }
    }

    public static ArrayList<Article> getFromJSONArray(JSONArray articles) {
        ArrayList<Article> results = new ArrayList<>();

        for (int i = 0; i < articles.length(); i++ ) {
            try {
                results.add(new Article(articles.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage());
            }
        }
        return results;
    }
}
