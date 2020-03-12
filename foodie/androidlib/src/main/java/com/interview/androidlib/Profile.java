package com.interview.androidlib;

public class Profile {
    String address;
    String addressTitle;

    public Profile(String address, String addressTitle) {
        this.address = address;
        this.addressTitle = addressTitle;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressTitle() {
        return addressTitle;
    }
}
