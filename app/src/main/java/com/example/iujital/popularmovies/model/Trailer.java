package com.example.iujital.popularmovies.model;

import org.parceler.Parcel;

@Parcel(Parcel.Serialization.BEAN)
public class Trailer {

    private String key;
    private String name;

    public Trailer(){
    }

    public String getKey(){
        return key;
    }

    public void setKey(String key){
        this.key = key;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}