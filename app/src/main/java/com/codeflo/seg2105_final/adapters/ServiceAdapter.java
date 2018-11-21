package com.codeflo.seg2105_final.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeflo.seg2105_final.R;
import com.codeflo.seg2105_final.models.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServiceAdapter extends ArrayAdapter<Service> {

    private Context mContext;
    private ArrayList<Service> serviceList;
    private String username;
    private int addMode;
    private DialogInterface parentDialog;

    public ServiceAdapter(Context context, ArrayList<Service> serviceList, @Nullable String username, int addMode){
        super(context, 0, serviceList);
        mContext = context;
        this.serviceList = serviceList;
        this.username = username;
        this.addMode = addMode;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if(listItem == null && username==null)
            listItem = inflater.inflate(R.layout.service_item, parent, false);
        else if(listItem == null && addMode==0)
            listItem = inflater.inflate(R.layout.service_item_provider, parent, false);
        else if(listItem==null)
            listItem = inflater.inflate(R.layout.service_item_plain, parent, false);



        Service current = serviceList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.editName);
        name.setText(current.getName());

        TextView rate = (TextView) listItem.findViewById(R.id.rate);
        String tmp = "Rate: $" + String.valueOf(current.getRate());
        if(tmp.split("\\.")[1].length()<2) tmp+="0";
        rate.setText(tmp);

        if(username==null) listItem.findViewById(R.id.adminButton).setOnClickListener(adminListener);
        else if(addMode==0) listItem.findViewById(R.id.adminButton).setOnClickListener(providerListener);
        else if(addMode==1) listItem.findViewById(R.id.adminButton).setOnClickListener(addProviderServiceListener);

        return listItem;
    }

    private View.OnClickListener adminListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewGroup container = (ViewGroup) v.getParent();
            ListView parent = (ListView) container.getParent();
            final TextView oldName = (TextView) container.getChildAt(0);
            TextView rate = (TextView) container.getChildAt(1);



            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);

            dialogBuilder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    Map<String, String> service = new HashMap<>();

                    TextView rate = (TextView) ((AlertDialog) dialog).findViewById(R.id.editRate);
                    TextView name = (TextView) ((AlertDialog) dialog).findViewById(R.id.editName);

                    if(!rate.getText().toString().equals("") && !name.getText().toString().equals("")) {
                        service.put("rate", rate.getText().toString());
                        database.collection("Services").document(oldName.getText().toString()).delete();
                        database.collection("Services").document(name.getText().toString()).set(service);

                        Toast.makeText(mContext, "Service Successfully Edited", Toast.LENGTH_LONG).show();
                    }
                    update();
                    dialog.dismiss();
                }
            });

            dialogBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialogBuilder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    TextView name = (TextView) ((AlertDialog) dialog).findViewById(R.id.editName);

                    database.collection("Services").document(name.getText().toString()).delete();
                    update();
                }
            });

            dialogBuilder.setView(R.layout.dialog_edit_service);

            AlertDialog dialog = dialogBuilder.create();

            dialog.show();

            ((EditText) dialog.findViewById(R.id.editName)).setText(oldName.getText().toString());
            ((EditText) dialog.findViewById(R.id.editRate)).setText(rate.getText().toString()
                    .replace("Rate: $", ""));

        }
    };

    private View.OnClickListener providerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewGroup container = (ViewGroup) v.getParent();
            ListView parent = (ListView) container.getParent();
            String name = ((TextView) container.getChildAt(0)).getText().toString();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            for(Service s : serviceList){
                if(s.getName().equals(name)) serviceList.remove(s);

                db.collection("users").document(username)
                        .collection("Services").document(name).delete();
            }

            Toast.makeText(mContext, "Service Successfully Deleted", Toast.LENGTH_LONG).show();

            notifyDataSetChanged();
        }
    };

    private View.OnClickListener addProviderServiceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewGroup container = (ViewGroup) v.getParent();
            ListView parent = (ListView) container.getParent();
            String name = ((TextView) container.getChildAt(0)).getText().toString();
            double rate = 0.00;
            for(Service s : serviceList){
                if(s.getName().equals(name)) rate = s.getRate();
            }


            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, String> reference = new HashMap<>();
            reference.put("rate", String.valueOf(rate));

            db.collection("users").document(username)
                    .collection("Services").document(name).set(reference);
            parentDialog.dismiss();
        }
    };

    private void update(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Services").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    serviceList.clear();
                    for(DocumentSnapshot doc : task.getResult().getDocuments()){
                        serviceList.add(new Service(doc.getId(), Double.parseDouble((String) doc.get("rate")), null));
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }

    public Service getService(int index){
        return serviceList.get(index);
    }

    public void setParentDialog(DialogInterface dialog){
        this.parentDialog = dialog;
    }
}
