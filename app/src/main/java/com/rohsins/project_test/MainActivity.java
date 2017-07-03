package com.rohsins.project_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends Connectivity {

    public void Lights(View view) {
        Intent intent = new Intent(this, Lights.class);
        startActivity(intent);
    }

    public void Doors(View view) {
        Intent intent = new Intent(this, Doors.class);
        startActivity(intent);
    }

    public void Televisions(View view) {
        Intent intent = new Intent(this, Televisions.class);
        startActivity(intent);
    }

    public void Motor_Controls(View view) {
        Intent intent = new Intent(this, Motor_Controls.class);
        startActivity(intent);
    }

    public void Others(View view) {
        Intent intent = new Intent(this, Others.class);
        startActivity(intent);
    }

    public void Chat(View view) {
        Intent intent = new Intent(this, Chat.class);
        startActivity(intent);
    }

    public void SerialViewer(View view) {
        Intent intent = new Intent(this, SerialViewer.class);
        startActivity(intent);
    }

    public void Mqtt(View view) {
        Intent intent = new Intent(this, Mqtt.class);
        startActivity(intent);
    }

    public void Settings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        on_create_func();

        Intent intent = new Intent(this, AlwaysRunner.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, AlwaysRunner.class);
        stopService(intent);
//        if (globalMqttClient.isConnected()) {
//            try {
//                globalMqttClient.disconnect();
//                globalMqttClient.close();
//            } catch (MqttException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
