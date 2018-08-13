package com.example.iujital.popularmovies.presentation;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iujital.popularmovies.BuildConfig;
import com.example.iujital.popularmovies.R;
import com.example.iujital.popularmovies.adapter.MovieViewAdapter;
import com.example.iujital.popularmovies.api.Client;
import com.example.iujital.popularmovies.api.MovieDbService;
import com.example.iujital.popularmovies.model.Movie;
import com.example.iujital.popularmovies.model.MovieResponse;
import com.example.iujital.popularmovies.utils.Connectivity;
import com.example.iujital.popularmovies.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListMoviesActivity extends AppCompatActivity {
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;
    @BindView(R.id.tv_type_search)
    TextView mTypeSearch;
    @BindView(R.id.view_error)
    LinearLayout mViewError;
    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;
    @BindView(R.id.view_movies)
    LinearLayout mViewMovies;

    private static final String TAG = ListMoviesActivity.class.getSimpleName();
    private static final int NUM_SPAN = 3;
    private int typeSearch = Constants.POPULAR;
    private MovieViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movies);
        ButterKnife.bind(this);
        setupRecyclerView();
        validateTypeSearch();
    }

    private void setupRecyclerView() {
        mAdapter = new MovieViewAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, NUM_SPAN);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void validateTypeSearch() {
        switch (typeSearch) {
            case Constants.POPULAR:
                createRetrofit(Constants.TYPE_SEARCH_POPULAR);
                break;
            case Constants.TOP_RATED:
                createRetrofit(Constants.TYPE_SEARCH_TOP_RATED);
                break;
            case Constants.FAVORITES:
                setupViewModel();
                break;
        }
    }

    private void setupViewModel() {
        ListMoviesViewModel viewModel = ViewModelProviders.of(this).get(ListMoviesViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (movies != null && !movies.isEmpty()) {
                    mAdapter.setMovieList(movies);
                    mTypeSearch.setText(R.string.favorite_title);
                    showListMovieView();
                }else {
                    mErrorMessageDisplay.setText(R.string.error_message_without_favorites);
                    showErrorMessage();
                }
            }
        });
    }


    private void createRetrofit(final String typeSearchText) {
        if(typeSearch == Constants.TOP_RATED){
            mTypeSearch.setText(R.string.top_rated_title);
        }else if(typeSearch == Constants.POPULAR){
            mTypeSearch.setText(R.string.popular_title);
        }
        startLoading();
        if (Connectivity.isConnected(this)) {
            if (BuildConfig.api_key.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please obtain API Key firstly from themoviedb.org", Toast.LENGTH_SHORT).show();
                endLoading();
                return;
            }

            Retrofit retrofit = Client.getClient();
            MovieDbService service = retrofit.create(MovieDbService.class);

            Call<MovieResponse> requestMovies = service.listMovies(typeSearchText, BuildConfig.api_key);

            requestMovies.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    if (response.isSuccessful()) {
                        mAdapter.setMovieList(response.body().getResults());
                        endLoading();
                        showListMovieView();
                    } else {
                        Log.e(TAG, "erro: " + response.code());
                        endLoading();
                        showErrorMessage();
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Log.e(TAG, "erro: " + t.getMessage());
                    endLoading();
                    showErrorMessage();
                }
            });
        } else {
            endLoading();
            mErrorMessageDisplay.setText(R.string.error_message_connection);
            showErrorMessage();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem itemPopular = menu.add("Order by popularity");
        itemPopular.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                typeSearch = Constants.POPULAR;
                validateTypeSearch();
                return false;
            }
        });

        MenuItem itemTopRated = menu.add("Order by top rated");
        itemTopRated.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                typeSearch = Constants.TOP_RATED;
                validateTypeSearch();
                return false;
            }
        });

        MenuItem itemFavorites = menu.add("Order by favorites");
        itemFavorites.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                typeSearch = Constants.FAVORITES;
                validateTypeSearch();
                return false;
            }
        });
        return true;
    }

    private void startLoading() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void endLoading() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    private void showErrorMessage() {
        mViewMovies.setVisibility(View.GONE);
        mViewError.setVisibility(View.VISIBLE);
    }

    private void showListMovieView() {
        mViewMovies.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
    }

}
