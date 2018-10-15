package com.codeflo.seg2105_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.Map;

public class LoginActivity extends Activity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.signUpLink);

        if(getIntent().getExtras()!=null){
            findViewById(R.id.signinText).setVisibility(View.VISIBLE);
        }
    }

    public void signUpListener(View v){
        Intent signUp = new Intent(this, SignupActivity.class);
        startActivity(signUp);
    }

    OnCompleteListener<DocumentSnapshot> loginListener = new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if(task.getResult()!=null) {

                TextView username = (TextView) findViewById(R.id.username);
                TextView userPass = (TextView) findViewById(R.id.password);
                DocumentSnapshot user = task.getResult();
                if(user.get("Password").equals(userPass.getText().toString())){
                    Intent login = new Intent(getApplicationContext(), logined.class);
                    login.putExtra("username", username.getText().toString());
                    login.putExtra("type", (String) user.get("Type"));
                    startActivity(login);
                }else{
                    //TODO unknown user
                }
            }else{
                //TODO throw error
            }
        }
    };

    public void onLogin(View v){
        TextView userInput = (TextView) findViewById(R.id.username);

        db = FirebaseFirestore.getInstance();
        db.collection("users").document(userInput.getText().toString()).get().addOnCompleteListener(loginListener);
    }
}
