package com.rohsins.project_test;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class Chat extends Connectivity implements MqttCallback {

    EditText chatEditText;
    TextView chatTextView;
    Button chatSendButton;

    MqttClient mqttClientChat;
    String topicChat;
    int qosChat;
    String brokerChat;
    String clientIdChat;
    MemoryPersistence persistenceChat;
    MqttConnectOptions connOptsChat;
    Boolean retainedChat;
    byte[] payloadChat;

    private static MqttMessage mqttMessageTextViewChat;
    private static boolean closeFlag = false;

    private Handler runUiChat = new Handler();
    private Handler runUiChatSend = new Handler();

    Runnable uiRunnableChat = new Runnable() {
        @Override
        public void run() {
            chatTextView.setGravity(Gravity.START);
            chatTextView.append(mqttMessageTextViewChat.toString());

            int totalLines = ((chatTextView.getHeight())/chatTextView.getLineHeight());
            if (chatTextView.getLineCount() >= totalLines) {
                chatTextView.setScrollY((chatTextView.getLineCount() - totalLines) * chatTextView.getLineHeight());
                if (chatTextView.getLineCount() > 20000) {
                    chatTextView.setText("");
                }
            }
        }
    };

    Runnable uiRunnableChatSend = new Runnable() {
        @Override
        public void run() {
            try {
                mqttClientChat.publish(topicChat, payloadChat, qosChat, retainedChat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatEditText = (EditText) findViewById(R.id.chatEditText);
        chatTextView = (TextView) findViewById(R.id.chatTextView2);
        chatSendButton = (Button) findViewById(R.id.chatButton);

        on_create_func();

        chatTextView.setMovementMethod(new ScrollingMovementMethod());
        chatEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (chatEditText.getText().toString().isEmpty()) {
                    chatSendButton.setEnabled(false);
                } else {
                    chatSendButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        chatEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        });

        topicChat = "R&D/hardware/chat";
        qosChat = 1;
        brokerChat = "tcp://" + brokerAddress + ":1883";
        clientIdChat = uniqueId;
        persistenceChat = new MemoryPersistence();
        retainedChat = false;
        connOptsChat = new MqttConnectOptions();
        connOptsChat.setUserName("rtshardware");
        connOptsChat.setPassword("rtshardware".toCharArray());

        try {
            mqttClientChat = new MqttClient(brokerChat, clientIdChat, persistenceChat);
            mqttClientChat.connect(connOptsChat);
            mqttClientChat.setCallback(this);
            mqttClientChat.subscribe(topicChat, qosChat);
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
                mqttClientChat = new MqttClient(brokerChat, clientIdChat, persistenceChat);
                mqttClientChat.connect(connOptsChat);
                mqttClientChat.setCallback(this);
                mqttClientChat.subscribe(topicChat, qosChat);
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
                mqttClientChat = new MqttClient(brokerChat, clientIdChat, persistenceChat);
                mqttClientChat.connect(connOptsChat);
                mqttClientChat.setCallback(this);
                mqttClientChat.subscribe(topicChat, qosChat);
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
                mqttClientChat = new MqttClient(brokerChat, clientIdChat, persistenceChat);
                mqttClientChat.connect(connOptsChat);
                mqttClientChat.setCallback(this);
                mqttClientChat.subscribe(topicChat, qosChat);
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
                mqttClientChat.disconnect();
                mqttClientChat.close();
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
                mqttClientChat.disconnect();
                mqttClientChat.close();
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
                mqttClientChat.disconnect();
                mqttClientChat.close();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendButton(View view) {
//        chatTextView.setGravity(Gravity.END);
//        chatTextView.append(chatEditText.getText()+"\n");
        payloadChat = (clientIdChat+": "+chatEditText.getText().toString() + "\n").getBytes();
        chatEditText.setText("");
        runUiChatSend.post(uiRunnableChatSend);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        mqttMessageTextViewChat = mqttMessage;
//        stringBuilderMqtt.append(mqttMessage.toString());
        runUiChat.post(uiRunnableChat);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
