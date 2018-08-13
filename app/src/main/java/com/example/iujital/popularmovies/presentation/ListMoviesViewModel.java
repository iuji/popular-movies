package com.example.iujital.popularmovies.presentation;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.iujital.popularmovies.database.AppDatabase;
import com.example.iujital.popularmovies.model.Movie;

import java.util.List;

public class ListMoviesViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> movies;

    public ListMoviesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        movies = database.favoriteDao().loadAllFavorites();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
