package com.codeflo.seg2105_final.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codeflo.seg2105_final.models.Service;

import java.util.ArrayList;

public class ServiceAdapter extends ArrayAdapter<Service> {

    private Context mContext;
    private ArrayList<Service> serviceList;

    ServiceAdapter(Context context, ArrayList<Service> serviceList){
        super(context, 0, serviceList);
        mContext = context;
        this.serviceList = serviceList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
