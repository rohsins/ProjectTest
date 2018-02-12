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

public class Lights extends Connectivity {
	
	SeekBar lightSeekBar1;
	SeekBar lightSeekBar2;
	SeekBar lightSeekBar3;
	SeekBar lightSeekBar4;
	SeekBar lightSeekBar5;

	private int lightSeekBar1_value;
	private int lightSeekBar2_value;
	private int lightSeekBar3_value;
	private int lightSeekBar4_value;
	private int lightSeekBar5_value;
	
	Switch lightSwitch1;
	Switch lightSwitch2;
	Switch lightSwitch3;
	Switch lightSwitch4;
	Switch lightSwitch5;

	private boolean lightSwitch1_value;
	private boolean lightSwitch2_value;
	private boolean lightSwitch3_value;
	private boolean lightSwitch4_value;
	private boolean lightSwitch5_value;

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

		on_create_func();
		
		lightSeekBar1_value = settings.getInt("LIGHTSEEKBAR1", 20);
		lightSeekBar1.setProgress(lightSeekBar1_value);
		lightSeekBar2_value = settings.getInt("LIGHTSEEKBAR2", 20);
		lightSeekBar2.setProgress(lightSeekBar2_value);
		lightSeekBar3_value = settings.getInt("LIGHTSEEKBAR3", 20);
		lightSeekBar3.setProgress(lightSeekBar3_value);
		lightSeekBar4_value = settings.getInt("LIGHTSEEKBAR4", 20);
		lightSeekBar4.setProgress(lightSeekBar4_value);
		lightSeekBar5_value = settings.getInt("LIGHTSEEKBAR5", 20);
		lightSeekBar5.setProgress(lightSeekBar5_value);

		lightSwitch1_value = settings.getBoolean("LIGHTSWITCH1", false);
		lightSwitch1.setChecked(lightSwitch1_value);
		lightSwitch2_value = settings.getBoolean("LIGHTSWITCH2", false);
		lightSwitch2.setChecked(lightSwitch2_value);
		lightSwitch3_value = settings.getBoolean("LIGHTSWITCH3", false);
		lightSwitch3.setChecked(lightSwitch3_value);
		lightSwitch4_value = settings.getBoolean("LIGHTSWITCH4", false);
		lightSwitch4.setChecked(lightSwitch4_value);
		lightSwitch5_value = settings.getBoolean("LIGHTSWITCH5", false);
		lightSwitch5.setChecked(lightSwitch5_value);

		lightSeekBar1.setEnabled(lightSwitch1_value);
		lightSeekBar2.setEnabled(lightSwitch2_value);
		lightSeekBar3.setEnabled(lightSwitch3_value);
		lightSeekBar4.setEnabled(lightSwitch4_value);
		lightSeekBar5.setEnabled(lightSwitch5_value);
		
		lightSeekBar1.setOnSeekBarChangeListener(lightSeekBar1Listener);
		lightSeekBar2.setOnSeekBarChangeListener(lightSeekBar2Listener);
		lightSeekBar3.setOnSeekBarChangeListener(lightSeekBar3Listener);
		lightSeekBar4.setOnSeekBarChangeListener(lightSeekBar4Listener);
		lightSeekBar5.setOnSeekBarChangeListener(lightSeekBar5Listener);
		
		lightSwitch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				lightSwitch1_value = lightSwitch1.isChecked();
				lightSeekBar1.setEnabled(lightSwitch1_value);
				if(!lightSwitch1_value) {
					exchangeData("LIGHTONE:" + String.valueOf(0));
				}
				else if(lightSwitch1_value) {
					exchangeData("LIGHTONE:" + lightSeekBar1_value);
				}
			}
			
		});
		
		lightSwitch2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				lightSwitch2_value = lightSwitch2.isChecked();
				lightSeekBar2.setEnabled(lightSwitch2_value);
				if(!lightSwitch2_value) {
					exchangeData("LIGHTTWO:" + String.valueOf(0));
				}
				else if(lightSwitch2_value) {
					exchangeData("LIGHTTWO:" + lightSeekBar2_value);
				}
			}
			
		});
		
		lightSwitch3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				lightSwitch3_value = lightSwitch3.isChecked();
				lightSeekBar3.setEnabled(lightSwitch3_value);
				if(!lightSwitch3_value) {
					exchangeData("LIGHTTHREE:" + String.valueOf(0));
				}
				else if(lightSwitch3_value) {
					exchangeData("LIGHTTHREE:" + lightSeekBar3_value);
				}
			}
			
		});
		
		lightSwitch4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				lightSwitch4_value = lightSwitch4.isChecked();
				lightSeekBar4.setEnabled(lightSwitch4_value);
				if(!lightSwitch4_value) {
					exchangeData("LIGHTFOUR:" + String.valueOf(0));
				}
				else if(lightSwitch4_value) {
					exchangeData("LIGHTFOUR:" + lightSeekBar4_value);
				}
			}
			
		});
		
		lightSwitch5.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				lightSwitch5_value = lightSwitch5.isChecked();
				lightSeekBar5.setEnabled(lightSwitch5_value);
				if(!lightSwitch5_value) {
					exchangeData("LIGHTFIVE:" + String.valueOf(0));
				}
				else if(lightSwitch5_value) {
					exchangeData("LIGHTFIVE:" + lightSeekBar5_value);
				}
			}
			
		});
		
	}

	OnSeekBarChangeListener lightSeekBar1Listener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			lightSeekBar1_value = lightSeekBar1.getProgress();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			exchangeData("LIGHTONE:" + String.valueOf(lightSeekBar1_value));
		}
		
	};
	
	
	private OnSeekBarChangeListener lightSeekBar2Listener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			lightSeekBar2_value = lightSeekBar2.getProgress();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			exchangeData("LIGHTTWO:" + String.valueOf(lightSeekBar2_value));
		}
		
	};
	
	
	private OnSeekBarChangeListener lightSeekBar3Listener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			lightSeekBar3_value = lightSeekBar3.getProgress();

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			exchangeData("LIGHTTHREE:" + String.valueOf(lightSeekBar3_value));
		}
		
	};
	
	
	private OnSeekBarChangeListener lightSeekBar4Listener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			lightSeekBar4_value = lightSeekBar4.getProgress();

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			exchangeData("LIGHTFOUR:" + String.valueOf(lightSeekBar4_value));
		}
		
	};
	
	
	private OnSeekBarChangeListener lightSeekBar5Listener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			lightSeekBar5_value = lightSeekBar5.getProgress();

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			exchangeData("LIGHTFIVE:" + String.valueOf(lightSeekBar5_value));
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

	@Override
	protected void onDestroy() {
		super.onDestroy();

		editor.putBoolean("LIGHTSWITCH1", lightSwitch1.isChecked());
		editor.putBoolean("LIGHTSWITCH2", lightSwitch2.isChecked());
		editor.putBoolean("LIGHTSWITCH3", lightSwitch3.isChecked());
		editor.putBoolean("LIGHTSWITCH4", lightSwitch4.isChecked());
		editor.putBoolean("LIGHTSWITCH5", lightSwitch5.isChecked());

		editor.putInt("LIGHTSEEKBAR1", lightSeekBar1_value);
		editor.putInt("LIGHTSEEKBAR2", lightSeekBar2_value);
		editor.putInt("LIGHTSEEKBAR3", lightSeekBar3_value);
		editor.putInt("LIGHTSEEKBAR4", lightSeekBar4_value);
		editor.putInt("LIGHTSEEKBAR5", lightSeekBar5_value);
	}
}
