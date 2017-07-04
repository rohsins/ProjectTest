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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;


public class Chat extends Connectivity {

    EditText chatEditText;
    TextView chatTextView;
    Button chatSendButton;

    byte[] payloadChat;

    private static String mqttMessageTextViewChat;

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
                AlwaysRunner.globalMqttClient.publish(AlwaysRunner.globalChatTopic, payloadChat, AlwaysRunner.globalQos, AlwaysRunner.globalMqttRetained);
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

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mqttViewerOn = false;
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void sendButton(View view) {
//        chatTextView.setGravity(Gravity.END);
//        chatTextView.append(chatEditText.getText()+"\n");
        payloadChat = (AlwaysRunner.globalUniqueId +": "+chatEditText.getText().toString() + "\n").getBytes();
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


//    @Override
//    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
//        mqttMessageTextViewChat = mqttMessage;
////        stringBuilderMqtt.append(mqttMessage.toString());
//        runUiChat.post(uiRunnableChat);
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AlwaysRunner.MessageEvent event) {
        try {
            JSONObject jsonMqttMessage = new JSONObject(event.getMessageValue());
            mqttMessageTextViewChat = jsonMqttMessage.getString("payload") + "\n";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        runUiChat.post(uiRunnableChat);
    }
}
