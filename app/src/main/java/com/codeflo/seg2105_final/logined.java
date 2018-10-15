package com.codeflo.seg2105_final;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class logined extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logined);

        String username = (String)getIntent().getExtras().get("username");

        TextView text = (TextView)findViewById(R.id.username);

        text.setText(text.getText() + " " + username);

        TextView text2 = (TextView) findViewById(R.id.type);

        text2.setText(text2.getText() + " " + getIntent().getExtras().get("type"));


    }
}
