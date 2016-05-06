package com.wuyouz.pubsub;

public class Message {
    private String messageId;
    private String signature;
    private String messageBody;

    public Message() {

    }

    public Message(String entityId, String signature,
                   String messageBody) {
        super();
        this.messageId = entityId;
        this.messageBody = messageBody;
        this.signature = signature;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSignature() {
        return signature;
    }

    public String getMessageBody() {
        return messageBody;
    }

}
