package com.taydakov.twilio_barebone_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private static final String CHANNEL_NAME = "private-coach-4-3";

    private ChatApi chatAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Test Twilio Chat API */
        Log.d(TAG, "Chat initialization...");
        // test old API: chatAPI = new OldTwilioChatApi(this, ""); // TODO: put Twilio token
        chatAPI = new OldTwilioChatApi(this, "eyJ0eXAiOiAiSldUIiwgImFsZyI6ICJIUzI1NiIsICJjdHkiOiAidHdpbGlvLWZwYTt2PTEifQ.eyJqdGkiOiAiU0swYjE0NGNiYjMyMjI2YTE3OTBhMmVkYzEwZjcxNGM5Ni0xNDYzNjA1NTkyIiwgInN1YiI6ICJBQzBkNDcwNjIyZDY3NTBkN2QyYjNjZGVhMjJhYjEyNDhhIiwgImdyYW50cyI6IHsiaXBfbWVzc2FnaW5nIjogeyJzZXJ2aWNlX3NpZCI6ICJJU2ZiOGZiOWYxMTNjMTRjMGFhNDg2MGYxM2I2NmU2ZWEzIiwgImVuZHBvaW50X2lkIjogIk9uZVVuaUFwcDo0OnRlc3RhcHAifSwgImlkZW50aXR5IjogNH0sICJleHAiOiAxNDYzNjg4MzkyLCAiaXNzIjogIlNLMGIxNDRjYmIzMjIyNmExNzkwYTJlZGMxMGY3MTRjOTYifQ.UBmLZ4hnBqh-qdNIdm5Wmyu6--V5Ui5etzi44zSiO-A"); // TODO: put Twilio token
        chatAPI.initialize(new IChatEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Chat successfully initialized");
                retrieveChannels();
            }

            @Override
            public void onError(String message) {
                Log.d(TAG, "Chat failed to initialize, error is " + message);
            }
        });
    }

    void retrieveChannels() {
        chatAPI.retrieveChannels(new IChatEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Channels successfully retrieved");
                String[] channels = chatAPI.getChannels();
                String channelsListString = TextUtils.join(", ", channels);
                Log.d(TAG, "Number of channels is " + channels.length + ": " + channelsListString);
                retrieveMessages(CHANNEL_NAME);
            }

            @Override
            public void onError(String message) {
                Log.d(TAG, "Channels failed to retrieve, error is " + message);
            }
        });
    }

    private void retrieveMessages(final String channelName) {
        chatAPI.retrieveMessages(channelName, new IChatEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Messages retrieved successfully for " + channelName);
                sendMessage(channelName);
            }

            @Override
            public void onError(String message) {
                Log.d(TAG, "Messages failed to retrieve, error is " + message);
            }
        });
    }

    private void sendMessage(final String channelName) {
        chatAPI.sendMessage(channelName, "Hello world from Twilio Barebone Test", new IChatEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Message has been successfully sent to " + channelName);
            }

            @Override
            public void onError(String message) {
                Log.d(TAG, "Message failed to send, error is " + message);
            }
        });
    }
}
