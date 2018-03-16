package com.rohsins.project_test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Motor_Controls extends Connectivity {

	static private boolean firstStart = true;
	
	static SeekBar motor_controlsSeekBar1;
	static TextView motor_controlsTextView;
	static private int motor_controlsSeekBar1_value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_motor_controls);
		
		motor_controlsSeekBar1 = (SeekBar)findViewById(R.id.motor_controlsSeekBar1);
		motor_controlsTextView = (TextView)findViewById(R.id.motor_controlsTextView2);

		on_create_func();

		if (firstStart) {
			motor_controlsSeekBar1_value = settings.getInt("MOTORCONTROLSSEEKBAR1VALUE", 20);
			firstStart = false;
		}

		motor_controlsSeekBar1.setProgress(motor_controlsSeekBar1_value);
		motor_controlsTextView.setText(String.valueOf(motor_controlsSeekBar1_value));
		motor_controlsSeekBar1.setOnSeekBarChangeListener(motor_controlsSeekBar1Listener);
		
	}
	
	public OnSeekBarChangeListener motor_controlsSeekBar1Listener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			motor_controlsSeekBar1_value = motor_controlsSeekBar1.getProgress();
			motor_controlsTextView.setText(String.valueOf(motor_controlsSeekBar1_value));
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			memWriteFlag = true;
			exchangeData("CURTAIN:" + motor_controlsSeekBar1_value);
		}
		
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.motor_controls, menu);
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
	protected void onDestroy() {
		super.onDestroy();
		editor.putInt("MOTORCONTROLSSEEKBAR1VALUE", motor_controlsSeekBar1_value);
	}
}
