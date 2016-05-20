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
        // test new API: chatAPI = new NewTwilioChatApi(""); // TODO: put Twilio token
        chatAPI = new NewTwilioChatApi("eyJjdHkiOiAidHdpbGlvLWZwYTt2PTEiLCAiYWxnIjogIkhTMjU2IiwgInR5cCI6ICJKV1QifQ.eyJleHAiOiAxNDYzODUyODgyLCAianRpIjogIlNLMGIxNDRjYmIzMjIyNmExNzkwYTJlZGMxMGY3MTRjOTYtMTQ2Mzc3MDA4MiIsICJncmFudHMiOiB7ImlwX21lc3NhZ2luZyI6IHsiZW5kcG9pbnRfaWQiOiAiT25lVW5pQXBwOjQ6dGVzdGFwcCIsICJzZXJ2aWNlX3NpZCI6ICJJU2ZiOGZiOWYxMTNjMTRjMGFhNDg2MGYxM2I2NmU2ZWEzIn0sICJpZGVudGl0eSI6IDR9LCAic3ViIjogIkFDMGQ0NzA2MjJkNjc1MGQ3ZDJiM2NkZWEyMmFiMTI0OGEiLCAiaXNzIjogIlNLMGIxNDRjYmIzMjIyNmExNzkwYTJlZGMxMGY3MTRjOTYifQ.7x2R68ee_NRQe-KJNztc4gjx6HvYxOQ_3cms163f4GA"); // TODO: put Twilio token
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
