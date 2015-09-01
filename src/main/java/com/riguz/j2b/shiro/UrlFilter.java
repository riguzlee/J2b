package com.riguz.j2b.shiro;

public class UrlFilter {
    String url;
    String filters;

    public UrlFilter(String url, String filters) {
        this.url = url;
        this.filters = filters;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

}
