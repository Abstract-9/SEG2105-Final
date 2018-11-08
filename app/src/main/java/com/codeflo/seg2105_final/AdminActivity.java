package com.codeflo.seg2105_final;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.codeflo.seg2105_final.adapters.ServiceAdapter;
import com.codeflo.seg2105_final.models.Service;

import java.util.ArrayList;

public class AdminActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ListView listServices = (ListView)findViewById(R.id.serviceList);
        ServiceAdapter serviceAdapter = new ServiceAdapter(getApplicationContext(), new ArrayList<Service>());
        listServices.setAdapter(serviceAdapter);


    }


}
