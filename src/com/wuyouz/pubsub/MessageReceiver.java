package com.wuyouz.pubsub;

public interface MessageReceiver {

    void addSubscriber(Subscriber subscriber);

    void removeSubscriber(Subscriber subscriber);

    void start();

    void stop();

}
