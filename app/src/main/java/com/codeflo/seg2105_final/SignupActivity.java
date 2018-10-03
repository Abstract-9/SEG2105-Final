package com.codeflo.seg2105_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SignupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Set up spinner
        Spinner userChoices = (Spinner) findViewById(R.id.userType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.userChoices, android.R.layout.simple_spinner_item);
        userChoices.setAdapter(adapter);
    }

    public void loginListener(View v){
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);

    }
}
