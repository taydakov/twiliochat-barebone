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
import com.twilio.ipmessaging.TwilioIPMessagingClient;
import com.twilio.ipmessaging.TwilioIPMessagingSDK;
import com.twilio.ipmessaging.UserInfo;

import java.util.ArrayList;

public class OldTwilioChatAPI implements IChatAPI {
    public static final String TAG = "OldTwilioChatAPI";

    private String token;
    private Context context;
    private TwilioIPMessagingClient ipmClient;
    private IPMClientListener ipmClientListener;

    public OldTwilioChatAPI(final Context context, final String token) {
        this.token = token;
        this.context = context;

        Log.d(TAG, "Version of common library is " + com.twilio.common.BuildConfig.VERSION_CODE);
        Log.d(TAG, "Version of IPM library is " + com.twilio.ipmessaging.Version.SDK_VERSION);
    }

    @Override
    public void Initialize(final IChatEventListener eventListener) {
        TwilioIPMessagingSDK.initializeSDK(context, new Constants.InitListener() {
            @Override
            public void onInitialized() {
                InitializeAccessManager(eventListener);
            }

            @Override
            public void onError(Exception e) {
                eventListener.onError(e.getMessage());
            }
        });
    }

    private void InitializeAccessManager(final IChatEventListener eventListener) {
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
        ipmClientListener = new IPMClientListener();
        ipmClient = TwilioIPMessagingSDK.createIPMessagingClientWithAccessManager(accessManager, ipmClientListener);
        eventListener.onSuccess();
    }

    @Override
    public void RetrieveChannels(final IChatEventListener eventListener) {
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
    public String[] GetChannels() {
        Channel[] channelObjects = ipmClient.getChannels().getChannels();
        ArrayList<String> channels = new ArrayList<>();
        for (Channel channel : channelObjects) {
            channels.add(channel.getUniqueName());
        }
        return channels.toArray(new String[0]);
    }

    @Override
    public void RetrieveMessages(final String channelName, final IChatEventListener eventListener) {

    }

    @Override
    public void SendMessage(final String message, final IChatEventListener eventListener) {

    }
}

class IPMClientListener implements IPMessagingClientListener {
    @Override
    public void onChannelAdd(Channel channel) {

    }

    @Override
    public void onChannelChange(Channel channel) {

    }

    @Override
    public void onChannelDelete(Channel channel) {

    }

    @Override
    public void onError(ErrorInfo errorInfo) {

    }

    @Override
    public void onAttributesChange(String s) {

    }

    @Override
    public void onChannelHistoryLoaded(Channel channel) {

    }

    @Override
    public void onUserInfoChange(UserInfo userInfo) {

    }
}