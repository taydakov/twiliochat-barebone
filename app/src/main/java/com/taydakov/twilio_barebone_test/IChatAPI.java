package com.taydakov.twilio_barebone_test;

public interface IChatAPI {
    void Initialize(IChatEventListener eventListener);

    void RetrieveChannels(IChatEventListener eventListener);

    String[] GetChannels();

    void RetrieveMessages(String channelName, IChatEventListener eventListener);

    void SendMessage(String message, IChatEventListener eventListener);
}