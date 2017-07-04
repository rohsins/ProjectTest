package com.rohsins.project_test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Connectivity {
	
	EditText editText;
	EditText editText2;
	TextView textView;
	TextView textView2;
	Switch aSwitch;
	Switch bSwitch;
	Switch cSwitch;
    static Switch dSwitch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		aSwitch = (Switch) findViewById(R.id.settingsSwitch1);
		bSwitch = (Switch) findViewById(R.id.settingsSwitch2);
		cSwitch = (Switch) findViewById(R.id.settingsSwitch3);
        dSwitch = (Switch) findViewById(R.id.settingsSwitch4);
		textView = (TextView) findViewById(R.id.settingsTextView3);
		textView2 = (TextView) findViewById(R.id.settingsTextView4);
		editText = (EditText) findViewById(R.id.settingsEditText1);
		editText2 = (EditText) findViewById(R.id.settingsEditText2);

		SharedPreferences settings = getSharedPreferences("msettings",0);
		if(settings.getString("SERVERIPADDRESS", "192,168.1.9:8080").contains(":")) {
			ipAddressPort = settings.getString("SERVERIPADDRESS", "192.168.1.9:8080").split(":");
			Address = ipAddressPort[0];
			Port = Integer.parseInt(ipAddressPort[1]);
		} else {
			Address = settings.getString("SERVERIPADDRESS", "192.168.1.9");
		}
        brokerAddress = settings.getString("MQTTBROKERADDRESS", "m2m.eclipse.org");
		textView.setText("Current Server Socket:\n" + Address + ":" + Port + "\n" + brokerAddress + ":1883");

		aSwitch.setChecked(settings.getBoolean("ENABLENAGLE", false));
		bSwitch.setChecked(settings.getBoolean("ENABLEREUSEADDRESS", false));
        cSwitch.setChecked(settings.getBoolean("ENABLEMQTT", false));
        dSwitch.setChecked(AlwaysRunner.serviceIsAlive);

		aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				SharedPreferences settings = getSharedPreferences("msettings",0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("ENABLENAGLE", aSwitch.isChecked());
				editor.commit();
				aSwitch.setChecked(settings.getBoolean("ENABLENAGLE", false));
				if(!settings.getBoolean("ENABLENAGLE", false)) {
					nagleFlag = false;
				}
				else if(settings.getBoolean("ENABLENAGLE", false)) {
					nagleFlag = true;
				}
			}
		});

		bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				SharedPreferences settings = getSharedPreferences("msettings", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("ENABLEREUSEADDRESS", bSwitch.isChecked());
				editor.commit();
				bSwitch.setChecked(settings.getBoolean("ENABLEREUSEADDRESS", false));
				if (!settings.getBoolean("ENABLEREUSEADDRESS", false)) {
					reuseAddressFlag = false;
				} else if (settings.getBoolean("ENABLEREUSEADDRESS", false)) {
					reuseAddressFlag = true;
				}
			}
		});

		cSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				SharedPreferences settings = getSharedPreferences("msettings",0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("ENABLEMQTT", cSwitch.isChecked());
				editor.commit();
				cSwitch.setChecked(settings.getBoolean("ENABLEMQTT", false));
				if(!settings.getBoolean("ENABLEMQTT", false)) {
					mqttFlag = false;
				}
				else if(settings.getBoolean("ENABLEMQTT", false)) {
					mqttFlag = true;
				}
			}
		});

        dSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getSharedPreferences("msettings",0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("BACKGROUNDSERVICE", dSwitch.isChecked());
                editor.commit();
                dSwitch.setChecked(settings.getBoolean("BACKGROUNDSERVICE", false));
                if(settings.getBoolean("BACKGROUNDSERVICE", true) && !AlwaysRunner.serviceIsAlive) {
                    Intent intent = new Intent(Settings.this, AlwaysRunner.class);
                    startService(intent);
                }
                else if(!settings.getBoolean("BACKGROUNDSERVICE", true) && AlwaysRunner.serviceIsAlive) {
                    Intent intent = new Intent(Settings.this, AlwaysRunner.class);
                    stopService(intent);
                }
            }
        });
	}

	public void Apply(View view) {

		inputIpAddressPort = editText.getText().toString();
        inputMqttBrokerIp = editText2.getText().toString();
		
		if (!inputIpAddressPort.equals("") && inputMqttBrokerIp.equals("")) {
			if (inputIpAddressPort.contains(":")) {
				ipAddressPort = inputIpAddressPort.split(":");
				if (!(ipAddressPort[0].equals(""))) {
					Address = ipAddressPort[0];
				}
				Port = Integer.parseInt(ipAddressPort[1]);
			}
			else {
				Address = inputIpAddressPort;
			}
			SharedPreferences settings = getSharedPreferences("msettings",0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("SERVERIPADDRESS", Address + ":" + Port);
			editor.commit();
            textView.setText("Current Server Socket:\n" + Address + ":" + Port + "\n" + brokerAddress + ":1883");
			Toast.makeText(Settings.this, "Server IP Address is set to " + Address + ":" + Port, Toast.LENGTH_SHORT).show();
		} else if (!inputMqttBrokerIp.equals("") && inputIpAddressPort.equals("")) {
            brokerAddress = inputMqttBrokerIp;
            SharedPreferences settings = getSharedPreferences("msettings", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("MQTTBROKERADDRESS", brokerAddress);
            editor.commit();
            textView.setText("Current Server Socket:\n" + Address + ":" + Port + "\n" + brokerAddress + ":1883");
            Toast.makeText(Settings.this, "Mqtt Broker Address is set to " + brokerAddress, Toast.LENGTH_SHORT).show();
        } else if (inputIpAddressPort.equals("") || inputMqttBrokerIp.equals("")) {
			Toast.makeText(Settings.this, "Server IP Address or Mqtt Broker Address is empty", Toast.LENGTH_SHORT).show();
		} else if (!inputMqttBrokerIp.equals("") && !inputIpAddressPort.equals("")) {
            if (inputIpAddressPort.contains(":")) {
                ipAddressPort = inputIpAddressPort.split(":");
                if (!(ipAddressPort[0].equals(""))) {
                    Address = ipAddressPort[0];
                }
                Port = Integer.parseInt(ipAddressPort[1]);
            }
            else {
                Address = inputIpAddressPort;
            }
            brokerAddress = inputMqttBrokerIp;
            SharedPreferences settings = getSharedPreferences("msettings",0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("SERVERIPADDRESS", Address + ":" + Port);
            editor.putString("MQTTBROKERADDRESS", brokerAddress);
            editor.commit();
            textView.setText("Current Server Socket:\n" + Address + ":" + Port + "\n" + brokerAddress + ":1883");
            Toast.makeText(Settings.this, "Server IP Address is set to " + Address + ":" + Port + "\n" + brokerAddress + ":1883", Toast.LENGTH_SHORT).show();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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
