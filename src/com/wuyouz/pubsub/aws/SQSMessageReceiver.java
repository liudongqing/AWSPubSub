package com.wuyouz.pubsub.aws;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuyouz.pubsub.MessageReceiver;
import com.wuyouz.pubsub.Subscriber;

/**
 * 
 * The dispatcher that listens the SQS queue and send the change to consumer
 * 
 * @author dqliu
 *
 */
public class SQSMessageReceiver implements Runnable, MessageReceiver {

	private Subscriber subscriber;
	private final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(1);
	private AmazonSQSClient sqs;
	private String queueUrl;

	public void initialize(AWSCredentials credential, String region, String queueUrl) {
		sqs = new AmazonSQSClient(credential);
		sqs.setRegion(Region.getRegion(Regions.fromName(region)));
		this.queueUrl = queueUrl;
	}

	private void receiveMessage() {
		System.out.println("start to recieve msg fro queue");
		ReceiveMessageResult msgResult = sqs.receiveMessage(queueUrl);
		List<Message> messages = msgResult.getMessages();
		for (Message msg : messages) {
			if (subscriber != null){
				try {
					subscriber.onMessage(parseToChange(msg.getBody()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sqs.deleteMessage(queueUrl, msg.getReceiptHandle());;
		}
	}

	private com.wuyouz.pubsub.Message parseToChange(String json) throws Exception {
		System.out.println(json);
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> snsDataMap = mapper.readValue(json, Map.class);
		String changeJson = snsDataMap.get("Message").toString();
		return mapper.readValue(changeJson, com.wuyouz.pubsub.Message.class);
	}

	@Override
	public void addSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	@Override
	public void removeSubscriber(Subscriber subscriber) {

	}

	@Override
	public void start() {
		scheduler.scheduleAtFixedRate(this, 0, 3, TimeUnit.SECONDS);
	}

	@Override
	public void stop() {
		if (scheduler.isShutdown()){
			scheduler.shutdown();
		}
	}

	@Override
	public void run() {
		System.out.println("start to recieve msg fro queue");
		receiveMessage();
	}
}
