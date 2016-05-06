package com.wuyouz.pubsub;

@FunctionalInterface
public interface Publisher {

    Message publish(Message message);
}
