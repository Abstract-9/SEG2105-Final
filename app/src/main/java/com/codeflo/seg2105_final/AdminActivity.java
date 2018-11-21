package com.codeflo.seg2105_final;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    public void update(){
        database = FirebaseFirestore.getInstance();
        database.collection("Services").get().addOnCompleteListener(updateListener);
    }

    public OnCompleteListener<QuerySnapshot> updateListener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.isSuccessful()){
                ArrayList<Service> serviceList = new ArrayList<>();
                for(DocumentSnapshot doc : task.getResult().getDocuments()){
                    serviceList.add(new Service(doc.getId(), Double.parseDouble((String) doc.get("rate")), null));

                    ListView listServices = (ListView)findViewById(R.id.serviceList);
                    ServiceAdapter serviceAdapter = new ServiceAdapter(AdminActivity.this, serviceList, null, 0);
                    listServices.setAdapter(serviceAdapter);
                }
            }
        }
    };

    public void addService(View v){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database = FirebaseFirestore.getInstance();
                Map<String, String> service = new HashMap<>();



                TextView rate = (TextView) ((AlertDialog) dialog).findViewById(R.id.createRate);
                TextView name = (TextView) ((AlertDialog) dialog).findViewById(R.id.createName);

                if(!rate.getText().toString().equals("") && !name.getText().toString().equals("")) {
                    service.put("rate", rate.getText().toString());
                    database.collection("Services").document(name.getText().toString()).set(service);

                    Toast.makeText(getApplicationContext(), "Service Successfully Created", Toast.LENGTH_LONG).show();
                    update();
                }
                dialog.dismiss();
            }
        });

        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogBuilder.setView(R.layout.dialog_create_service);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

}
