package com.codeflo.seg2105_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        TextView search = (TextView) findViewById(R.id.searchBar);
        RadioGroup group = (RadioGroup) findViewById(R.id.searchType);

        int selectedID = group.getCheckedRadioButtonId();

        if(selectedID==-1){
            Toast.makeText(this, "Please select a search type", Toast.LENGTH_LONG).show();
        } else if(!search.getText().toString().equals("")) {
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra("search", search.getText().toString());
            intent.putExtra("byName", selectedID==R.id.byName);
            startActivity(intent);
        }

    }
}
