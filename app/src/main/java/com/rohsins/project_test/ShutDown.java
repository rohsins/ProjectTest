package com.rohsins.project_test;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class ShutDown extends socket {

    EditText editText01;
    String password = null;

    public void shutDown(View view) {
        password = editText01.getText().toString();
        if(password.equals("0315")) {
            exchangeData("ShutDown");
        } else {
            Toast.makeText(ShutDown.this, "Wrong Password", Toast.LENGTH_SHORT).show();
        }
    }

    public void shutDownAbort(View view) {
        password = editText01.getText().toString();
        if(password.equals("0315")) {
            exchangeData("ShutDownAbort");
        } else {
            Toast.makeText(ShutDown.this, "Wrong Password", Toast.LENGTH_SHORT).show();
        }
    }

    public void hibernate(View view) {
        password = editText01.getText().toString();
        if(password.equals("0315")) {
            exchangeData("Hibernate");
        } else {
            Toast.makeText(ShutDown.this, "Wrong Password", Toast.LENGTH_SHORT).show();
        }
    }

    public void wakeUp(View view) {
        password = editText01.getText().toString();
        if(password.equals("0315")) {
            exchangeData("WakeUP");
        } else {
            Toast.makeText(ShutDown.this, "Wrong Password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shut_down);

        editText01 = (EditText) findViewById(R.id.shutDownEditText01);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shut_down, menu);
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
