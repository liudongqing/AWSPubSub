package com.wuyouz.pubsub;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.wuyouz.pubsub.aws.SQSMessageReceiver;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.junit.Test;

public class SQSMessageReceiverTest {

	private AWSCredentials credential = new BasicAWSCredentials("","");
	private String region = "ap-northeast-1";
	private String queueUrl = "https://sqs.ap-northeast-1.amazonaws.com/...";

	@Test
	public void testStart() {
		SQSMessageReceiver listener = new SQSMessageReceiver();
		listener.addSubscriber(message -> System.out.println(message));
		listener.initialize(credential, region, queueUrl );
		listener.start();
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		listener.stop();
		
	}

}
