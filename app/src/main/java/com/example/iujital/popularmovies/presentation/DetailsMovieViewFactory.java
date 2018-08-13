package com.example.iujital.popularmovies.presentation;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.iujital.popularmovies.database.AppDatabase;

public class DetailsMovieViewFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mMovieId;

    public DetailsMovieViewFactory(AppDatabase database, int taskId) {
        mDb = database;
        mMovieId = taskId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailsViewModel(mDb, mMovieId);
    }
}
