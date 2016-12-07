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
            timer.scheduleAtFixedRate(timerTask, 1, period);
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

        SharedPreferences settings = getSharedPreferences("msettings", 0);
        on_create_func();
        editTextUpdateRate.setText(String.valueOf(settings.getInt("UPDATERATE", 1)));
        if (!settings.getBoolean("SYNCSWITCH", false)) {
            editTextUpdateRate.setEnabled(true);
        } else if (settings.getBoolean("SYNCSWITCH", false)) {
            editTextUpdateRate.setEnabled(false);
        }
        sync.setChecked(settings.getBoolean("SYNCSWITCH", false));

        /*
         to fix app crash because of calling stopTransfer() before startTransfer();
         not a good fix. work on it later.
         */
        startTransfer(settings.getInt("UPDATERATE", 1000));
        stopTransfer();

        if (settings.getBoolean("SYNCSWITCH", false)) {
            startTransfer(settings.getInt("UPDATERATE", 1000));
        }

        sync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SharedPreferences settings = getSharedPreferences("msettings", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("UPDATERATE", Integer.parseInt(editTextUpdateRate.getText().toString()));
                editor.putBoolean("SYNCSWITCH", sync.isChecked());
                editor.commit();

                if (!settings.getBoolean("SYNCSWITCH", false)) {
                    stopTransfer();
                    editTextUpdateRate.setEnabled(true);
                } else if (settings.getBoolean("SYNCSWITCH", false)) {
                    startTransfer(settings.getInt("UPDATERATE", 1000));
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
