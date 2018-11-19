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

import com.codeflo.seg2105_final.AdminActivity;
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

    public ServiceAdapter(Context context, ArrayList<Service> serviceList){
        super(context, 0, serviceList);
        mContext = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if(listItem == null) listItem = inflater.inflate(R.layout.service_item, parent, false);

        Service current = serviceList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.editName);
        name.setText(current.getName());

        TextView rate = (TextView) listItem.findViewById(R.id.rate);
        String tmp = "Rate: $" + String.valueOf(current.getRate());
        if(tmp.split("\\.")[1].length()<2) tmp+="0";
        rate.setText(tmp);

        listItem.findViewById(R.id.adminButton).setOnClickListener(listener);

        return listItem;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
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
}
