package com.rohsins.project_test;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connectivity extends Activity {

    public static boolean initializeChecker = false;

    public static String uniqueId;
	public static String Address;
    public static String brokerAddress;
	public static int Port;
    static String ipAddressPort[];
    static String inputIpAddressPort;
    static String inputMqttBrokerIp;
    TextView serialViewerTextView;
	public static boolean nagleFlag = false;
	public static boolean mqttFlag = false;
//	public static boolean nagleReplyFlag = true;
	public static boolean reuseAddressFlag = false;

	public boolean memWriteFlag = false;

	public static SharedPreferences settings;
	public static SharedPreferences.Editor editor;

    private static MqttClient mqttClient;

    String dstAddress;
    int dstPort;
    String response = "";
    String msgToServer;

    static String topic;
    static int qos;
    static String broker;
    static String clientId;
    static MemoryPersistence persistence;
    static MqttConnectOptions connOpts;
    static Boolean retained;
//    byte[] payload;
//    byte[] will;

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

            brokerAddress = settings.getString("MQTTBROKERADDRESS", "hardware.wscada.net");
            nagleFlag = settings.getBoolean("ENABLENAGLE", false);
            reuseAddressFlag = settings.getBoolean("ENABLEREUSEADDRESS", false);
            mqttFlag = settings.getBoolean("ENABLEMQTT", false);
            uniqueId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);


            topic = "RTSR&D/baanvak/sub/rhome";
            qos = 0;
            broker = "tcp://" + brokerAddress + ":1883";
            clientId = "rohsins" + "c" + uniqueId;
//            will = "rohsins's cell phone out".getBytes();
            retained = false;
            persistence = new MemoryPersistence();
            connOpts = new MqttConnectOptions();
            connOpts.setUserName("rohsins");
            connOpts.setPassword("escapecharacters".toCharArray());
            connOpts.setCleanSession(true);
        }
    }

    public static void mqttConnect() {
        mConnection mConnect = new mConnection(true);
        new Thread(mConnect).start();
    }

    public static void mqttDisconnet() {
        mConnection mDisconnect = new mConnection(false);
        new Thread(mDisconnect).start();
    }

    public static class mConnection implements Runnable {
        boolean connect;

        mConnection(boolean connect) {
            this.connect = connect;
        }

        public void run() {
            try {
                if (connect && mqttClient == null) {
                    mqttClient = new MqttClient(broker, clientId, persistence);
                    mqttClient.connect(connOpts);
                } else if (!connect && mqttClient != null) {
                    mqttClient.disconnect();
                    mqttClient.close();
                    mqttClient = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {
        byte[] payload;

		MyClientTask(byte[] payload) {
            this.payload = payload;
		}

		MyClientTask(String addr, int port, String msgTo) {
			dstAddress = addr;
			dstPort = port;
			msgToServer = msgTo;

            serialViewerTextView = findViewById(R.id.serialViewerTextView01);
		}

		@Override
		protected Void doInBackground(Void... arg0) {

            if (!mqttFlag) {
                Socket socket = null;
                DataOutputStream dataOutputStream = null;
                DataInputStream dataInputStream = null;
			    try {
                    socket = new Socket(dstAddress, dstPort);
                    if (!nagleFlag) {
                        socket.setTcpNoDelay(true);
                    } else if (nagleFlag) {
                        socket.setTcpNoDelay(false);
                    }
                    if (!reuseAddressFlag) {
                        socket.setReuseAddress(false);
                    } else if (reuseAddressFlag) {
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
            } else if (mqttFlag && mqttClient != null) {
                try {
                    mqttClient.publish(topic, payload, qos, retained);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
            if (!mqttFlag) {
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
                        if (switchCheck[1].equals("java.net.SocketTimeoutException")) break;
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
		if (!mqttFlag) {
            myClientTask = new MyClientTask(Address, Port, tMsg);
//        Toast.makeText(Connectivity.this, String.valueOf(nagleReplyFlag), Toast.LENGTH_SHORT).show();
        } else if (mqttFlag) {
            myClientTask = new MyClientTask(tMsg.getBytes());
        }
		myClientTask.execute();
	}
}
