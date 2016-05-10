//package com.taydakov.twilio_barebone_test;
//
//import android.util.Log;
//
//import com.twilio.common.TwilioAccessManager;
//import com.twilio.common.TwilioAccessManagerListener;
//import com.twilio.ipmessaging.Channel;
//import com.twilio.ipmessaging.Constants;
//import com.twilio.ipmessaging.ErrorInfo;
//import com.twilio.ipmessaging.IPMessagingClientListener;
//import com.twilio.ipmessaging.Messages;
//import com.twilio.ipmessaging.TwilioIPMessagingClient;
//import com.twilio.ipmessaging.TwilioIPMessagingSDK;
//import com.twilio.ipmessaging.UserInfo;
//
//import java.util.ArrayList;
//import java.util.Date;
//
//public class NewTwilioChatApi implements ChatApi {
//    public static final String TAG = "NewTwilioChatApi";
//
//    private String token;
//    private TwilioIPMessagingClient ipmClient;
//    private IPMessagingClientListener ipmClientListener;
//
//    public NewTwilioChatApi(final String token) {
//        this.token = token;
//
//        Log.d(TAG, "Version of common library is " + com.twilio.common.BuildConfig.VERSION_CODE);
//        Log.d(TAG, "Version of IPM library is " + com.twilio.ipmessaging.TwilioIPMessagingSDK.getVersion());
//    }
//
//    @Override
//    public void initialize(final IChatEventListener eventListener) {
//        TwilioAccessManager accessManager = new TwilioAccessManager() {
//            @Override
//            public void updateToken(String s) {
//            }
//
//            @Override
//            public String getToken() {
//                return token;
//            }
//
//            @Override
//            public String getIdentity() {
//                return null;
//            }
//
//            @Override
//            public boolean isExpired() {
//                return false;
//            }
//
//            @Override
//            public Date getExpirationDate() {
//                return null;
//            }
//
//            @Override
//            public ArrayList<TwilioAccessManagerListener> getListeners() {
//                return null;
//            }
//
//            @Override
//            public void addListener(TwilioAccessManagerListener twilioAccessManagerListener) {
//
//            }
//
//            @Override
//            public void removeListener(TwilioAccessManagerListener twilioAccessManagerListener) {
//
//            }
//
//            @Override
//            public void dispose() {
//
//            }
//        };
//        TwilioIPMessagingClient.Properties clientProps = new TwilioIPMessagingClient.Properties(
//                TwilioIPMessagingClient.SynchronizationStrategy.CHANNELS_LIST,
//                50
//        );
//        Constants.CallbackListener<TwilioIPMessagingClient> listener = new Constants.CallbackListener<TwilioIPMessagingClient>() {
//            @Override
//            public void onSuccess(TwilioIPMessagingClient twilioIPMessagingClient) {
//            }
//
//            @Override
//            public void onError(ErrorInfo errorInfo) {
//                super.onError(errorInfo);
//                eventListener.onError(errorInfo.getErrorText());
//            }
//        };
//        ipmClientListener = new DumbNewIPMessagingClientListener(eventListener);
//        ipmClient = TwilioIPMessagingSDK.createClient(accessManager, clientProps, listener);
//        ipmClient.setListener(ipmClientListener);
//    }
//
//    @Override
//    public void retrieveChannels(final IChatEventListener eventListener) {
//        eventListener.onSuccess();
//    }
//
//    @Override
//    public String[] getChannels() {
//        Channel[] channelObjects = ipmClient.getChannels().getChannels();
//        ArrayList<String> channels = new ArrayList<>();
//        for (Channel channel : channelObjects) {
//            channels.add(channel.getUniqueName());
//        }
//        return channels.toArray(new String[0]);
//    }
//
//    @Override
//    public void retrieveMessages(final String channelName, final IChatEventListener eventListener) {
//        Channel.SynchronizationStatus status = ipmClient.getChannels().getChannelByUniqueName(channelName).getSynchronizationStatus();
//        ipmClient.getChannels().getChannelByUniqueName(channelName).synchronize(new Constants.CallbackListener<Channel>() {
//            @Override
//            public void onSuccess(Channel channel) {
//                Messages messagesObject = ipmClient.getChannels().getChannelByUniqueName(channelName).getMessages();
//                Log.d(TAG, "Number of messages is " + messagesObject.getMessages().length);
//                eventListener.onSuccess();
//            }
//
//            @Override
//            public void onError(ErrorInfo errorInfo) {
//                super.onError(errorInfo);
//                eventListener.onError(errorInfo.getErrorText());
//            }
//        });
//    }
//
//    @Override
//    public void sendMessage(final String channelName, final String message, final IChatEventListener eventListener) {
//        Messages messages = ipmClient.getChannels().getChannelByUniqueName(channelName).getMessages();
//        messages.sendMessage(messages.createMessage(message), new Constants.StatusListener() {
//            @Override
//            public void onSuccess() {
//                eventListener.onSuccess();
//            }
//
//            @Override
//            public void onError(ErrorInfo errorInfo) {
//                super.onError(errorInfo);
//                eventListener.onError(errorInfo.getErrorText());
//            }
//        });
//    }
//}
//
//class DumbNewIPMessagingClientListener implements IPMessagingClientListener {
//    public static final String TAG = "DumbIPMClientListener";
//
//    private IChatEventListener listener;
//
//    public DumbNewIPMessagingClientListener(IChatEventListener listener) {
//        this.listener = listener;
//    }
//
//    @Override
//    public void onClientSynchronization(TwilioIPMessagingClient.SynchronizationStatus synchronizationStatus) {
//        Log.d(TAG, "Client synchronized, status is " + synchronizationStatus.name());
//        if (synchronizationStatus == TwilioIPMessagingClient.SynchronizationStatus.COMPLETED) {
//            listener.onSuccess();
//        }
//    }
//
//    @Override
//    public void onChannelAdd(Channel channel) {
//        Log.d(TAG, "Channel added, " + channel.getUniqueName());
//    }
//
//    @Override
//    public void onChannelChange(Channel channel) {
//        Log.d(TAG, "Channel changed, " + channel.getUniqueName());
//    }
//
//    @Override
//    public void onChannelDelete(Channel channel) {
//        Log.d(TAG, "Channel deleted, " + channel.getUniqueName());
//    }
//
//    @Override
//    public void onError(ErrorInfo errorInfo) {
//
//    }
//
//    @Override
//    public void onAttributesChange(String s) {
//
//    }
//
//    @Override
//    public void onChannelHistoryLoaded(Channel channel) {
//        Log.d(TAG, "Channel history loaded, " + channel.getUniqueName());
//    }
//
//    @Override
//    public void onUserInfoChange(UserInfo userInfo) {
//
//    }
//}