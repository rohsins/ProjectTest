package com.rohsins.project_test;

import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Mqtt extends Connectivity implements MqttCallback {

    TextView textView;

    MqttClient mqttClient;
    String topic;
    int qos;
    String broker;
    String clientId;
    MemoryPersistence persistence;
    MqttConnectOptions connOpts;

    private static MqttMessage mqttMessageTextView;

    private static boolean closeFlag = false;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);

        on_create_func();

        textView = (TextView) findViewById(R.id.mqttTextView01);
        textView.setMovementMethod(new ScrollingMovementMethod());

        topic = "R&D/hardware/viewer";
        qos = 1;
        broker = "tcp://" + brokerAddress + ":1883";
        clientId = "rohsins's cell phone mqtt viewer";
        persistence = new MemoryPersistence();
        connOpts = new MqttConnectOptions();
        connOpts.setUserName("rtshardware");
        connOpts.setPassword("rtshardware".toCharArray());

        try {
            closeFlag = false;
            mqttClient = new MqttClient(broker, clientId, persistence);
            mqttClient.connect(connOpts);
            mqttClient.setCallback(this);
            mqttClient.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (closeFlag == true) {
            try {
                closeFlag = false;
                mqttClient = new MqttClient(broker, clientId, persistence);
                mqttClient.connect(connOpts);
                mqttClient.setCallback(this);
                mqttClient.subscribe(topic, qos);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (closeFlag == true) {
            try {
                closeFlag = false;
                mqttClient = new MqttClient(broker, clientId, persistence);
                mqttClient.connect(connOpts);
                mqttClient.setCallback(this);
                mqttClient.subscribe(topic, qos);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (closeFlag == true) {
            try {
                closeFlag = false;
                mqttClient = new MqttClient(broker, clientId, persistence);
                mqttClient.connect(connOpts);
                mqttClient.setCallback(this);
                mqttClient.subscribe(topic, qos);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (closeFlag == false) {
            try {
                closeFlag = true;
                mqttClient.disconnect();
                mqttClient.close();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (closeFlag == false) {
            try {
                closeFlag = true;
                mqttClient.disconnect();
                mqttClient.close();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (closeFlag == false) {
            try {
                closeFlag = true;
                mqttClient.disconnect();
                mqttClient.close();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
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

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        mqttMessageTextView = mqttMessage;
//        stringBuilderMqtt.append(mqttMessage.toString());
        runUi.post(uiRunnable);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}