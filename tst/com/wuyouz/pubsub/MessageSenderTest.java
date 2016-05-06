package com.wuyouz.pubsub;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.wuyouz.pubsub.aws.SNSMessageSender;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dqliu on 5/6/16.
 */
public class MessageSenderTest {

    private AWSCredentials credential = new BasicAWSCredentials("","");
    private String region = "ap-northeast-1";
    private String topicArn = "arn:aws:sns:ap-northeast-1:";

    @Test
    public void testPublishEntityChange() throws Exception {
        SNSMessageSender sender = new SNSMessageSender();
        sender.initialize(credential, region, topicArn);
        Message message = new Message("001","001","ABCD");
        assertTrue(sender.sendMessage(message));
    }
}