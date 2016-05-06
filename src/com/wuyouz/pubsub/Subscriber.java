package com.wuyouz.pubsub;

@FunctionalInterface
public interface Subscriber {

    void onMessage(Message message);

}
