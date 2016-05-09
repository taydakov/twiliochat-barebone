package com.taydakov.twilio_barebone_test;

public interface ChatApi {
    void initialize(IChatEventListener eventListener);

    void retrieveChannels(IChatEventListener eventListener);

    String[] getChannels();

    void retrieveMessages(String channelName, IChatEventListener eventListener);

    void sendMessage(String channelName, String message, IChatEventListener eventListener);
}