package com.wuyouz.pubsub;

import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.NotFoundException;

/**
 * Created by dqliu on 5/7/16.
 */
public class TopicManager {

    private AmazonSNSClient snsClient ;

    public void setSnsClient(AmazonSNSClient snsClient) {
        this.snsClient = snsClient;
    }

    public Topic createTopic(String name) {
        try {
            CreateTopicResult createTopicResult = snsClient.createTopic(name);
            final Topic topic = new Topic();
            topic.setName(name);
            topic.setUri(createTopicResult.getTopicArn());
            return topic;
        } catch (Error error) {
            return null;
        }
    }

    public void deleteTopic(Topic topic) {
        snsClient.deleteTopic(topic.getUri());
    }

    public boolean topicExists(Topic topic) {
        try {
            snsClient.getTopicAttributes(topic.getUri());
            return true;
        } catch (NotFoundException exception) {
            return false;
        }
    }
}
