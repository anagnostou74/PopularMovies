package gr.mobap.popularmovies.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gr.mobap.popularmovies.R;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private final ArrayList<String> review;
    private final ArrayList<String> reviewer;

    public ReviewsAdapter(ArrayList<String> review, ArrayList<String> reviewer) {
        this.review = review;
        this.reviewer = reviewer;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_view_element, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        Resources res = holder.itemView.getResources();
        String text = String.format(res.getString(R.string.user_name_review), reviewer.get(position));

        holder.reviewTitle.setText(text);
        holder.reviewDesc.setText(review.get(position));

    }

    @Override
    public int getItemCount() {
        return review.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        final TextView reviewTitle;
        final TextView reviewDesc;

        ReviewViewHolder(View view) {
            super(view);
            reviewTitle = view.findViewById(R.id.review_title);
            reviewDesc = view.findViewById(R.id.review_desc);
        }

    }

}
