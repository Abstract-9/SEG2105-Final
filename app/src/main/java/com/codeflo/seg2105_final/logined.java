package com.codeflo.seg2105_final;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class logined extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logined);

        String username = (String)getIntent().getExtras().get("username");

        TextView text = (TextView)findViewById(R.id.username);

        text.setText(text.getText() + username);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("user").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.getResult()!=null) {

                    TextView text2 = (TextView)findViewById(R.id.type);

                    text2.setText(text2.getText() + task.getResult().get("Type").toString());
                }


            }
        });

    }
}
