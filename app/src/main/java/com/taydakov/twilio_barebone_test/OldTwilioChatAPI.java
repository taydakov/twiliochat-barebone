package com.taydakov.twilio_barebone_test;

import android.content.Context;
import android.util.Log;

import com.twilio.common.TwilioAccessManager;
import com.twilio.common.TwilioAccessManagerFactory;
import com.twilio.common.TwilioAccessManagerListener;
import com.twilio.ipmessaging.Channel;
import com.twilio.ipmessaging.Constants;
import com.twilio.ipmessaging.ErrorInfo;
import com.twilio.ipmessaging.IPMessagingClientListener;
import com.twilio.ipmessaging.Messages;
import com.twilio.ipmessaging.TwilioIPMessagingClient;
import com.twilio.ipmessaging.TwilioIPMessagingSDK;
import com.twilio.ipmessaging.UserInfo;

import java.util.ArrayList;

public class OldTwilioChatApi implements ChatApi {
    public static final String TAG = "OldTwilioChatApi";

    private String token;
    private Context context;
    private TwilioIPMessagingClient ipmClient;
    private DumbIPMessagingClientListener ipmClientListener;

    public OldTwilioChatApi(final Context context, final String token) {
        this.token = token;
        this.context = context;

        Log.d(TAG, "Version of common library is " + com.twilio.common.BuildConfig.VERSION_CODE);
        Log.d(TAG, "Version of IPM library is " + com.twilio.ipmessaging.TwilioIPMessagingSDK.getVersion());
    }

    @Override
    public void initialize(final IChatEventListener eventListener) {
        TwilioIPMessagingSDK.initializeSDK(context, new Constants.InitListener() {
            @Override
            public void onInitialized() {
                initializeAccessManager(eventListener);
            }

            @Override
            public void onError(Exception e) {
                eventListener.onError(e.getMessage());
            }
        });
    }

    private void initializeAccessManager(final IChatEventListener eventListener) {
        TwilioAccessManager accessManager = TwilioAccessManagerFactory.createAccessManager(token, new TwilioAccessManagerListener() {
            @Override
            public void onTokenExpired(TwilioAccessManager twilioAccessManager) {

            }

            @Override
            public void onTokenUpdated(TwilioAccessManager twilioAccessManager) {

            }

            @Override
            public void onError(TwilioAccessManager twilioAccessManager, String s) {
                eventListener.onError(s);
            }
        });
        ipmClientListener = new DumbIPMessagingClientListener();
        ipmClient = TwilioIPMessagingSDK.createIPMessagingClientWithAccessManager(accessManager, ipmClientListener);
        eventListener.onSuccess();
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

class DumbIPMessagingClientListener implements IPMessagingClientListener {
    public static final String TAG = "DumbIPMClientListener";

    @Override
    public void onClientSynchronization(TwilioIPMessagingClient.SynchronizationStatus synchronizationStatus) {
        Log.d(TAG, "Client synchronized, status is " + synchronizationStatus.name());
    }

    @Override
    public void onChannelAdd(Channel channel) {
        Log.d(TAG, "Channel added, " + channel.getUniqueName());
    }

    @Override
    public void onChannelChange(Channel channel) {
        Log.d(TAG, "Channel changed, " + channel.getUniqueName());
    }

    @Override
    public void onChannelDelete(Channel channel) {
        Log.d(TAG, "Channel deleted, " + channel.getUniqueName());
    }

    @Override
    public void onError(ErrorInfo errorInfo) {

    }

    @Override
    public void onAttributesChange(String s) {

    }

    @Override
    public void onChannelHistoryLoaded(Channel channel) {
        Log.d(TAG, "Channel history loaded, " + channel.getUniqueName());
    }

    @Override
    public void onUserInfoChange(UserInfo userInfo) {

    }
}