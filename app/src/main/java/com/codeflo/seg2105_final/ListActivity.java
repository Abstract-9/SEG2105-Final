package com.codeflo.seg2105_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.codeflo.seg2105_final.adapters.ProviderAdapter;
import com.codeflo.seg2105_final.models.Service;
import com.codeflo.seg2105_final.models.ServiceProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ListActivity extends Activity {

    String search;
    boolean byName;

    ArrayList<ServiceProvider> providerList;
    ProviderAdapter adapter;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        providerList = new ArrayList<>();

        TextView searchBar = (TextView) findViewById(R.id.searchBar);
        search = getIntent().getStringExtra("search");
        searchBar.setText(search);
        byName = getIntent().getBooleanExtra("byName", false);

        db = FirebaseFirestore.getInstance();

        if(byName){
            db.collection("users").document(getIntent().getStringExtra("search")).
                    get().addOnCompleteListener(userSearch);
        }else{
            db.collection("users").get().addOnCompleteListener(serviceSearch);
        }

    }

    public void searchBarClicked(View v){
        finish();
    }



    OnCompleteListener<QuerySnapshot> serviceSearch = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.isSuccessful() && task.getResult()!=null){
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    QuerySnapshot services = doc.getReference().collection("Services").get().getResult();
                    if(services!=null) {
                        for (DocumentSnapshot service : services.getDocuments()) {
                            if (service.getId().equals(search)) {
                                ServiceProvider current = new ServiceProvider(doc.getId(), (String) doc.get("Address"), (int) service.get("rate"));
                                providerList.add(current);
                            }
                        }
                    }
                }
            }
            adapter = new ProviderAdapter(ListActivity.this, providerList);
            ((ListView)findViewById(R.id.providerList)).setAdapter(adapter);
        }
    };

    OnCompleteListener<DocumentSnapshot> userSearch = new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if(task.isSuccessful() && task.getResult()!=null){
                DocumentSnapshot doc = task.getResult();
                ServiceProvider current = new ServiceProvider(doc.getId(), (String) doc.get("Address"), (int) doc.get("rate"));
                providerList.add(current);
                adapter = new ProviderAdapter(ListActivity.this, providerList);
                ((ListView)findViewById(R.id.providerList)).setAdapter(adapter);
            }
        }
    };
}
