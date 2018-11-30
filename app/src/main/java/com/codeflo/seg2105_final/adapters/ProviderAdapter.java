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

        ((TextView)listItem.findViewById(R.id.name)).setText(current.getName());
        ((TextView)listItem.findViewById(R.id.rate)).setText(current.getRate());
        ((TextView)listItem.findViewById(R.id.address)).setText(current.getAddress());

        return listItem;
    }
}
