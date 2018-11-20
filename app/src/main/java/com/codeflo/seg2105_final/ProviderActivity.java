package com.codeflo.seg2105_final;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CalendarView;
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

public class ProviderActivity extends Activity {

    ServiceAdapter adapter;
    ArrayList<Service> services;
    FirebaseFirestore db;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        db = FirebaseFirestore.getInstance();

        username = (String) getIntent().getExtras().get("username");

        db.collection("Services").get().addOnCompleteListener(allServicesListener);
        db.collection("users").document(username)
                .collection("Services").get().addOnCompleteListener(userServicesListener);
    }

    OnCompleteListener<QuerySnapshot> allServicesListener = new OnCompleteListener<QuerySnapshot>() {
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

    OnCompleteListener<QuerySnapshot> userServicesListener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.isSuccessful()){
                services = new ArrayList<>();
                for(DocumentSnapshot doc : task.getResult().getDocuments()){
                    services.add(new Service(doc.getId(), Double.parseDouble((String) doc.get("rate")), null));
                }
                //TODO add services to ListView on UI
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

            db.collection("users").document(username)
                    .collection("Services").document(choice.getName()).set(reference);
            dialog.dismiss();
        }
    };


    public void addServiceListener(View v){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle("Choose a service to add");
        if(adapter!=null) dialogBuilder.setAdapter(adapter, serviceAddListener);

        else dialogBuilder.setMessage("DatabaseError: Check internet Connection.");

        dialogBuilder.create().show();
    }

    public void submitListener(View v){
        /*
        TODO for this section:

        Pull info from UI

        VERIFY

        Map and submit it to FireStore

        Pull info from FireStore and put it back into UI for consistency
         */
    }

    public void selectAvailability(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        CalendarView calendarView = new CalendarView(this);

        calendarView.setMinDate(System.currentTimeMillis());
        calendarView.setMaxDate(System.currentTimeMillis() + (long) 6.048e+8);


        builder.setTitle("Select Availability").setView(new CalendarView(this));


    }
}