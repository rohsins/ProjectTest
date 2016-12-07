package com.rohsins.project_test;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class OpenSiteEditor extends Connectivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_site_editor);

        on_create_func();
        editText = (EditText) findViewById(R.id.openSiteEditorEdittext01);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.open_site_editor, menu);
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

    public void send(View view) {
        if (!editText.getText().toString().equals("")) {
            exchangeData(editText.getText().toString());
        } else {
            Toast.makeText(OpenSiteEditor.this, "Empty String", Toast.LENGTH_SHORT).show();
        }
    }

}
