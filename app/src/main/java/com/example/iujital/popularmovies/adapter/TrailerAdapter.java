package com.example.iujital.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iujital.popularmovies.R;
import com.example.iujital.popularmovies.model.Trailer;
import com.example.iujital.popularmovies.utils.Constants;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{
    private Context mContext ;
    private List<Trailer> mTrailerList;

    public TrailerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setTrailerList(List<Trailer> trailerList) {
        this.mTrailerList = trailerList;
        notifyDataSetChanged();
    }

    @Override public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForList = R.layout.card_item_trailer;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(layoutIdForList, parent, shouldAttachToParentImmediately);
        TrailerAdapter.TrailerViewHolder viewHolder = new TrailerAdapter.TrailerViewHolder(view);

        return viewHolder;
    }

    @Override public void onBindViewHolder(TrailerAdapter.TrailerViewHolder holder, final int position) {
        holder.listItemTitleView.setText(mTrailerList.get(position).getName());
        final Uri uri = Uri.parse(Constants.BASE_URL_YOUTUBE + mTrailerList.get(position).getKey());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("VIDEO_ID", mTrailerList.get(position).getKey());
                mContext.startActivity(intent);
            }
        });
    }

    @Override public int getItemCount() {
        if(mTrailerList != null){
            return mTrailerList.size();
        }
        return 0;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder{
        TextView listItemTitleView;
        CardView cardView;

        TrailerViewHolder(View itemView) {
            super(itemView);

            listItemTitleView = itemView.findViewById(R.id.tv_trailer_title);
            cardView = itemView.findViewById(R.id.cv_item_trailer);
        }
    }
}
