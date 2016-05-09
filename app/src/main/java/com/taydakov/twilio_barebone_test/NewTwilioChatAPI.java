package com.taydakov.twilio_barebone_test;

import android.util.Log;

import com.twilio.common.TwilioAccessManager;
import com.twilio.common.TwilioAccessManagerListener;
import com.twilio.ipmessaging.Channel;
import com.twilio.ipmessaging.Constants;
import com.twilio.ipmessaging.ErrorInfo;
import com.twilio.ipmessaging.IPMessagingClientListener;
import com.twilio.ipmessaging.Messages;
import com.twilio.ipmessaging.TwilioIPMessagingClient;
import com.twilio.ipmessaging.TwilioIPMessagingSDK;

import java.util.ArrayList;
import java.util.Date;

public class NewTwilioChatApi implements ChatApi {
    public static final String TAG = "NewTwilioChatApi";

    private String token;
    private TwilioIPMessagingClient ipmClient;

    public NewTwilioChatApi(final String token) {
        this.token = token;

        Log.d(TAG, "Version of common library is " + com.twilio.common.BuildConfig.VERSION_CODE);
        Log.d(TAG, "Version of IPM library is " + com.twilio.ipmessaging.TwilioIPMessagingSDK.getVersion());
    }

    @Override
    public void initialize(final IChatEventListener eventListener) {
        TwilioAccessManager accessManager = new TwilioAccessManager() {
            @Override
            public void updateToken(String s) {
            }

            @Override
            public String getToken() {
                return token;
            }

            @Override
            public String getIdentity() {
                return null;
            }

            @Override
            public boolean isExpired() {
                return false;
            }

            @Override
            public Date getExpirationDate() {
                return null;
            }

            @Override
            public ArrayList<TwilioAccessManagerListener> getListeners() {
                return null;
            }

            @Override
            public void addListener(TwilioAccessManagerListener twilioAccessManagerListener) {

            }

            @Override
            public void removeListener(TwilioAccessManagerListener twilioAccessManagerListener) {

            }

            @Override
            public void dispose() {

            }
        };
        TwilioIPMessagingClient.Properties clientProps = new TwilioIPMessagingClient.Properties(
                TwilioIPMessagingClient.SynchronizationStrategy.ALL,
                50
        );
        Constants.CallbackListener<TwilioIPMessagingClient> listener = new Constants.CallbackListener<TwilioIPMessagingClient>() {
            @Override
            public void onSuccess(TwilioIPMessagingClient twilioIPMessagingClient) {
                ipmClient = twilioIPMessagingClient;
                eventListener.onSuccess();
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                super.onError(errorInfo);
                eventListener.onError(errorInfo.getErrorText());
            }
        };
        TwilioIPMessagingSDK.createClient(accessManager, clientProps, listener);
    }

    @Override
    public void retrieveChannels(final IChatEventListener eventListener) {
        ipmClient.getChannels().loadChannelsWithListener(new Constants.StatusListener() {
            @Override
            public void onSuccess() {
                eventListener.onSuccess();
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                super.onError(errorInfo);
                eventListener.onError(errorInfo.getErrorText());
            }
        });
    }

    @Override
    public String[] getChannels() {
        Channel[] channelObjects = ipmClient.getChannels().getChannels();
        ArrayList<String> channels = new ArrayList<>();
        for (Channel channel : channelObjects) {
            channels.add(channel.getUniqueName());
        }
        return channels.toArray(new String[0]);
    }

    @Override
    public void retrieveMessages(final String channelName, final IChatEventListener eventListener) {
        Messages messagesObject = ipmClient.getChannels().getChannelByUniqueName(channelName).getMessages();
        Log.d(TAG, "Number of messages is " + messagesObject.getMessages().length);
        eventListener.onSuccess();
    }

    @Override
    public void sendMessage(final String channelName, final String message, final IChatEventListener eventListener) {
        Messages messages = ipmClient.getChannels().getChannelByUniqueName(channelName).getMessages();
        messages.sendMessage(messages.createMessage(message), new Constants.StatusListener() {
            @Override
            public void onSuccess() {
                eventListener.onSuccess();
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                super.onError(errorInfo);
                eventListener.onError(errorInfo.getErrorText());
            }
        });
    }
}
