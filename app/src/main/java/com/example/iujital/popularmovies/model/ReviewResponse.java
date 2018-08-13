package com.example.iujital.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResponse {
    @SerializedName("id")
    private int idReview;
    @SerializedName("results")
    private List<Review> results;

    public int getIdReview(){
        return idReview;
    }

    public void seIdTrailer(int id_trailer){
        this.idReview = id_trailer;
    }

    public List<Review> getResults(){
        return results;
    }
}
