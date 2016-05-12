package com.wuyouz.pubsub;

import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dqliu on 5/7/16.
 */
public class TopicManagerTest {

    private TopicManager manager = new TopicManager();

    @Before
    public void setUp() throws Exception {
        final SystemPropertiesCredentialsProvider awsCredentialsProvider = new SystemPropertiesCredentialsProvider();
        final Region region = Region.getRegion(Regions.fromName("ap-northeast-1"));
        AmazonSNSClient snsClient = new AmazonSNSClient(awsCredentialsProvider);
        snsClient.setRegion(region);
        AmazonSQSClient sqsClient = new AmazonSQSClient(awsCredentialsProvider);
        sqsClient.setRegion(region);
        manager.setSnsClient(snsClient);
        manager.setSqsClient(sqsClient);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void createTopic() throws Exception {
        Topic testTopic1 = manager.createTopic("testTopic1");
        assertEquals("testTopic1", testTopic1.getName());
        assertEquals("arn:aws:sns:ap-northeast-1:930510034119:testTopic1", testTopic1.getUri());
        assertTrue(manager.topicExists(testTopic1));

        manager.deleteTopic(testTopic1);
        assertFalse(manager.topicExists(testTopic1));
    }

    @Test
    public void createSubscription() throws Exception {
        Topic testTopic1 = manager.createTopic("testTopic1");
        Subscription subscription = manager.createSubscription(testTopic1);
        assertNotNull(subscription);
        assertNotNull(subscription.getUri());

        List<Subscription> subscriptions = manager.listSubscriptions(testTopic1);
        System.out.println(subscriptions);
        assertEquals(1, subscriptions.size());
        manager.deleteSubscription(testTopic1, subscription);
        subscriptions = manager.listSubscriptions(testTopic1);
        System.out.println(subscriptions);
        assertEquals(0, subscriptions.size());

        manager.deleteTopic(testTopic1);
        assertFalse(manager.topicExists(testTopic1));
    }

}