package com.example.iujital.popularmovies.model;

import java.util.List;

public class MovieResponse {
    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
