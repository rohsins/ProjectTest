package com.rohsins.project_test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends socket {
	
	EditText editText;
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		textView = (TextView)findViewById(R.id.settingsTextView3);
		SharedPreferences settings = getSharedPreferences("msettings",0);
		if(settings.getString("SERVERIPADDRESS", "192,168.1.9:8080").contains(":")) {
			ipAddressPort = settings.getString("SERVERIPADDRESS", "192.168.1.9:8080").split(":");
			Address = ipAddressPort[0];
			Port = Integer.parseInt(ipAddressPort[1]);
		}
		else {
			Address = settings.getString("SERVERIPADDRESS", "192.168.1.9");
		}
		textView.setText("Current Server IP:" + Address + ":" + Port);
		editText = (EditText)findViewById(R.id.settingsEditText1);
		
	}

public void Apply(View view) {

	inputIpAddressPort = editText.getText().toString();
		
		if(!(inputIpAddressPort.equals(""))) {
			if(inputIpAddressPort.contains(":")) {
				ipAddressPort = inputIpAddressPort.split(":");
				if(!(ipAddressPort[0].equals(""))) {
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
			textView.setText("Current Server IP:" + Address + ":" + Port);
			Toast.makeText(Settings.this, "Server IP Address is set to " + Address + ":" + Port, Toast.LENGTH_SHORT).show();
		}
		else if(Address.equals("")) {
			Toast.makeText(Settings.this, "Server IP Address is empty " + Address, Toast.LENGTH_SHORT).show();
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
