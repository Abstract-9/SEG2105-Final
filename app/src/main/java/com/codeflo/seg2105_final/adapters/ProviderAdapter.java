package com.codeflo.seg2105_final.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codeflo.seg2105_final.R;
import com.codeflo.seg2105_final.models.ServiceProvider;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProviderAdapter extends ArrayAdapter<ServiceProvider> {
    private Context mContext;
    private ArrayList<ServiceProvider> serviceList;
    private String username;
    private int addMode;

    public ProviderAdapter(Context context, ArrayList<ServiceProvider> serviceList){
        super(context, 0, serviceList);
        mContext = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        ServiceProvider current = serviceList.get(position);

        if(listItem == null) listItem = inflater.inflate(R.layout.service_item_list, parent, false );

        TextView name = (TextView)listItem.findViewById(R.id.name);
        TextView rate = (TextView)listItem.findViewById(R.id.rate);
        TextView address = (TextView)listItem.findViewById(R.id.address);

        name.setText(current.getName());
        rate.setText("Rate: " + String.valueOf(current.getRate()));
        address.setText("Address: " + current.getAddress());

        return listItem;
    }
}
