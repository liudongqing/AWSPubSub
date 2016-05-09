package com.wuyouz.pubsub;

import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dqliu on 5/7/16.
 */
public class TopicManagerTest {

    private TopicManager manager = new TopicManager();

    @Before
    public void setUp() throws Exception {
        AmazonSNSClient snsClient = new AmazonSNSClient(new SystemPropertiesCredentialsProvider());
        snsClient.setRegion(Region.getRegion(Regions.fromName("ap-northeast-1")));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void createTopic() throws Exception {
        Topic testTopic1 = manager.createTopic("testTopic1");
        assertEquals("testTopic1",testTopic1.getName());
        assertEquals("arn:aws:sns:us-east-1:930510034119:testTopic1",testTopic1.getUri());
        assertTrue(manager.topicExists(testTopic1));

        manager.deleteTopic(testTopic1);
        assertFalse(manager.topicExists(testTopic1));
    }

}