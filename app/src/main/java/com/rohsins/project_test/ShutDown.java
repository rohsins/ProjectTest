package com.rohsins.project_test;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class ShutDown extends Connectivity {

    EditText editText01;
    String password = null;

    public boolean passwordCheck() {
        password = editText01.getText().toString();
        if (password.equals("")) {
            Toast.makeText(ShutDown.this, "Please Enter the Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.equals("0315")) {
            return true;
        } else {
            Toast.makeText(ShutDown.this, "Wrong Password", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void shutDown(View view) {
        if (passwordCheck() == true) {
            exchangeData("ShutDown");
        }
    }

    public void shutDownAbort(View view) {
        if (passwordCheck() == true) {
            exchangeData("ShutDownAbort");
        }
    }

    public void restart(View view) {
        if (passwordCheck() == true) {
            exchangeData("Restart");
        }
    }

    public void hibernate(View view) {
        if (passwordCheck() == true) {
            exchangeData("Hibernate");
        }
    }

    public void wakeUp(View view) {
        if(passwordCheck() == true) {
            exchangeData("WakeUP");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shut_down);

        on_create_func();
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
        else if (id == R.id.action_password) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
