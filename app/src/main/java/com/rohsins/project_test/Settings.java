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
	Switch dSwitch;

	private boolean aSwitch_value;
	private boolean bSwitch_value;
	private boolean cSwitch_value;
	private boolean dSwitch_value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		aSwitch = findViewById(R.id.settingsSwitch1);
		bSwitch = findViewById(R.id.settingsSwitch2);
		cSwitch = findViewById(R.id.settingsSwitch3);
        dSwitch = findViewById(R.id.settingsSwitch4);
		textView = findViewById(R.id.settingsTextView3);
		textView2 = findViewById(R.id.settingsTextView4);
		editText = findViewById(R.id.settingsEditText1);
		editText2 = findViewById(R.id.settingsEditText2);

		if(settings.getString("SERVERIPADDRESS", "192,168.1.9:8080").contains(":")) {
			ipAddressPort = settings.getString("SERVERIPADDRESS", "192.168.1.9:8080").split(":");
			Address = ipAddressPort[0];
			Port = Integer.parseInt(ipAddressPort[1]);
		} else {
			Address = settings.getString("SERVERIPADDRESS", "192.168.1.9");
		}
        brokerAddress = settings.getString("MQTTBROKERADDRESS", "hardware.wscada.net");
		textView.setText("Current Server Socket:\n" + Address + ":" + Port + "\n" + brokerAddress + ":1883");

		aSwitch_value = settings.getBoolean("ENABLENAGLE", false);
		bSwitch_value = settings.getBoolean("ENABLEREUSEADDRESS", false);
		cSwitch_value = settings.getBoolean("ENABLEMQTT", false);
		dSwitch_value = AlwaysRunner.serviceIsAlive;

		aSwitch.setChecked(aSwitch_value);
		bSwitch.setChecked(bSwitch_value);
        cSwitch.setChecked(cSwitch_value);
        dSwitch.setChecked(dSwitch_value);

		aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				memWriteFlag = true;
				aSwitch_value = aSwitch.isChecked();
				aSwitch.setChecked(aSwitch_value);
				if(!aSwitch_value) {
					nagleFlag = false;
				}
				else if(aSwitch_value) {
					nagleFlag = true;
				}
			}
		});

		bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				memWriteFlag = true;
				bSwitch_value = bSwitch.isChecked();
				bSwitch.setChecked(bSwitch_value);
				if (!bSwitch_value) {
					reuseAddressFlag = false;
				} else if (bSwitch_value) {
					reuseAddressFlag = true;
				}
			}
		});

		cSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				memWriteFlag = true;
				cSwitch_value = cSwitch.isChecked();
				cSwitch.setChecked(cSwitch_value);
				if(!cSwitch_value) {
					mqttFlag = false;
				}
				else if(cSwitch_value) {
					mqttFlag = true;
				}
			}
		});

        dSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				memWriteFlag = true;
                dSwitch_value = dSwitch.isChecked();
                dSwitch.setChecked(dSwitch_value);
                if(dSwitch_value && !AlwaysRunner.serviceIsAlive) {
                    Intent intent = new Intent(Settings.this, AlwaysRunner.class);
                    startService(intent);
                }
                else if(!dSwitch_value && AlwaysRunner.serviceIsAlive) {
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
			memWriteFlag = true;
			editor.putString("SERVERIPADDRESS", Address + ":" + Port);
            textView.setText("Current Server Socket:\n" + Address + ":" + Port + "\n" + brokerAddress + ":1883");
			Toast.makeText(Settings.this, "Server IP Address is set to " + Address + ":" + Port, Toast.LENGTH_SHORT).show();
		} else if (!inputMqttBrokerIp.equals("") && inputIpAddressPort.equals("")) {
            brokerAddress = inputMqttBrokerIp;
			memWriteFlag = true;
            editor.putString("MQTTBROKERADDRESS", brokerAddress);
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
			memWriteFlag = true;
            editor.putString("SERVERIPADDRESS", Address + ":" + Port);
            editor.putString("MQTTBROKERADDRESS", brokerAddress);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();

		editor.putBoolean("ENABLENAGLE", aSwitch.isChecked());
		editor.putBoolean("ENABLEREUSEADDRESS", bSwitch.isChecked());
		editor.putBoolean("ENABLEMQTT", cSwitch.isChecked());
		editor.putBoolean("BACKGROUNDSERVICE", dSwitch.isChecked());
		if (memWriteFlag) {
			editor.commit();
			memWriteFlag = false;
		}
	}

}
