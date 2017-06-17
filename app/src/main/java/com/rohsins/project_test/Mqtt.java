package com.rohsins.project_test;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Date;

public class Mqtt extends Connectivity implements MqttCallback {

    TextView textView;

    MqttClient mqttClient;
    String topic;
    int qos;
    String broker;
    String clientId;
    MemoryPersistence persistence;
    MqttConnectOptions connOpts;

    NotificationCompat.Builder mBuilder;
    Intent resultIntent;
    TaskStackBuilder stackBuilder;
    PendingIntent resultPendingIntent;
    NotificationManager mNotificationManager;
    int mId = 0;
    boolean backRun = false;

//    private static MqttMessage mqttMessageTextView;
    private static String mqttMessageTextView;
    private Handler runUi = new Handler();
    private Handler notificationHandler = new Handler();

    Runnable uiRunnable = new Runnable() {
        @Override
        public void run() {
            textView.append(mqttMessageTextView.toString());

            int totalLines = ((textView.getHeight())/textView.getLineHeight());
            if (textView.getLineCount() >= totalLines) {
                textView.setScrollY((textView.getLineCount() - totalLines) * textView.getLineHeight());
                if (textView.getLineCount() > 20000) {
                    textView.setText("");
                }
            }
        }
    };

    Runnable launchMqtt = new Runnable() {
        @Override
        public void run() {
            try {
                mqttClient = new MqttClient(broker, clientId, persistence);
                mqttClient.connect(connOpts);
                mqttClient.setCallback(Mqtt.this);
                mqttClient.subscribe(topic, qos);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    };

    Runnable notificationRunnable = new Runnable() {
        @Override
        public void run() {
            if (backRun) {
                mBuilder.setContentText(mqttMessageTextView.toString());
                mNotificationManager.notify(mId, mBuilder.build());
            }
        }
    };

    Thread mqttThread = new Thread(launchMqtt);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);

        on_create_func();

        textView = (TextView) findViewById(R.id.mqttTextView01);
        textView.setMovementMethod(new ScrollingMovementMethod());

        topic = "RTSR&D/rozbor/sub/D21348830";
        qos = 2;
        broker = "tcp://" + brokerAddress + ":1883";
        clientId = uniqueId;
        persistence = new MemoryPersistence();
        connOpts = new MqttConnectOptions();
        connOpts.setUserName("rtshardware");
        connOpts.setPassword("rtshardware".toCharArray());

        mqttThread.start();

        mBuilder = new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.motor_controls)
            .setContentTitle("Mqtt Notification");

        resultIntent = new Intent(this, Mqtt.class);
        stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Mqtt.class);
        stackBuilder.addNextIntent(resultIntent);
        resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        backRun = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!mqttClient.isConnected()) {
            mqttThread.start();
        }
        backRun = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        backRun = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
                mqttClient.close();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        backRun = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mqtt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void connectionLost(Throwable throwable) {
//        Toast.makeText(Mqtt.this, "Connection Lost", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        JSONObject jsonMqttMessage = new JSONObject(mqttMessage.toString());
        mqttMessageTextView = jsonMqttMessage.getString("data") + " @ " + jsonMqttMessage.getString("date")  + "\n";
        runUi.post(uiRunnable);
        notificationHandler.post(notificationRunnable);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}