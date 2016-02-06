package com.rohsins.project_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

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

    public void SerialViewer(View view) {
        Intent intent = new Intent(this, SerialViewer.class);
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,Settings.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
