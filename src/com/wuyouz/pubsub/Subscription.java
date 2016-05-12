package com.wuyouz.pubsub;

/**
 * Created by dqliu on 5/12/16.
 */
public class Subscription {

    private final String uri;
    private final String queueUrl;

    Subscription(final String uri, final String queueUrl) {
        this.uri = uri;
        this.queueUrl = queueUrl;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return String.format("(Subscribed to Queue: %s )", uri);
    }

    public String getQueueUrl() {
        return queueUrl;
    }
}
