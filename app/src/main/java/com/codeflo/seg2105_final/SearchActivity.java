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
import com.google.firebase.firestore.QuerySnapshot;

public class SearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }



    public void search(View v){
        TextView search = findViewById(R.id.searchBar);

        if(!search.getText().toString().equals("")) {
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra("search", search.getText().toString());
            intent.putExtra("byName", byName);
            startActivity(intent);
        }


    }
}
}
