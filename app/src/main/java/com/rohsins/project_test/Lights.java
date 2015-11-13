package com.rohsins.project_test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;

public class Lights extends socket {
	
	SeekBar lightSeekBar1;
	SeekBar lightSeekBar2;
	SeekBar lightSeekBar3;
	SeekBar lightSeekBar4;
	SeekBar lightSeekBar5;
	private int lightSeekBar_value;
	
	Switch lightSwitch1;
	Switch lightSwitch2;
	Switch lightSwitch3;
	Switch lightSwitch4;
	Switch lightSwitch5;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lights);
		
		lightSeekBar1 = (SeekBar) findViewById(R.id.lightSeekBar1);
		lightSeekBar2 = (SeekBar) findViewById(R.id.lightSeekBar2);
		lightSeekBar3 = (SeekBar) findViewById(R.id.lightSeekBar3);
		lightSeekBar4 = (SeekBar) findViewById(R.id.lightSeekBar4);
		lightSeekBar5 = (SeekBar) findViewById(R.id.lightSeekBar5);
		
		lightSwitch1 = (Switch) findViewById(R.id.lightSwitch1);
		lightSwitch2 = (Switch) findViewById(R.id.lightSwitch2);
		lightSwitch3 = (Switch) findViewById(R.id.lightSwitch3);
		lightSwitch4 = (Switch) findViewById(R.id.lightSwitch4);
		lightSwitch5 = (Switch) findViewById(R.id.lightSwitch5);
		
		SharedPreferences settings = getSharedPreferences("msettings",0);
