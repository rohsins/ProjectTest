package com.rohsins.project_test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

public class Mqtt extends Connectivity {

    TextView textView;
    public static boolean mqttViewerOn = false;

    private static String mqttMessageTextView;
    private Handler runUi = new Handler();

    Runnable uiRunnable = new Runnable() {
        @Override
        public void run() {
            textView.append(mqttMessageTextView.toString());

            int totalLines = ((textView.getHeight()) / textView.getLineHeight());
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

        mqttViewerOn = true;
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mqttViewerOn = true;
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mqttViewerOn = false;
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttViewerOn = false;
        EventBus.getDefault().unregister(this);
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
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AlwaysRunner.MessageEvent event) {
        if (event.getMessageTopic().contains("RTSR&D/rozbor/sub/")) {
            mqttMessageTextView = event.getMessageData();
            runUi.post(uiRunnable);
        }
    }
}