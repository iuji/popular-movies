package com.example.iujital.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResponse {

    @SerializedName("id")
    private int idTrailer;
    @SerializedName("results")
    private List<Trailer> results;

    public int getIdTrailer(){
        return idTrailer;
    }

    public void seIdTrailer(int id_trailer){
        this.idTrailer = id_trailer;
    }

    public List<Trailer> getResults(){
        return results;
    }
}