//		Address = settings.getString("SERVERIPADDRESS", "192.168.1.9");
		on_create_func();
		
		lightSeekBar1.setEnabled(settings.getBoolean("LIGHTSWITCH1", false));
		lightSeekBar2.setEnabled(settings.getBoolean("LIGHTSWITCH2", false));
		lightSeekBar3.setEnabled(settings.getBoolean("LIGHTSWITCH3", false));
		lightSeekBar4.setEnabled(settings.getBoolean("LIGHTSWITCH4", false));
		lightSeekBar5.setEnabled(settings.getBoolean("LIGHTSWITCH5", false));
		
		lightSeekBar_value = settings.getInt("LIGHTSEEKBAR1", 20);
		lightSeekBar1.setProgress(lightSeekBar_value);
		lightSeekBar_value = settings.getInt("LIGHTSEEKBAR2", 20);
		lightSeekBar2.setProgress(lightSeekBar_value);
		lightSeekBar_value = settings.getInt("LIGHTSEEKBAR3", 20);
		lightSeekBar3.setProgress(lightSeekBar_value);
		lightSeekBar_value = settings.getInt("LIGHTSEEKBAR4", 20);
		lightSeekBar4.setProgress(lightSeekBar_value);
		lightSeekBar_value = settings.getInt("LIGHTSEEKBAR5", 20);
		lightSeekBar5.setProgress(lightSeekBar_value);
		
		lightSwitch1.setChecked(settings.getBoolean("LIGHTSWITCH1", false));
		lightSwitch2.setChecked(settings.getBoolean("LIGHTSWITCH2", false));
		lightSwitch3.setChecked(settings.getBoolean("LIGHTSWITCH3", false));
		lightSwitch4.setChecked(settings.getBoolean("LIGHTSWITCH4", false));
		lightSwitch5.setChecked(settings.getBoolean("LIGHTSWITCH5", false));
		
		lightSeekBar1.setOnSeekBarChangeListener(lightSeekBar1Listener);
		lightSeekBar2.setOnSeekBarChangeListener(lightSeekBar2Listener);
		lightSeekBar3.setOnSeekBarChangeListener(lightSeekBar3Listener);
		lightSeekBar4.setOnSeekBarChangeListener(lightSeekBar4Listener);
		lightSeekBar5.setOnSeekBarChangeListener(lightSeekBar5Listener);
		
		lightSwitch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				SharedPreferences settings = getSharedPreferences("msettings",0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("LIGHTSWITCH1", lightSwitch1.isChecked());
				editor.commit();
				lightSeekBar1.setEnabled(settings.getBoolean("LIGHTSWITCH1", false));
				if(!settings.getBoolean("LIGHTSWITCH1", true)) {
					exchangeData("LIGHTONE:" + String.valueOf(0));
				}
				else if(settings.getBoolean("LIGHTSWITCH1", true)) {
					exchangeData("LIGHTONE:" + settings.getInt("LIGHTSEEKBAR1", 20));
				}
			}
			
		});
		
		lightSwitch2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				SharedPreferences settings = getSharedPreferences("msettings",0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("LIGHTSWITCH2", lightSwitch2.isChecked());
				editor.commit();
				lightSeekBar2.setEnabled(settings.getBoolean("LIGHTSWITCH2", false));
				if(!settings.getBoolean("LIGHTSWITCH2", true)) {
					exchangeData("LIGHTTWO:" + String.valueOf(0));
				}
				else if(settings.getBoolean("LIGHTSWITCH2", true)) {
					exchangeData("LIGHTTWO:" + settings.getInt("LIGHTSEEKBAR2", 20));
				}
			}
			
		});
		
		lightSwitch3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				SharedPreferences settings = getSharedPreferences("msettings",0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("LIGHTSWITCH3", lightSwitch3.isChecked());
				editor.commit();
				lightSeekBar3.setEnabled(settings.getBoolean("LIGHTSWITCH3", false));
				if(!settings.getBoolean("LIGHTSWITCH3", true)) {
					exchangeData("LIGHTTHREE:" + String.valueOf(0));
				}
				else if(settings.getBoolean("LIGHTSWITCH3", true)) {
					exchangeData("LIGHTTHREE:" + settings.getInt("LIGHTSEEKBAR3", 20));
				}
			}
			
		});
		
		lightSwitch4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				SharedPreferences settings = getSharedPreferences("msettings",0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("LIGHTSWITCH4", lightSwitch4.isChecked());
				editor.commit();
				lightSeekBar4.setEnabled(settings.getBoolean("LIGHTSWITCH4", false));
				if(!settings.getBoolean("LIGHTSWITCH4", true)) {
					exchangeData("LIGHTFOUR:" + String.valueOf(0));
				}
				else if(settings.getBoolean("LIGHTSWITCH4", true)) {
					exchangeData("LIGHTFOUR:" + settings.getInt("LIGHTSEEKBAR4", 20));
				}
			}
			
		});
		
		lightSwitch5.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				SharedPreferences settings = getSharedPreferences("msettings",0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("LIGHTSWITCH5", lightSwitch5.isChecked());
				editor.commit();
				lightSeekBar5.setEnabled(settings.getBoolean("LIGHTSWITCH5", false));
				if(!settings.getBoolean("LIGHTSWITCH5", true)) {
					exchangeData("LIGHTFIVE:" + String.valueOf(0));
				}
				else if(settings.getBoolean("LIGHTSWITCH5", true)) {
					exchangeData("LIGHTFIVE:" + settings.getInt("LIGHTSEEKBAR5", 20));
				}
			}
			
		});
		
	}

	OnSeekBarChangeListener lightSeekBar1Listener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			// TODO Auto-generated method stub
			
			SharedPreferences settings = getSharedPreferences("msettings",0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("LIGHTSEEKBAR1", lightSeekBar1.getProgress());
			editor.commit();
			
			exchangeData("LIGHTONE:" + String.valueOf(lightSeekBar1.getProgress()));
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	private OnSeekBarChangeListener lightSeekBar2Listener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			SharedPreferences settings = getSharedPreferences("msettings",0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("LIGHTSEEKBAR2", lightSeekBar2.getProgress());
			editor.commit();
			
			exchangeData("LIGHTTWO:" + String.valueOf(lightSeekBar2.getProgress()));
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	private OnSeekBarChangeListener lightSeekBar3Listener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			SharedPreferences settings = getSharedPreferences("msettings",0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("LIGHTSEEKBAR3", lightSeekBar3.getProgress());
			editor.commit();
			
			exchangeData("LIGHTTHREE:" + String.valueOf(lightSeekBar3.getProgress()));
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	private OnSeekBarChangeListener lightSeekBar4Listener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			SharedPreferences settings = getSharedPreferences("msettings",0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("LIGHTSEEKBAR4", lightSeekBar4.getProgress());
			editor.commit();
			
			exchangeData("LIGHTFOUR:" + String.valueOf(lightSeekBar4.getProgress()));
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	private OnSeekBarChangeListener lightSeekBar5Listener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			SharedPreferences settings = getSharedPreferences("msettings",0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("LIGHTSEEKBAR5", lightSeekBar5.getProgress());
			editor.commit();
			
			exchangeData("LIGHTFIVE:" + String.valueOf(lightSeekBar5.getProgress()));
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lights, menu);
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
}
