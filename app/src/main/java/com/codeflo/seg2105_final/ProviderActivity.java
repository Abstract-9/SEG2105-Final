package com.codeflo.seg2105_final;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codeflo.seg2105_final.adapters.ServiceAdapter;
import com.codeflo.seg2105_final.models.Availability;
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

    ServiceAdapter allServiceAdapter, userServiceAdapter;
    ArrayList<Service> services;
    ArrayList<Availability> availabilities;
    FirebaseFirestore db;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        db = FirebaseFirestore.getInstance();
        services = new ArrayList<>();

        username = (String) getIntent().getExtras().get("username");

        db.collection("Services").get().addOnCompleteListener(allServicesListener);
        db.collection("users").document(username)
                .get().addOnCompleteListener(userServicesListener);
        db.collection("users").document(username).collection("Times")
                .get().addOnCompleteListener(userAvailsListener);
    }

    OnCompleteListener<QuerySnapshot> allServicesListener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.isSuccessful()){
                ArrayList<Service> serviceList = new ArrayList<>();

                for(DocumentSnapshot doc : task.getResult().getDocuments()){
                    serviceList.add(new Service(doc.getId(), Double.parseDouble((String) doc.get("rate")), null));
                }

                allServiceAdapter = new ServiceAdapter(ProviderActivity.this, serviceList, username, 1);
            }
        }
    };

    OnCompleteListener<DocumentSnapshot> userServicesListener = new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if(task.isSuccessful()){
                ArrayList<String> tmp = (ArrayList) task.getResult().get("Services");
                if(tmp == null) tmp = new ArrayList<>();
                for(String service : tmp){
                    services.add(new Service(service.split(":")[0], Double.parseDouble(service.split(":")[1]), null));
                }
                userServiceAdapter = new ServiceAdapter(ProviderActivity.this, services, username, 0);
            }
        }
    };

    OnCompleteListener<QuerySnapshot> userAvailsListener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.isSuccessful() && !task.getResult().getDocuments().isEmpty()){
                availabilities = new ArrayList<>();
                for(DocumentSnapshot doc : task.getResult().getDocuments()){
                    availabilities.add(new Availability((String)doc.get("Day"),
                            (String)doc.get("Time1"), (String)doc.get("Time2")));
                }
            }
        }
    };

    public void editAvailability(View v){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        ViewGroup parent = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.dialog_availability, null);
        ViewGroup sub = (ViewGroup) parent.findViewById(R.id.availList);

        if(availabilities!=null){
            for(Availability av : availabilities) {
                View tmp = LayoutInflater.from(this).inflate(R.layout.dialog_availability_item, null);
                Spinner day = (Spinner) tmp.findViewById(R.id.day), time1 = (Spinner) tmp.findViewById(R.id.time1),
                        time2 = (Spinner) tmp.findViewById(R.id.time2);
                for (int i = 0; i < day.getCount(); i++) {
                    day.setSelection(i);
                    if (day.getSelectedItem().equals(av.getDay())) break;
                }
                for (int i = 0; i < time1.getCount(); i++) {
                    time1.setSelection(i);
                    if (time1.getSelectedItem().equals(av.getTime1())) break;
                }
                for (int i = 0; i < time2.getCount(); i++) {
                    time2.setSelection(i);
                    if (time2.getSelectedItem().equals(av.getTime2())) break;
                }
                sub.addView(tmp,0);
            }
        }

        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog tmp = (AlertDialog) dialog;

                ViewGroup parent = (ViewGroup) tmp.findViewById(R.id.availList);

                db = FirebaseFirestore.getInstance();

                for(int i=availabilities.size();i<parent.getChildCount()-1;i++){
                    View current = parent.getChildAt(i);
                    Spinner day = (Spinner) current.findViewById(R.id.day), time1 = (Spinner) current.findViewById(R.id.time1),
                            time2 = (Spinner) current.findViewById(R.id.time2);

                    Map<String, String> map = new HashMap<>();

                    map.put("Day", (String) day.getSelectedItem());
                    map.put("Time1", (String) time1.getSelectedItem());
                    map.put("Time2", (String) time2.getSelectedItem());

                    availabilities.add(new Availability((String) day.getSelectedItem(),
                            (String) time1.getSelectedItem(), (String) time2.getSelectedItem()));

                    db.collection("users").document(username)
                            .collection("Times").add(map);
                }

                dialog.dismiss();
                Toast.makeText(ProviderActivity.this,
                        "Availabilities successfully updated", Toast.LENGTH_LONG).show();
            }
        });

        dialogBuilder.setView(parent);

        AlertDialog dialog = dialogBuilder.create();

        dialog.show();
    }

    public void layoutAddAvail(View v){
        ViewGroup parent = (ViewGroup) v.getParent();
        View newAvail = LayoutInflater.from(this).inflate(R.layout.dialog_availability_item, null);
        parent.addView(newAvail, parent.getChildCount()-1);
    }


    public void editServices(View v){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle("Your Services");
        dialogBuilder.setAdapter(userServiceAdapter, null);

        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogBuilder.setNegativeButton("Add Service", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder innerBuilder = new AlertDialog.Builder(ProviderActivity.this);
                innerBuilder.setAdapter(allServiceAdapter, null);
                innerBuilder.setTitle("Select a service to add");
                AlertDialog tmp = innerBuilder.create();
                allServiceAdapter.setParentDialog(tmp);
                tmp.show();
                tmp.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        db.collection("users").document(username)
                                .get().addOnCompleteListener(userServicesListener);
                    }
                });
            }
        });

        dialogBuilder.create().show();
    }

    public void submitListener(View v){

        String phone = ((TextView)findViewById(R.id.phone)).getText().toString();
        String name = ((TextView)findViewById(R.id.companyName)).getText().toString();
        String address = ((TextView)findViewById(R.id.address)).getText().toString();
        String desc = ((TextView)findViewById(R.id.description)).getText().toString();
        boolean licensed = ((Spinner)findViewById(R.id.licensed)).getSelectedItemPosition()==1;

        //TODO verify

        Map<String, Object> map = new HashMap<>();

        map.put("Phone", phone);
        map.put("CompanyName", name);
        map.put("Address", address);
        map.put("Description", desc);
        map.put("Licensed", licensed?"Yes":"No");

        db = FirebaseFirestore.getInstance();

        db.collection("users").document(username).update(map);

        Toast.makeText(this, "Content updated!", Toast.LENGTH_LONG).show();

        /*
        TODO for this section:

        Pull info from UI

        VERIFY

        Map and submit it to FireStore

        Pull info from FireStore and put it back into UI for consistency
         */
    }
}
