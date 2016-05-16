package com.wuyouz.pubsub;

import java.util.List;

/**
 * Created by dqliu on 5/16/16.
 */
public interface TopicManager {
    Topic createTopic(String name);

    void deleteTopic(Topic topic);

    boolean topicExists(Topic topic);

    Subscription createSubscription(Topic topic);

    void deleteSubscription(Topic topic, Subscription subscription);

    List<Subscription> listSubscriptions(Topic testTopic1);
}
