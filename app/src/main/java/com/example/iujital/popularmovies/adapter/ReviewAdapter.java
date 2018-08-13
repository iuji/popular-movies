package com.example.iujital.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iujital.popularmovies.R;
import com.example.iujital.popularmovies.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{
    private Context mContext ;
    private List<Review> mReviewList;

    public ReviewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setReviewList(List<Review> mReviewList) {
        this.mReviewList = mReviewList;
        notifyDataSetChanged();
    }

    @Override public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForList = R.layout.card_item_review;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(layoutIdForList, parent, shouldAttachToParentImmediately);
        ReviewAdapter.ReviewViewHolder viewHolder = new ReviewAdapter.ReviewViewHolder(view);

        return viewHolder;
    }

    @Override public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, final int position) {
        holder.listItemAuthor.setText(mReviewList.get(position).getAuthor());
        holder.listItemComent.setText(mReviewList.get(position).getContent());
    }

    @Override public int getItemCount() {
        if(mReviewList != null){
            return mReviewList.size();
        }
        return 0;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView listItemAuthor;
        TextView listItemComent;

        ReviewViewHolder(View itemView) {
            super(itemView);

            listItemAuthor = itemView.findViewById(R.id.tv_review_author);
            listItemComent = itemView.findViewById(R.id.tv_review_coment);
        }
    }
}
