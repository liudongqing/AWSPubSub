package com.wuyouz.pubsub;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicResult;
import com.amazonaws.services.sns.model.NotFoundException;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by dqliu on 5/7/16.
 */
public class TopicManager {

    public static final String QUEUE_ARN_ATTR_NAME = "QueueArn";
    public static final String PROTOCOL_SQS = "sqs";
    private AmazonSNSClient snsClient;
    private AmazonSQSClient sqsClient;

    public void setSnsClient(AmazonSNSClient snsClient) {
        this.snsClient = snsClient;
    }

    public void setSqsClient(AmazonSQSClient sqsClient) {
        this.sqsClient = sqsClient;
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

    public Subscription createSubscription(Topic topic) {
        CreateQueueResult createQueueResult = sqsClient.createQueue(topic.getName() + UUID.randomUUID());
        final String queueUrl = createQueueResult.getQueueUrl();
        final GetQueueAttributesResult attributesResult = sqsClient.getQueueAttributes(queueUrl,
                Lists.newArrayList(QUEUE_ARN_ATTR_NAME));
        SubscribeResult subscribeResult = snsClient.subscribe(topic.getUri(), PROTOCOL_SQS,
                attributesResult.getAttributes().get(QUEUE_ARN_ATTR_NAME));
        subscribeResult.getSubscriptionArn();
        return new Subscription(subscribeResult.getSubscriptionArn(), queueUrl);
    }

    public void deleteSubscription(Topic topic, Subscription subscription) {
        snsClient.unsubscribe(subscription.getUri());
        sqsClient.deleteQueue(subscription.getQueueUrl());
    }

    public List<Subscription> listSubscriptions(Topic testTopic1) {
        final ListSubscriptionsByTopicResult result = snsClient.listSubscriptionsByTopic(testTopic1.getUri());
        return result.getSubscriptions().stream().map(TopicManager::convertSubscription).collect(Collectors.toList());
    }

    public static Subscription convertSubscription(com.amazonaws.services.sns.model.Subscription subscription) {
        final String subscriptionArn = subscription.getSubscriptionArn();
        final String endpoint = subscription.getEndpoint();
        return new Subscription(subscriptionArn, endpoint);
    }
}
