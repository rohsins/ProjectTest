package com.rohsins.project_test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Mqtt extends Connectivity {

    TextView textView;

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
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mqttViewerOn = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        backRun = true;
        mqttViewerOn = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttViewerOn = false;
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
}