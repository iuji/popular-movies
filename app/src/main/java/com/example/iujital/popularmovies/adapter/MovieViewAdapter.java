package com.example.iujital.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iujital.popularmovies.R;
import com.example.iujital.popularmovies.model.Movie;
import com.example.iujital.popularmovies.presentation.DetailsActivity;
import com.example.iujital.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewAdapter.MovieViewHolder>{
    private Context mContext ;
    private List<Movie> mMovieList;

    public MovieViewAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public void setMovieList(List<Movie> mMovieList) {
        this.mMovieList = mMovieList;
        notifyDataSetChanged();
    }

    public List<Movie> getmMovieList() {
        return mMovieList;
    }

    @Override public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListMovie = R.layout.card_item_movie;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(layoutIdForListMovie, parent, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.listItemTitleView.setText(mMovieList.get(position).getTitle());
        String url = Constants.BASE_URL_IMAGE + mMovieList.get(position).getPoster_path();
        Picasso.with(mContext).load(url).placeholder(R.drawable.placeholder).into(holder.listItemImageView);



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                Movie movie = mMovieList.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("movie", Parcels.wrap(movie));

                // passing bundle to details activity
                intent.putExtras(bundle);

                // start the activity
                mContext.startActivity(intent);
            }
        });
    }

    @Override public int getItemCount() {
        if(mMovieList != null){
            return mMovieList.size();
        }
        return 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder{
        TextView listItemTitleView;
        ImageView listItemImageView;
        CardView cardView;

        MovieViewHolder(View itemView) {
            super(itemView);

            listItemTitleView = itemView.findViewById(R.id.tv_movie_title);
            listItemImageView = itemView.findViewById(R.id.iv_movie_img);
            cardView = itemView.findViewById(R.id.cv_item_movie);
        }
    }
}

