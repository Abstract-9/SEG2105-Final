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

    }

    public void signUpListener(View v){
        Intent signUp = new Intent(this, SignupActivity.class);
        startActivity(signUp);
    }

    OnCompleteListener<QuerySnapshot> loginListener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.getResult()!=null) {
                TextView userPass = (TextView) findViewById(R.id.password);
                QuerySnapshot result = task.getResult();
                DocumentSnapshot user = result.getDocuments().get(0);
                if(user.get("Password").equals(userPass.getText())){
                    //TODO authentication successful
                }else{
                    //TODO unknown user
                }
            }else{
                //TODO throw error
            }
        }
    };

    public void onLogin(View v){
        TextView userInput = (TextView) findViewById(R.id.userName);

        db = FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("Username", userInput.getText())
                .get().addOnCompleteListener(loginListener);
    }
}
