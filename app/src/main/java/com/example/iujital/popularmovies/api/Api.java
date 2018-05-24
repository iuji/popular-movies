package com.example.iujital.popularmovies.api;


import com.example.iujital.popularmovies.model.MovieCatalog;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    @GET("{search_type}") Call<MovieCatalog> listMovies(@Path("search_type") String searchType, @Query("api_key") String
            apiKey);

}
