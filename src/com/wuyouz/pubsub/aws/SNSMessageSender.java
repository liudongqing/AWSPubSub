package com.wuyouz.pubsub.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuyouz.pubsub.Message;
import com.wuyouz.pubsub.MessageSender;

public class SNSMessageSender implements MessageSender {

	private AmazonSNSClient sns ;
	private String topicArn ;
	
	@Override
	public boolean sendMessage(Message notification) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String jsonMsgBody = mapper.writeValueAsString(notification);
		String messageId = pubishToTopic(jsonMsgBody);
		return messageId != null;
	}
	
	public void initialize(AWSCredentials credential, String region, String topicArn){
        sns = new AmazonSNSClient(credential);
        sns.setRegion(Region.getRegion(Regions.fromName(region)));
        this.topicArn = topicArn;
	}
	
	private String pubishToTopic(String message){
		PublishRequest publishRequest = new PublishRequest(topicArn, message);
		PublishResult publishResult = sns.publish(publishRequest);
		System.out.println("MessageId - " + publishResult.getMessageId());
		return publishResult.getMessageId();
	}

}
