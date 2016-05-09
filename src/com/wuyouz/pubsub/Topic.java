package com.wuyouz.pubsub;

/**
 * Created by dqliu on 5/7/16.
 */
public class Topic {
    private String name;
    private String uri;

    public String getUri() {
        return uri;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
