package com.wuyouz.pubsub;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.wuyouz.pubsub.aws.AWSTopicManager;
import com.wuyouz.pubsub.aws.SNSMessageSender;
import com.wuyouz.pubsub.aws.SQSMessageReceiver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dqliu on 5/6/16.
 */
public class IntegrationTest {

    public static final String REGION_NAME = "ap-northeast-1";
    final SystemPropertiesCredentialsProvider awsCredentialsProvider = new SystemPropertiesCredentialsProvider();
    private AWSTopicManager manager = new AWSTopicManager();
    private Topic testTopic1;
    private Subscription subscription;

    @Before
    public void setupSubscription(){
        final Region region = Region.getRegion(Regions.fromName(REGION_NAME));
        AmazonSNSClient snsClient = new AmazonSNSClient(awsCredentialsProvider);
        snsClient.setRegion(region);
        AmazonSQSClient sqsClient = new AmazonSQSClient(awsCredentialsProvider);
        sqsClient.setRegion(region);
        manager.setSnsClient(snsClient);
        manager.setSqsClient(sqsClient);

        testTopic1 = manager.createTopic("testTopic1");
        subscription = manager.createSubscription(testTopic1);
    }

    @Test
    public void testPubSub() throws Exception {
        SQSMessageReceiver listener = new SQSMessageReceiver();
        listener.addSubscriber(message -> System.out.println(message));
        final AWSCredentials credentials = awsCredentialsProvider.getCredentials();
        listener.initialize(credentials, REGION_NAME, subscription.getQueueUrl() );
        listener.start();

        SNSMessageSender sender = new SNSMessageSender();
        sender.initialize(credentials, REGION_NAME, testTopic1.getUri());
        Message message = new Message("001","001","ABCD");
        assertTrue(sender.sendMessage(message));

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        listener.stop();
    }

    @After
    public void clearSubscriptioin(){
        manager.deleteSubscription(testTopic1, subscription);
        manager.deleteTopic(testTopic1);
    }
}