package com.example.iujital.popularmovies.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.iujital.popularmovies.database.AppDatabase;
import com.example.iujital.popularmovies.model.Movie;

public class DetailsViewModel extends ViewModel {
    private LiveData<Movie> movie;

    public DetailsViewModel(AppDatabase database, int movieId) {
        movie = database.favoriteDao().loadMovieById(movieId);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }
}
