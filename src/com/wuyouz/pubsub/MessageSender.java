package com.wuyouz.pubsub;

public interface MessageSender {

    boolean sendMessage(Message message) throws Exception;

}
