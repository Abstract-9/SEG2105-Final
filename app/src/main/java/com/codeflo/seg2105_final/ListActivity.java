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


public class ListActivity extends Activity {

    boolean byName;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        TextView searchBar = (TextView) findViewById(searchBar);
        searchBar.setText(getIntent().getStringExtra("search"));
        byName = getIntent().getBooleanExtra("byName", false);

        db = FirebaseFirestore.getInstance();

        if(byName){
            db.collection("users").whereEqualTo()
        }

    }

    public void searchBarCLicked(View v){
        finish();
    }



    OnCompleteListener<QuerySnapshot> serviceSearch = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.isSuccessful() && task.getResult()!=null){
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    if(doc.get)
                }
            }
        }
    };
}
