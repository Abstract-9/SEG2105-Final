package com.codeflo.seg2105_final.models;

import java.util.ArrayList;

public class Service {
    String name;
    int rate;
    ArrayList<Service> children;

    Service(String name, int rate, ArrayList<Service> children){
        this.name = name;
        this.rate = rate;
        this.children = children;
    }

    Service(String name, int rate){
        this.name = name;
        this.rate = rate;
    }



}
