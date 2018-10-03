package com.codeflo.seg2105_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends Activity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.signUpLink);

    }

    public void signUpListener(View v){
        Intent signUp = new Intent(this, SignupActivity.class);
        startActivity(signUp);
    }

    public void onLogin(View v){
    }
}
