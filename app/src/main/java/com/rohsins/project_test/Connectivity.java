package com.rohsins.project_test;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connectivity extends Activity implements MqttCallbackExtended {

    public boolean initializeChecker = false;

    public static String uniqueId;
	public volatile String Address;
    public volatile String brokerAddress;
	public volatile int Port;
    String ipAddressPort[];
    String inputIpAddressPort;
    String inputMqttBrokerIp;
    TextView serialViewerTextView;
//	TextView mqttViewerTextView;
	public static boolean nagleFlag = false;
	public static boolean mqttFlag = false;
//	public static boolean nagleReplyFlag = true;
	public static boolean reuseAddressFlag = false;

    public static MqttClient globalMqttClient;
    public static String globalPublishTopic;
    public static String globalSubscribeTopic;
    public static int globalQos;
    public static String globalBrokerAddress;
    public static String globalClientId;
    public static MemoryPersistence globalPersistence;
    public static MqttConnectOptions globalConnectOptions;
    public static Boolean globalMqttRetained;
    public static byte[] globalMqttPayload;

    NotificationCompat.Builder globalNotificationBuilder;
    NotificationManager globalNotificationManager;
    public static int globalNotificationId = 0;
    public static String globalNotificationMessage;

    Handler globalMqttSendHandler = new Handler();
    Handler globalNotificationHandler = new Handler();

    Runnable globalMqttSend = new Runnable() {
        @Override
        public void run() {
            try {
                globalMqttClient.publish(globalPublishTopic, globalMqttPayload, globalQos, globalMqttRetained);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Runnable globalNotificationRunnable = new Runnable() {
        @Override
        public void run() {
            globalNotificationBuilder.setContentText(globalNotificationMessage);
            globalNotificationBuilder.setShowWhen(true);
            globalNotificationManager.notify(globalNotificationId, globalNotificationBuilder.build());
        }
    };


    Runnable globalMqttLaunch = new Runnable() {
        @Override
        public void run() {
            try {
                globalMqttClient = new MqttClient(globalBrokerAddress, globalClientId, globalPersistence);
                globalMqttClient.connect(globalConnectOptions);
                globalMqttClient.setCallback(Connectivity.this);
                globalMqttClient.subscribe(globalSubscribeTopic, globalQos);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    };

    Thread globalMqttLaunchThread = new Thread(globalMqttLaunch);

    public void on_create_func() {
        if (!initializeChecker) {
            initializeChecker = true;
            SharedPreferences settings = getSharedPreferences("msettings", 0);
            if (settings.getString("SERVERIPADDRESS", "192,168.1.9:8080").contains(":")) {
                ipAddressPort = settings.getString("SERVERIPADDRESS", "192.168.1.9:8080").split(":");
                Address = ipAddressPort[0];
                Port = Integer.parseInt(ipAddressPort[1]);
            } else {
                Address = settings.getString("SERVERIPADDRESS", "192.168.1.9");
            }
            brokerAddress = settings.getString("MQTTBROKERADDRESS", "m2m.eclipse.org");
            nagleFlag = settings.getBoolean("ENABLENAGLE", false);
            reuseAddressFlag = settings.getBoolean("ENABLEREUSEADDRESS", false);
            mqttFlag = settings.getBoolean("ENABLEMQTT", false);
            uniqueId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

            globalBrokerAddress = "tcp://" + brokerAddress + ":1883";
            globalPublishTopic = "RTSR&D/rozbor/pub";
            globalSubscribeTopic = "RTSR&D/rozbor/sub/" + uniqueId;
            globalQos = 2;
            globalClientId = "rohsins";
            globalPersistence = new MemoryPersistence();
            globalConnectOptions = new MqttConnectOptions();
            globalConnectOptions.setAutomaticReconnect(true);
            globalConnectOptions.setCleanSession(false);
            globalConnectOptions.setUserName("rtshardware");
            globalConnectOptions.setPassword(("rtshardware").toCharArray());

            globalNotificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.motor_controls)
                    .setContentTitle("Mqtt Notification")
                    .setLights(Color.YELLOW, 1000, 3000)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS);

            globalNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            globalMqttLaunchThread.start();
        }
    }

    @Override
    public void connectComplete(boolean b, String s) {
        if (b) {
            try {
                globalMqttClient.subscribe(globalSubscribeTopic, globalQos);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        Log.d("Runner", mqttMessage.toString());
        JSONObject jsonMqttMessage = new JSONObject(mqttMessage.toString());
        globalNotificationMessage = jsonMqttMessage.getString("payload") + " @ " + jsonMqttMessage.getString("date")  + "\n";
        globalNotificationHandler.post(globalNotificationRunnable);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

		String dstAddress;
		int dstPort;
		String response = "";
		String msgToServer;

		String topic;
		int qos;
		String broker;
		String clientId;
		MemoryPersistence persistence;
		MqttConnectOptions connOpts;
        Boolean retained;
        byte[] payload;
        byte[] will;

		MyClientTask(byte[] payload) {
            topic = "R&D/hardware/home";
            qos = 2;
            broker = "tcp://" + brokerAddress + ":1883";
            clientId = uniqueId;
//            will = "rohsins's cell phone out".getBytes();
            retained = false;
            this.payload = payload;
            persistence = new MemoryPersistence();
            connOpts = new MqttConnectOptions();
            connOpts.setUserName("rtshardware");
            connOpts.setPassword("rtshardware".toCharArray());
//            connOpts.setWill(topic, will, 1, retained);
//            connOpts.setKeepAliveInterval(30000);

//            serialViewerTextView = (TextView) findViewById(R.id.serialViewerTextView01);
		}

		MyClientTask(String addr, int port, String msgTo) {
			dstAddress = addr;
			dstPort = port;
			msgToServer = msgTo;

            serialViewerTextView = (TextView) findViewById(R.id.serialViewerTextView01);
		}

		@Override
		protected Void doInBackground(Void... arg0) {

            if (mqttFlag == false) {
                Socket socket = null;
                DataOutputStream dataOutputStream = null;
                DataInputStream dataInputStream = null;
			    try {
                    socket = new Socket(dstAddress, dstPort);
                    if (nagleFlag == false) {
                        socket.setTcpNoDelay(true);
                    } else if (nagleFlag == true) {
                        socket.setTcpNoDelay(false);
                    }
                    if (reuseAddressFlag == false) {
                        socket.setReuseAddress(false);
                    } else if (reuseAddressFlag == true) {
                        socket.setReuseAddress(true);
                    }
                    socket.setSoTimeout(200);
//				      nagleReplyFlag = Connectivity.getTcpNoDelay();
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());

                    if (msgToServer != null) {
                        dataOutputStream.writeUTF(msgToServer);
                    }

                    response = dataInputStream.readUTF();
    //                  response = msgToServer;

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    response = "UnknownHostException: -@=" + e.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                    response = "IOException: -@=" + e.toString();
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (dataOutputStream != null) {
                        try {
                            dataOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (dataInputStream != null) {
                        try {
                            dataInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (mqttFlag == true) {
                try {
                    MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
                    mqttClient.connect(connOpts);
                    mqttClient.publish(topic, payload, qos, retained);
                    mqttClient.disconnect();
                    mqttClient.close();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
            if (mqttFlag == false) {
//                editText.setText(response);
//                if(!response.equals("IOException: java.io.EOFException")) {
//                    Toast.makeText(Connectivity.this, response, Toast.LENGTH_SHORT).show();
//                }

                String switchCheck[] = null;
                switchCheck = response.split("-@=");
                switch (switchCheck[0]) {
                    case "0xA0":
//                        serialViewerTextView.setText(switchCheck[1]);

                        serialViewerTextView.setMovementMethod(new ScrollingMovementMethod());
                        serialViewerTextView.append(switchCheck[1]);

                        int totalLines = ((serialViewerTextView.getHeight())/serialViewerTextView.getLineHeight());
                        if (serialViewerTextView.getLineCount() >= totalLines) {
                            serialViewerTextView.setScrollY((serialViewerTextView.getLineCount() - totalLines) * serialViewerTextView.getLineHeight());
                            if (serialViewerTextView.getLineCount() > 20000) {
                                serialViewerTextView.setText("");
                            }
                        }
                        break;
                    case "IOException: ":
                        if (switchCheck[1].equals("java.io.EOFException")) {
                            break;
                        }
                        if (switchCheck[1].equals("java.net.SocketTimeoutException: Read timed out")) break;
                        Toast.makeText(Connectivity.this, "Error: " + switchCheck[1], Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(Connectivity.this, switchCheck[1], Toast.LENGTH_SHORT).show();
                        break;
                }
                super.onPostExecute(result);
            }
        }
	}

	public void exchangeData(String tMsg) {
        MyClientTask myClientTask = null;
		if (mqttFlag == false) {
            myClientTask = new MyClientTask(Address, Port, tMsg);
//        Toast.makeText(Connectivity.this, String.valueOf(nagleReplyFlag), Toast.LENGTH_SHORT).show();
        } else if (mqttFlag == true) {
            myClientTask = new MyClientTask(tMsg.getBytes());
        }
		myClientTask.execute();
	}
}
