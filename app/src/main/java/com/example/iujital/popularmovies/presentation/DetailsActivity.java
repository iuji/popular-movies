package com.example.iujital.popularmovies.presentation;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.iujital.popularmovies.BuildConfig;
import com.example.iujital.popularmovies.R;
import com.example.iujital.popularmovies.adapter.ReviewAdapter;
import com.example.iujital.popularmovies.adapter.TrailerAdapter;
import com.example.iujital.popularmovies.database.AppDatabase;
import com.example.iujital.popularmovies.model.Movie;
import com.example.iujital.popularmovies.model.MovieResponse;
import com.example.iujital.popularmovies.api.MovieDbService;
import com.example.iujital.popularmovies.api.Client;
import com.example.iujital.popularmovies.model.Review;
import com.example.iujital.popularmovies.model.ReviewResponse;
import com.example.iujital.popularmovies.model.Trailer;
import com.example.iujital.popularmovies.model.TrailerResponse;
import com.example.iujital.popularmovies.utils.AppExecutors;
import com.example.iujital.popularmovies.utils.Connectivity;
import com.example.iujital.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.iv_movie_img_detail)
    ImageView imagePoster;
    @BindView(R.id.iv_movie_background)
    ImageView backgroundPoster;
    @BindView(R.id.image_favorite)
    ImageView imageFavorite;
    @BindView(R.id.tv_movie_original_title)
    TextView originalTitleText;
    @BindView(R.id.tv_movie_overview)
    TextView overviewText;
    @BindView(R.id.tv_movie_vote_average)
    TextView voteAverageText;
    @BindView(R.id.tv_release_date)
    TextView releaseDateText;
    @BindView(R.id.recycler_trailers)
    RecyclerView mRecyclerTrailers;
    @BindView(R.id.recycler_reviews)
    RecyclerView mRecyclerReviews;
    @BindView(R.id.view_error_trailer)
    LinearLayout mViewErrorTrailer;
    @BindView(R.id.view_error_review)
    LinearLayout mViewErrorReview;
    @BindView(R.id.tv_error_trailer)
    TextView mTextErrorTrailer;
    @BindView(R.id.tv_error_review)
    TextView mTextErrorReview;

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";
    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private static final String TRAILERS_KEY = "trailers";
    private static final String REVIEWS_KEY = "reviews";
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private Movie mMovie;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private boolean isSaved;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        mDb = AppDatabase.getInstance(getApplicationContext());
        if (getIntent().getExtras() != null) {
            mMovie = Parcels.unwrap(getIntent().getExtras().getParcelable("movie"));
        }

        setupRecyclerView();
        setupViewModel();

        if (savedInstanceState != null
                && savedInstanceState.containsKey(TRAILERS_KEY)
                && savedInstanceState.containsKey(REVIEWS_KEY)) {
            List<Trailer> trailers = Parcels.unwrap(savedInstanceState.getParcelable(TRAILERS_KEY));
            List<Review> reviews = Parcels.unwrap(savedInstanceState.getParcelable(REVIEWS_KEY));
            mTrailerAdapter.setTrailerList(trailers);
            showListTrailerView();
            mReviewAdapter.setReviewList(reviews);
            showListReviewView();
        } else {
            requestTrailers();
            requestReviews();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TRAILERS_KEY, Parcels.wrap(mTrailerAdapter.getmTrailerList()));
        outState.putParcelable(REVIEWS_KEY, Parcels.wrap(mReviewAdapter.getmReviewList()));
    }

    private void setupRecyclerView() {
        mTrailerAdapter = new TrailerAdapter(this);
        LinearLayoutManager layoutManagerHorizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerTrailers.setAdapter(mTrailerAdapter);
        mRecyclerTrailers.setHasFixedSize(true);
        mRecyclerTrailers.setLayoutManager(layoutManagerHorizontal);

        mReviewAdapter = new ReviewAdapter(this);
        LinearLayoutManager layoutManagerVertical = new LinearLayoutManager(this);
        mRecyclerReviews.setAdapter(mReviewAdapter);
        mRecyclerReviews.setHasFixedSize(true);
        mRecyclerReviews.setLayoutManager(layoutManagerVertical);
    }

    @OnClick(R.id.image_favorite)
    void onImageFavoriteClick() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isSaved) {
                    mDb.favoriteDao().deleteFavorite(mMovie);
                } else {
                    mDb.favoriteDao().insertFavorite(mMovie);
                }
            }
        });
    }

    @OnClick(R.id.icon_back)
    void onBackButtonClick() {
        super.onBackPressed();
    }

    private void requestTrailers() {
        if (Connectivity.isConnected(this)) {
            Retrofit retrofit = Client.getClient();
            MovieDbService service = retrofit.create(MovieDbService.class);

            Call<TrailerResponse> requestTrailers = service.listTrailers(mMovie.getId(), BuildConfig.api_key);

            requestTrailers.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getResults().size() > 0) {
                            mTrailerAdapter.setTrailerList(response.body().getResults());
                            showListTrailerView();
                        }else{
                            mTextErrorTrailer.setText(R.string.error_message_without_trailers);
                            showErrorTrailerMessage();
                        }
                    } else {
                        Log.e(TAG, "erro: " + response.code());
                        showErrorTrailerMessage();
                    }
                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {
                    Log.e(TAG, "erro: " + t.getMessage());
                    showErrorTrailerMessage();
                }
            });
        } else {
            showErrorTrailerMessage();
            mTextErrorTrailer.setText(R.string.error_message_connection);
        }
    }

    private void requestReviews() {
        if (Connectivity.isConnected(this)) {
            Retrofit retrofit = Client.getClient();
            MovieDbService service = retrofit.create(MovieDbService.class);

            Call<ReviewResponse> requestReviews = service.listReviews(mMovie.getId(), BuildConfig.api_key);

            requestReviews.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getResults().size() > 0) {
                            mReviewAdapter.setReviewList(response.body().getResults());
                            showListReviewView();
                        } else {
                            mTextErrorReview.setText(R.string.error_message_without_reviews);
                            showErrorReviewMessage();
                        }
                    } else {
                        Log.e(TAG, "erro: " + response.code());
                        showErrorReviewMessage();
                    }
                }

                @Override
                public void onFailure(Call<ReviewResponse> call, Throwable t) {
                    Log.e(TAG, "erro: " + t.getMessage());
                    showErrorReviewMessage();
                }
            });
        } else {
            showErrorReviewMessage();
            mTextErrorReview.setText(R.string.error_message_connection);
        }
    }


    private void setupViewModel() {
        DetailsMovieViewFactory factory = new DetailsMovieViewFactory(mDb, mMovie.getId());
        final DetailsViewModel viewModel
                = ViewModelProviders.of(this, factory).get(DetailsViewModel.class);

        viewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null) {
                    isSaved = true;
                    populateUI(movie);
                } else {
                    isSaved = false;
                    populateUI(mMovie);
                }
            }
        });
    }

    private void populateUI(Movie movie) {
        String urlPosterPath = Constants.BASE_URL_IMAGE + movie.getPoster_path();
        String urlBackgroundPosterPath = Constants.BASE_URL_IMAGE_BG + movie.getBackdrop_path();
        String releaseDate = dateFormat.format(movie.getRelease_date());

        originalTitleText.setText(movie.getOriginal_title());
        Picasso.with(this).load(urlPosterPath).placeholder(R.drawable.placeholder).into(imagePoster);
        Picasso.with(this).load(urlBackgroundPosterPath).into(backgroundPoster);
        overviewText.setText(movie.getOverview());
        voteAverageText.setText(movie.getVote_average());
        releaseDateText.setText(releaseDate);
        if (isSaved) {
            imageFavorite.setImageResource(R.drawable.ic_baseline_favorite);
        } else {
            imageFavorite.setImageResource(R.drawable.ic_baseline_favorite_border);
        }
    }

    private void showErrorTrailerMessage() {
        mRecyclerTrailers.setVisibility(View.GONE);
        mViewErrorTrailer.setVisibility(View.VISIBLE);
    }

    private void showListTrailerView() {
        mRecyclerTrailers.setVisibility(View.VISIBLE);
        mViewErrorTrailer.setVisibility(View.GONE);
    }

    private void showErrorReviewMessage() {
        mRecyclerReviews.setVisibility(View.GONE);
        mViewErrorReview.setVisibility(View.VISIBLE);
    }

    private void showListReviewView() {
        mRecyclerReviews.setVisibility(View.VISIBLE);
        mViewErrorReview.setVisibility(View.GONE);
    }
}

