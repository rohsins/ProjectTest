package com.rohsins.project_test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class SerialViewer extends Connectivity {

    TextView serialViewerTextView;
    Switch sync;
    EditText editTextUpdateRate;

    private int editTextUpdateRate_value;
    private  boolean sync_value;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            exchangeData("0xFD");
        }
    };

    Handler handler = new Handler();
    TimerTask timerTask;
    Timer timer;

    public void startTransfer(int period) {
        try {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        handler.post(runnable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            timer = new Timer("updateTimer");
            timer.scheduleAtFixedRate(timerTask, 1000, period);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopTransfer() {
        timerTask.cancel();
        timer.purge();
        timer.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_viewer);

        serialViewerTextView = (TextView) findViewById(R.id.serialViewerTextView01);
        editTextUpdateRate = (EditText) findViewById(R.id.editTextUpdateRate);
        sync = (Switch) findViewById(R.id.syncSwitch);

        on_create_func();

        editTextUpdateRate_value = settings.getInt("UPDATERATE", 1000);
        sync_value = settings.getBoolean("SYNCSWITCH", false);

        editTextUpdateRate.setText(String.valueOf(editTextUpdateRate_value));

        if (!sync_value) {
            editTextUpdateRate.setEnabled(true);
        } else if (sync_value) {
            editTextUpdateRate.setEnabled(false);
        }
        sync.setChecked(sync_value);

        /*
         to fix app crash because of calling stopTransfer() before startTransfer();
         not a good fix. work on it later.
         */
        startTransfer(editTextUpdateRate_value);
        stopTransfer();

        if (sync_value) {
            startTransfer(editTextUpdateRate_value);
        }

        sync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                memWriteFlag = true;
                sync_value = sync.isChecked();
                editTextUpdateRate_value = Integer.parseInt(editTextUpdateRate.getText().toString());

                if (!sync_value) {
                    stopTransfer();
                    editTextUpdateRate.setEnabled(true);
                } else if (sync_value) {
                    startTransfer(editTextUpdateRate_value);
                    editTextUpdateRate.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTransfer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTransfer();

        editor.putInt("UPDATERATE", editTextUpdateRate_value);
        editor.putBoolean("SYNCSWITCH", sync_value);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_serial_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
