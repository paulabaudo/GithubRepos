package com.globant.paulabaudo.githubrepos;

/**
 * Created by paula.baudo on 10/02/2015.
 */
public class GithubRepo {

    private String name;
    private String description;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return name + ", " + description;
    }
}
