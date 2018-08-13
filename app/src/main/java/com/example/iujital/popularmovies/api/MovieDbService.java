package com.example.iujital.popularmovies.api;


import com.example.iujital.popularmovies.model.MovieResponse;
import com.example.iujital.popularmovies.model.ReviewResponse;
import com.example.iujital.popularmovies.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDbService {
    @GET("{search_type}") Call<MovieResponse> listMovies(@Path("search_type") String searchType, @Query("api_key") String
            apiKey);

    @GET("{id}/reviews") Call<ReviewResponse> listReviews(@Path("id") int id, @Query("api_key") String
            apiKey);

    @GET("{id}/videos") Call<TrailerResponse> listTrailers(@Path("id") int id, @Query("api_key") String
            apiKey);
}
