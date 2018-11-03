package com.codeflo.seg2105_final;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String username = (String)getIntent().getExtras().get("username");

        TextView text = (TextView)findViewById(R.id.username);

        text.setText(text.getText() + " " + username);

        TextView text2 = (TextView) findViewById(R.id.type);

        text2.setText(text2.getText() + " " + getIntent().getExtras().get("type"));


    }
}
