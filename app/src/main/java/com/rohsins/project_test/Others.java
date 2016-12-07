package com.rohsins.project_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Others extends Connectivity {

    public void othersFunction01(View view) {
        exchangeData("Altium");
    }

    public void othersFunction02(View view) {
        exchangeData("Chrome");
    }

    public void othersFunction03(View view) {
        exchangeData("ShowDesktop");
    }

    public void othersFunction04(View view) {
        Intent intent = new Intent(this, ShutDown.class);
        startActivity(intent);
    }

    public void othersFunction05(View view) {
        Intent intent = new Intent(this, OpenSiteEditor.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);

//        SharedPreferences settings = getSharedPreferences("msettings",0);
//        Address = settings.getString("SERVERIPADDRESS", "192.168.1.9");
        on_create_func();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_others, menu);
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
