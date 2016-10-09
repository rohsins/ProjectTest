package com.rohsins.project_test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class socket extends Activity {

	public volatile String Address;
	public volatile int Port;
    String ipAddressPort[];
    String inputIpAddressPort;
    TextView serialViewerTextView;
	public static boolean nagleFlag = false;
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
		nagleFlag = settings.getBoolean("ENABLENAGLE", false);
		reuseAddressFlag = settings.getBoolean("ENABLEREUSEADDRESS", false);
    }
		
	public class MyClientTask extends AsyncTask<Void, Void, Void> {

		String dstAddress;
		int dstPort;
		String response = "";
		String msgToServer;

		MyClientTask(String addr, int port, String msgTo) {
			dstAddress = addr;
			dstPort = port;
			msgToServer = msgTo;

            serialViewerTextView = (TextView) findViewById(R.id.serialViewerTextView01);
		}

		@Override
		protected Void doInBackground(Void... arg0) {

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
				socket.setSoTimeout(100);
//				nagleReplyFlag = socket.getTcpNoDelay();
				dataOutputStream = new DataOutputStream(socket.getOutputStream());
				dataInputStream = new DataInputStream(socket.getInputStream());
				
				if(msgToServer != null){
					dataOutputStream.writeUTF(msgToServer);
				}
				
				response = dataInputStream.readUTF();
//                response = msgToServer;

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
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
//			editText.setText(response);
//            if(!response.equals("IOException: java.io.EOFException")) {
//                Toast.makeText(socket.this, response, Toast.LENGTH_SHORT).show();
//            }

			String switchCheck[] = null;
			switchCheck = response.split("-@=");
			switch (switchCheck[0]) {
				case "0xA0":
					serialViewerTextView.setText(switchCheck[1]);
					break;
                case "IOException: ":
                    if(switchCheck[1].equals("java.io.EOFException")) {
                        break;
                    }
					if (switchCheck[1].equals("java.net.SocketTimeoutException")) break;
                    Toast.makeText(socket.this, "Error: " + switchCheck[1], Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(socket.this, switchCheck[1], Toast.LENGTH_SHORT).show();
                    break;
			}
			super.onPostExecute(result);
		}
	}

	public void exchangeData(String tMsg) {
		MyClientTask myClientTask = new MyClientTask(Address, Port, tMsg);
//        Toast.makeText(socket.this, String.valueOf(nagleReplyFlag), Toast.LENGTH_SHORT).show();
		myClientTask.execute();
	}
}

