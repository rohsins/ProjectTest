package com.rohsins.project_test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Connectivity extends Activity {

	public volatile String Address;
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

    public void on_create_func() {
        SharedPreferences settings = getSharedPreferences("msettings",0);
        if(settings.getString("SERVERIPADDRESS", "192,168.1.9:8080").contains(":")) {
            ipAddressPort = settings.getString("SERVERIPADDRESS", "192.168.1.9:8080").split(":");
            Address = ipAddressPort[0];
            Port = Integer.parseInt(ipAddressPort[1]);
        }
        else {
            Address = settings.getString("SERVERIPADDRESS", "192.168.1.9");
        }
        inputMqttBrokerIp = settings.getString("MQTTBROKERADDRESS", "m2m.eclipse.org");
		nagleFlag = settings.getBoolean("ENABLENAGLE", false);
		reuseAddressFlag = settings.getBoolean("ENABLEREUSEADDRESS", false);
        mqttFlag = settings.getBoolean("ENABLEMQTT", false);
    }

//	@Override
//	public void connectionLost(Throwable throwable) {
//		mqttViewerTextView.setText(throwable.toString());
//	}
//
//	@Override
//	public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
//		mqttViewerTextView.setText(mqttMessage.toString());
//	}
//
//	@Override
//	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//		mqttViewerTextView.setText(iMqttDeliveryToken.toString());
//	}

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
            topic = "R&D/hardware";
            qos = 1;
            broker = "tcp://" + inputMqttBrokerIp + ":1883";
            clientId = "rohsinsCellPhone";
            will = "rohsins's cell phone out".getBytes();
            retained = false;
            this.payload = payload;
            persistence = new MemoryPersistence();
            connOpts = new MqttConnectOptions();
            connOpts.setUserName("rtshardware");
            connOpts.setPassword("rtshardware".toCharArray());
            connOpts.setWill(topic, will, 1, retained);
            connOpts.setKeepAliveInterval(30000);

//            serialViewerTextView = (TextView) findViewById(R.id.serialViewerTextView01);
//            serialViewerTextView.setMovementMethod(new ScrollingMovementMethod());
		}

		MyClientTask(String addr, int port, String msgTo) {
			dstAddress = addr;
			dstPort = port;
			msgToServer = msgTo;

            serialViewerTextView = (TextView) findViewById(R.id.serialViewerTextView01);
            serialViewerTextView.setMovementMethod(new ScrollingMovementMethod());
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
//                    socket.setSoTimeout(100);
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
		if (mqttFlag == false) {
            myClientTask = new MyClientTask(Address, Port, tMsg);
//        Toast.makeText(Connectivity.this, String.valueOf(nagleReplyFlag), Toast.LENGTH_SHORT).show();
        } else if (mqttFlag == true) {
            myClientTask = new MyClientTask(tMsg.getBytes());
        }
		myClientTask.execute();
	}
}

