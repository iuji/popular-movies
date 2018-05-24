package com.example.iujital.popularmovies.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iujital.popularmovies.R;
import com.example.iujital.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView originalTitleText = (TextView) findViewById(R.id.tv_movie_original_title);
        ImageView imagePoster = (ImageView) findViewById(R.id.iv_movie_img_detail);
        TextView overviewText = (TextView) findViewById(R.id.tv_movie_overview);
        TextView voteAverageText = (TextView) findViewById(R.id.tv_movie_vote_average);
        TextView releaseDateText = (TextView) findViewById(R.id.tv_release_date);

        Intent intent = getIntent();

        String title = intent.getStringExtra("ORIGINAL_TITLE");
        String posterPath = intent.getStringExtra("POSTER");
        String urlPosterPath = Constants.BASE_URL_IMAGE + posterPath;
        String overview = intent.getStringExtra("OVERVIEW");
        String voteAverage = intent.getStringExtra("VOTE_AVERAGE");
        String releaseDate = intent.getStringExtra("RELEASE_DATE");

        originalTitleText.setText(title);
        Picasso.with(this).load(urlPosterPath).into(imagePoster);
        overviewText.setText(overview);
        voteAverageText.setText(voteAverage);
        releaseDateText.setText(releaseDate);

    }
}
