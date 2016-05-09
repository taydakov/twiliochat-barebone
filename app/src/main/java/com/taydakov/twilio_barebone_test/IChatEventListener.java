package com.taydakov.twilio_barebone_test;


public interface IChatEventListener {
    void onSuccess();

    void onError(String message);
}