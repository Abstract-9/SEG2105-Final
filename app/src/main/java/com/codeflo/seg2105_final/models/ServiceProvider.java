package com.codeflo.seg2105_final.models;

public class ServiceProvider {
    String name, address;
    int rate;

    public ServiceProvider(String name, String address, int rate){
        this.name = name;
        this.address = address;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
