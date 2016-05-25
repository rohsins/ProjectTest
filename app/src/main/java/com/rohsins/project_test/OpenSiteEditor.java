package com.rohsins.project_test;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OpenSiteEditor extends socket {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_site_editor);

        on_create_func();
        editText = (EditText) findViewById(R.id.openSiteEditorEdittext01);
    }

    public void send(View view) {
        if (!editText.getText().toString().equals("")) {
            exchangeData(editText.getText().toString());
        } else {
            Toast.makeText(OpenSiteEditor.this, "Empty String", Toast.LENGTH_SHORT).show();
        }
    }

}
