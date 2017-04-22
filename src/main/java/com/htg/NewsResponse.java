package com.htg;

/**
 * Created by Morten on 22/04/2017.
 */
public class NewsResponse {
    private String news;

    public NewsResponse(){};

    public NewsResponse(String news) {
        this.news = news;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }
}
