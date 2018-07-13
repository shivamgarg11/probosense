package com.shivam.probussense.Classes;

public class swimmingpools {

    String organizationname;
    String Name;
    String address;
    double Ph;
    double Chlorine;
    double temp;
    String datetime;
    double hubid;


    public swimmingpools(String organizationname,String name, String address, double ph, double chlorine, double temp, String datetime, double hubid) {
        this.organizationname=organizationname;
        Name = name;
        this.address = address;
        Ph = ph;
        Chlorine = chlorine;
        this.temp = temp;
        this.datetime = datetime;
        this.hubid = hubid;
    }

    public String getOrganizationname() {
        return organizationname;
    }

    public double getTemp() {
        return temp;
    }

    public String getDatetime() {
        return datetime;
    }

    public double getHubid() {
        return hubid;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return address;
    }

    public double getPh() {
        return Ph;
    }

    public double getChlorine() {
        return Chlorine;
    }

}

