package com.example.iujital.popularmovies.ui.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.iujital.popularmovies.R;
import com.example.iujital.popularmovies.adapter.MovieViewAdapter;
import com.example.iujital.popularmovies.api.Api;
import com.example.iujital.popularmovies.model.Movie;
import com.example.iujital.popularmovies.model.MovieCatalog;
import com.example.iujital.popularmovies.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListMoviesActivity extends AppCompatActivity {

    private static final String TAG = ListMoviesActivity.class.getSimpleName();
    private static final int NUM_SPAN = 3;
    private RecyclerView mRecyclerView;
    private MovieViewAdapter mAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movies);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        mAdapter = new MovieViewAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, NUM_SPAN);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

        createRetrofit(Constants.TYPE_SEARCH_POPULAR);

    }


    private void createRetrofit(String typeSearch) {
        startLoading();
        if(checkConnection()){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Api service = retrofit.create(Api.class);

            Call<MovieCatalog> requestMovies = service.listMovies(typeSearch, Constants.TMDB_API_KEY);

            requestMovies.enqueue(new Callback<MovieCatalog>() {
                @Override public void onResponse(Call<MovieCatalog> call, Response<MovieCatalog> response) {
                    if(response.isSuccessful()){
                        mAdapter.setmMovieList(response.body().getResults());
                        endLoading();
                        showListMovieView();
                    }else {
                        Log.e(TAG, "erro: " + response.code());
                        endLoading();
                        showErrorMessage();
                    }
                }

                @Override public void onFailure(Call<MovieCatalog> call, Throwable t) {
                    Log.e(TAG, "erro: " + t.getMessage());
                    endLoading();
                    showErrorMessage();
                }
            });
        }else{
            endLoading();
            mErrorMessageDisplay.setText(R.string.error_message_connection);
            showErrorMessage();
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem itemPopular = menu.add("Order by popularity");
        itemPopular.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                createRetrofit(Constants.TYPE_SEARCH_POPULAR);
                return false;
            }
        });

        MenuItem itemTopRated = menu.add("Order by top rated");
        itemTopRated.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                createRetrofit(Constants.TYPE_SEARCH_TOP_RATED);
                return false;
            }
        });
        return true;
    }

    public  boolean checkConnection() {
        boolean connected;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            connected = true;
        } else {
            connected = false;
        }
        return connected;
    }

    private void startLoading(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void endLoading(){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showListMovieView(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

}
