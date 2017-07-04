package com.rohsins.project_test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlwaysRunner extends Service implements MqttCallbackExtended {

    public volatile String globalLoadBrokerAddress;
    public static String globalUniqueId;

    public static MqttClient globalMqttClient;
    public static String globalPublishTopic;
    public static String globalSubscribeTopic;
    public static String globalChatTopic;
    public static int globalQos;
    public static String globalBrokerAddress;
    public static String globalClientId;
    public static MemoryPersistence globalPersistence;
    public static MqttConnectOptions globalConnectOptions;
    public static Boolean globalMqttRetained;

    NotificationCompat.Builder globalNotificationBuilder;
    NotificationManager globalNotificationManager;
    public static int globalNotificationId = 0;
    public static String globalNotificationMessage;
    public static boolean serviceIsAlive = false;

    Handler globalNotificationHandler = new Handler();
    Handler executeService = new Handler();

    Runnable globalNotificationRunnable = new Runnable() {
        @Override
        public void run() {
            globalNotificationBuilder.setContentText(globalNotificationMessage);
            globalNotificationBuilder.setShowWhen(true);
            globalNotificationManager.notify(globalNotificationId, globalNotificationBuilder.build());
        }
    };

    Runnable globalMqttLaunchRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                globalMqttClient = new MqttClient(globalBrokerAddress, globalClientId, globalPersistence);
                globalMqttClient.connect(globalConnectOptions);
                globalMqttClient.setCallback(AlwaysRunner.this);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    };

    Runnable stopServiceRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                globalMqttClient.unsubscribe(globalSubscribeTopic);
                globalMqttClient.unsubscribe(globalChatTopic);
                globalMqttClient.disconnect();
                globalMqttClient.close();
            } catch (MqttException e) {
                e.printStackTrace();
            }
            serviceIsAlive = false;
            Toast.makeText(AlwaysRunner.this, "Killing Service", Toast.LENGTH_SHORT).show();
        }
    };

    Thread globalMqttLaunchThread = new Thread(globalMqttLaunchRunnable);

    public static class MessageEvent {
        String messageData;
        String messageTopic;

        MessageEvent(String topic, String data) {
            messageTopic = topic;
            messageData = data;
        }

        public String getMessageData() {
            return messageData;
        }

        public String getMessageTopic() {
            return messageTopic;
        }
    }

    @Override
    public void onCreate() {

        globalUniqueId = android.provider.Settings.Secure.getString(this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        SharedPreferences settings = getSharedPreferences("msettings", 0);
        globalLoadBrokerAddress = settings.getString("MQTTBROKERADDRESS", "m2m.eclipse.org");

        globalBrokerAddress = "tcp://" + globalLoadBrokerAddress + ":1883";
        globalPublishTopic = "RTSR&D/rozbor/pub";
        globalSubscribeTopic = "RTSR&D/rozbor/sub/" + globalUniqueId;
        globalChatTopic = "RTSR&D/rozbor/chatpub";
        globalQos = 2;
        globalClientId = "rohsins";
        globalPersistence = new MemoryPersistence();
        globalConnectOptions = new MqttConnectOptions();
        globalConnectOptions.setAutomaticReconnect(true);
        globalConnectOptions.setCleanSession(false);
        globalConnectOptions.setUserName("rtshardware");
        globalConnectOptions.setPassword(("rtshardware").toCharArray());
        globalMqttRetained = false;

        globalNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.motor_controls)
                .setContentTitle("Mqtt Notification")
                .setLights(Color.YELLOW, 1000, 3000)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS);

        globalNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        globalMqttLaunchThread.start();
    }

    @Override
    public void connectComplete(boolean b, String s) {
        if (b) {
            try {
                globalMqttClient.subscribe(globalSubscribeTopic, globalQos);
                globalMqttClient.subscribe(globalChatTopic, globalQos);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        Toast.makeText(this, "Connection Lost", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        JSONObject jsonMqttMessage = new JSONObject(mqttMessage.toString());

        if (!Mqtt.mqttViewerOn && !Chat.mqttChatOn && s.contains("RTSR&D/rozbor/chatpub")) {
            EventBus.getDefault().post(new AlwaysRunner.MessageEvent(s, mqttMessage.toString()));
            globalNotificationBuilder.setContentTitle("Chat Notification");
            globalNotificationMessage = jsonMqttMessage.toString();
            globalNotificationHandler.post(globalNotificationRunnable);
        } else if (!Chat.mqttChatOn && !Mqtt.mqttViewerOn && s.contains("RTSR&D/rozbor/sub/")){
            EventBus.getDefault().post(new AlwaysRunner.MessageEvent(s, mqttMessage.toString()));
            globalNotificationBuilder.setContentTitle("Viewer Notification");
            globalNotificationMessage = jsonMqttMessage.toString();
            globalNotificationHandler.post(globalNotificationRunnable);
        } else if (Mqtt.mqttViewerOn && s.contains("RTSR&D/rozbor/chatpub")) {
            EventBus.getDefault().post(new AlwaysRunner.MessageEvent(s, mqttMessage.toString()));
            globalNotificationBuilder.setContentTitle("Chat Notification");
            globalNotificationMessage = jsonMqttMessage.toString();
            globalNotificationHandler.post(globalNotificationRunnable);
        } else if (Chat.mqttChatOn && s.contains("RTSR&D/rozbor/sub/")){
            EventBus.getDefault().post(new AlwaysRunner.MessageEvent(s, mqttMessage.toString()));
            globalNotificationBuilder.setContentTitle("Viewer Notification");
            globalNotificationMessage = jsonMqttMessage.toString();
            globalNotificationHandler.post(globalNotificationRunnable);
        } else {
            EventBus.getDefault().post(new AlwaysRunner.MessageEvent(s, mqttMessage.toString()));
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIsAlive = true;
        Toast.makeText(this, "Starting Service", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        executeService.post(stopServiceRunnable);
    }
}
