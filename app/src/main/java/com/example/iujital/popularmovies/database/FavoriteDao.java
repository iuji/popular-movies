package com.example.iujital.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.iujital.popularmovies.model.Movie;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    LiveData<List<Movie>> loadAllFavorites();

    @Insert
    void insertFavorite(Movie favoriteEntry);

    @Delete
    void deleteFavorite(Movie favoriteEntry);

    @Query("SELECT * FROM favorite WHERE id = :id")
    LiveData<Movie> loadMovieById(int id);

}
