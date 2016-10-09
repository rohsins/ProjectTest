package com.rohsins.project_test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Mqtt extends Activity {

    TextView textView;
    StringBuilder stringBuilderMqtt = new StringBuilder("ME");

    public void mqttTestFunction2() {
        //String topic = "rtsmqtt/r&d/roomX";
        String topic = "rhome/test";
        String content = "rohsins from Research & Development test sequence 001022456691578";
        int qos = 2;
        String broker = "tcp://m2m.eclipse.org:1883";
        String clientId = "rtsHardware";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient hardwareClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
//                System.out.println("Connecting to broker: " + broker);
            stringBuilderMqtt.append("Connecting to broker: " + broker);
            hardwareClient.connect(connOpts);
//                System.out.println("Connected");
            stringBuilderMqtt.append("\n\r Connected");
//                System.out.println("topic: " + topic);
            stringBuilderMqtt.append("\n\r topic:" + topic);
//                System.out.println("Publishing message: " + content);
            stringBuilderMqtt.append("\n\r Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            hardwareClient.publish(topic, message);
//                System.out.println("Message published");
            stringBuilderMqtt.append("\n\r Message published");
            hardwareClient.disconnect();
//                System.out.println("Disconnected");
            stringBuilderMqtt.append("\n\rDisconnected");
            System.exit(0);
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);

        textView = (TextView) findViewById(R.id.mqttTextView01);

        textView.setText("hello this is test");
        mqttTestFunction2();
        textView.setText(stringBuilderMqtt.toString());

    }
/*
    public class MqttTest extends AsyncTask<Void, Void, Void> {

        //String topic = "rtsmqtt/r&d/roomX";
        String topic = "rhome/test";
        String content = "rohsins from Research & Development test sequence 001022456691578";
        int qos = 2;
        String broker = "tcp://m2m.eclipse.org:1883";
        String clientId = "rtsHardware";
        MemoryPersistence persistence = new MemoryPersistence();



        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                MqttClient hardwareClient = new MqttClient(broker, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
//                System.out.println("Connecting to broker: " + broker);
                stringBuilderMqtt.append("Connecting to broker: " + broker);
                hardwareClient.connect(connOpts);
//                System.out.println("Connected");
                stringBuilderMqtt.append("\n\r Connected");
//                System.out.println("topic: " + topic);
                stringBuilderMqtt.append("\n\r topic:" + topic);
//                System.out.println("Publishing message: " + content);
                stringBuilderMqtt.append("\n\r Publishing message: " + content);
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(qos);
                hardwareClient.publish(topic, message);
//                System.out.println("Message published");
                stringBuilderMqtt.append("\n\r Message published");
                hardwareClient.disconnect();
//                System.out.println("Disconnected");
                stringBuilderMqtt.append("\n\rDisconnected");
                System.exit(0);
            } catch (MqttException me) {
                me.printStackTrace();
            }
            return null;
        }
    }
    public void mqttTestFunction() {
        MqttTest mqttTestInstance = new MqttTest();
        mqttTestInstance.execute();
    }
    */
}
