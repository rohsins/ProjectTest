package com.rohsins.project_test;

import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

public class Mqtt extends Connectivity implements MqttCallbackExtended {

    TextView textView;

    MqttClient mqttClient;
    String topic;
    int qos;
    String broker;
    String clientId;
    MemoryPersistence persistence;
    MqttConnectOptions connOpts;

    int mId = 0;
    boolean backRun = false;

    private static String mqttMessageTextView;
    private Handler runUi = new Handler();

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

    Thread mqttThread = new Thread(launchMqtt);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);

        on_create_func();

        textView = (TextView) findViewById(R.id.mqttTextView01);
        textView.setMovementMethod(new ScrollingMovementMethod());

        topic = "R&D/hardware/viewer";
        qos = 2;
        broker = "tcp://" + brokerAddress + ":1883";
        clientId = uniqueId;
        persistence = new MemoryPersistence();
        connOpts = new MqttConnectOptions();
        connOpts.setUserName("rtshardware");
        connOpts.setPassword("rtshardware".toCharArray());
        connOpts.setAutomaticReconnect(true);
        connOpts.setCleanSession(false);

        backRun = false;
        mqttThread.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        if (!mqttClient.isConnected()) {
//            mqttThread.start();
//        }
        backRun = false;
        globalNotificationManager.cancel(globalNotificationId);
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
    public void connectComplete(boolean b, String s) {
        if (b) {
            try {
                mqttClient.subscribe(topic, qos);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
//        Toast.makeText(Mqtt.this, "Connection Lost", Toast.LENGTH_SHORT).show();
//        Log.d("Lost", "connection lost " + throwable);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        JSONObject jsonMqttMessage = new JSONObject(mqttMessage.toString());
        mqttMessageTextView = jsonMqttMessage.getString("data") + " @ " + jsonMqttMessage.getString("date")  + "\n";
        globalNotificationMessage = mqttMessageTextView;
        runUi.post(uiRunnable);
        if (backRun) {
            globalNotificationHandler.post(globalNotificationRunnable);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}