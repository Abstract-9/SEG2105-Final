package com.codeflo.seg2105_final;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.codeflo.seg2105_final.adapters.ServiceAdapter;
import com.codeflo.seg2105_final.models.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminActivity extends Activity {


    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        //Read the services info from the db. Once read, asynchronously
        //load into the listView.
        database = FirebaseFirestore.getInstance();
        database.collection("Services").get().addOnCompleteListener(updateListener);
    }

    OnCompleteListener<QuerySnapshot> updateListener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.isSuccessful()){
                ArrayList<Service> serviceList = new ArrayList<>();
                for(DocumentSnapshot doc : task.getResult().getDocuments()){
                    serviceList.add(new Service(doc.getId(), Integer.parseInt((String) doc.get("rate")), null));

                    ListView listServices = (ListView)findViewById(R.id.serviceList);
                    ServiceAdapter serviceAdapter = new ServiceAdapter(getApplicationContext(), serviceList);
                    listServices.setAdapter(serviceAdapter);
                }
            }
        }
    };

    public View addService(View v){
        //TODO add service
    }
}
