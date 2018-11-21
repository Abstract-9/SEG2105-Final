package com.codeflo.seg2105_final;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
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

    ServiceAdapter adapter;
    ArrayList<Service> services;
    ArrayList<Availability> availabilities;
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

                    ListView listServices = (ListView)findViewById(R.id.serviceList);
                    adapter = new ServiceAdapter(ProviderActivity.this, serviceList, username);
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

    public void editAvailability(View v){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        ViewGroup parent = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.dialog_availability, null);

        if(availabilities!=null){
            for(Availability av : availabilities) {
                View tmp = LayoutInflater.from(this).inflate(R.layout.dialog_availability_item, parent);
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
            }
        }

        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog tmp = (AlertDialog) dialog;

                ViewGroup parent = (ViewGroup) tmp.findViewById(R.id.availList);

                db = FirebaseFirestore.getInstance();

                for(int i=0;i<parent.getChildCount()-1;i++){
                    View current = parent.getChildAt(i);
                    Spinner day = (Spinner) current.findViewById(R.id.day), time1 = (Spinner) current.findViewById(R.id.time1),
                            time2 = (Spinner) current.findViewById(R.id.time2);

                    Map<String, String> map = new HashMap<>();

                    map.put("Day", (String) day.getSelectedItem());
                    map.put("Time1", (String) time1.getSelectedItem());
                    map.put("Time2", (String) time2.getSelectedItem());


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

        dialogBuilder.setTitle("Choose a service to add");
        if(adapter!=null) dialogBuilder.setAdapter(adapter, serviceAddListener);

        else dialogBuilder.setMessage("DatabaseError: Check internet Connection.");

        dialogBuilder.create().show();
    }

    public void submitListener(View v){

        String phone = ((TextView)findViewById(R.id.phone)).getText().toString();
        String name = ((TextView)findViewById(R.id.companyName)).getText().toString();
        String address = ((TextView)findViewById(R.id.address)).getText().toString();
        String desc = ((TextView)findViewById(R.id.description)).getText().toString();
        boolean licensed = ((Spinner)findViewById(R.id.licensed)).getSelectedItemPosition()==1;

        //TODO verify

        Map<String, String> map = new HashMap<>();

        map.put("Phone", phone);
        map.put("CompanyName", name);
        map.put("Address", address);
        map.put("Description", desc);
        map.put("Licensed", licensed?"Yes":"No");

        db = FirebaseFirestore.getInstance();

        db.collection("users").document(username).set(map);



        /*
        TODO for this section:

        Pull info from UI

        VERIFY

        Map and submit it to FireStore

        Pull info from FireStore and put it back into UI for consistency
         */
    }
}
