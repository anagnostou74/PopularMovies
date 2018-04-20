package gr.mobap.popularmovies.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gr.mobap.popularmovies.R;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private final Context context;
    private final ArrayList<String> trailers;
    private final ArrayList<String> trailersName;

    public TrailerAdapter(Context context, ArrayList<String> trailers, ArrayList<String> trailersName) {
        this.context = context;
        this.trailers = trailers;
        this.trailersName = trailersName;
    }

    @NonNull
    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_view_element, parent, false);
        return new TrailerAdapter.TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TrailerViewHolder holder, int position) {

        holder.trailerTitle.setText(trailersName.get(position));

        holder.cardView.setOnClickListener(view -> {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailers.get(holder.getAdapterPosition())));
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailers.get(holder.getAdapterPosition())));
            try {
                context.startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                context.startActivity(webIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        final TextView trailerTitle;
        final CardView cardView;

        TrailerViewHolder(View view) {
            super(view);
            trailerTitle = view.findViewById(R.id.trailer_title);
            cardView = view.findViewById(R.id.trailer_card_view);
        }
    }
}