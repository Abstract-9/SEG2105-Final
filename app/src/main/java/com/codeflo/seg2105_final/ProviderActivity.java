package com.codeflo.seg2105_final;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class ProviderActivity extends Activity {

    ServiceAdapter adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        db = FirebaseFirestore.getInstance();
        db.collection("Service").get().addOnCompleteListener(serviceListener);

    }

    OnCompleteListener<QuerySnapshot> serviceListener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.isSuccessful()){
                ArrayList<Service> serviceList = new ArrayList<>();

                for(DocumentSnapshot doc : task.getResult().getDocuments()){
                    serviceList.add(new Service(doc.getId(), Double.parseDouble((String) doc.get("rate")), null));

                    ListView listServices = (ListView)findViewById(R.id.serviceList);
                    adapter = new ServiceAdapter(ProviderActivity.this, serviceList);
                }
            }
        }
    };

    DialogInterface.OnClickListener serviceAddListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Service choice = adapter.getService(which);
            db = FirebaseFirestore.getInstance();

            Map<String, String> reference = new HashMap<>();
            reference.put(choice.getName(), "Services/" + choice.getName());

            db.collection("users").document("SERVICEPROVIDER")
                    .collection("Services").document(choice.getName()).set(reference);
        }
    };


    public void addServiceListener(View v){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle("Choose a service to add");
        if(adapter!=null) dialogBuilder.setAdapter(adapter, serviceAddListener);

        else dialogBuilder.setMessage("DatabaseError: Check internet Connection.");

    }
}
