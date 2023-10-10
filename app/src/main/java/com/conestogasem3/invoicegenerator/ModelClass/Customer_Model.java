package com.conestogasem3.invoicegenerator.ModelClass;

import java.io.Serializable;



public class Customer_Model implements Serializable {

    String IDcustomer, Customer_Name, Email, Gst, Address, City, State, Pincode;

    public String getIDcustomer() {
        return IDcustomer;
    }

    public void setIDcustomer(String IDcustomer) {
        this.IDcustomer = IDcustomer;
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGst() {
        return Gst;
    }

    public void setGst(String gst) {
        Gst = gst;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }
}
