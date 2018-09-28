package com.loroad.seg2105_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void loginListener(View v){
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);

    }
}
