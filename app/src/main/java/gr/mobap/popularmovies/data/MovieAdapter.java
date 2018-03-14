package gr.mobap.popularmovies.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import gr.mobap.popularmovies.R;
import gr.mobap.popularmovies.model.MovieObject;

import static gr.mobap.popularmovies.data.MoviePreferences.base_image_url;

// Our Adapter used to bind our Movies data that are displayed in the RecyclerView

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    final private PosterClickListener mPosterClickListener;
    private final Context mContext;
    private ArrayList<MovieObject> mMovieData;
    private final String imageSize;

    public MovieAdapter(Context context, PosterClickListener mPosterClickListener) {
        this.mContext = context;
        this.mPosterClickListener = mPosterClickListener;
        String[] imageSizes = context.getResources().getStringArray(R.array.image_sizes);
        this.imageSize = imageSizes[3];
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        String posterPath = new StringBuilder(base_image_url)
                .append(imageSize)
                .append(mMovieData.get(position).getPoster_path())
                .toString();

        Picasso.with(mContext)
                .load(posterPath)
                .fit()
                .into(holder.posterImageView);

    }

    public void swapMovieData(ArrayList<MovieObject> movieData) {
        this.mMovieData = movieData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMovieData == null ? 0 : mMovieData.size();
    }

    public interface PosterClickListener {
        void onPosterClicked(MovieObject movie);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster)
        ImageView posterImageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            posterImageView.setOnClickListener(v -> {
                int clickedPosition = getAdapterPosition();
                mPosterClickListener.onPosterClicked(mMovieData.get(clickedPosition));
            });
        }
    }

}
