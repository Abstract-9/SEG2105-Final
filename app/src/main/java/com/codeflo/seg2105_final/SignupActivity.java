package com.codeflo.seg2105_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends Activity {

    FirebaseFirestore db;

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

    OnCompleteListener<Void> userAddListener = new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                login.putExtra("CreationSuccess", true);
                startActivity(login);
            }else{
                //TODO throw error
            }
        }
    };

    public void submitListener(View v){

        EditText email = (EditText) findViewById(R.id.email);
        EditText user = (EditText) findViewById(R.id.userName);
        EditText password = (EditText) findViewById(R.id.password);
        Spinner userChoices = (Spinner) findViewById(R.id.userType);
        String choice = userChoices.getSelectedItem().toString();



        db = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("email", email.getText());
        map.put("password", password.getText());
        map.put("type", choice);
        db.collection("users").document(user.getText().toString()).set(map).
                addOnCompleteListener(userAddListener);
    }
}
