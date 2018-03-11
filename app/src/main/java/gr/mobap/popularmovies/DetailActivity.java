package gr.mobap.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import gr.mobap.popularmovies.model.MovieObject;

import static gr.mobap.popularmovies.data.MoviePreferences.base_image_url;
// The Detail Activity responsible to show Title, release date, rating, overview and to display poster
public class DetailActivity extends AppCompatActivity {

    private static final String MOVIE_EXTRA = "MOVIE";
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView ivPosterHeader = findViewById(R.id.activity_detail_header);
        TextView tvDisplayData = findViewById(R.id.activity_detail_title);
        ImageView ivPoster = findViewById(R.id.activity_detail_poster);
        TextView tvReleaseDate = findViewById(R.id.activity_detail_release_date);
        TextView tvRating = findViewById(R.id.activity_detail_rating);
        TextView tvOverview = findViewById(R.id.activity_detail_overview);
        setColor();
        CardView cardView = findViewById(R.id.card_view);
        cardView.setCardBackgroundColor(getColorCrtWay());
        Intent intentDetailActivity = getIntent();

        if (intentDetailActivity != null) {
            if (intentDetailActivity.hasExtra(MainActivity.LOADER_BUNDLE)) {
                MovieObject mItemData = intentDetailActivity.getParcelableExtra(MainActivity.LOADER_BUNDLE);
                tvDisplayData.setText(mItemData.getOriginal_title());
                tvReleaseDate.setText(mItemData.getRelease_date());
                tvRating.setText(mItemData.getVote_average());
                tvOverview.setText(mItemData.getOverview());

                String[] imageSizes = getResources().getStringArray(R.array.image_sizes);
                String imagePoster = imageSizes[3];

                String posterPath = new StringBuilder(base_image_url)  //String to load movie poster
                        .append(imagePoster)
                        .append(mItemData.getPosterPath())
                        .toString();
                Picasso.with(this)
                        .load(posterPath)
                        .fit()
                        .into(ivPoster);
                Log.v(MOVIE_EXTRA, "Title: " + posterPath);

                String[] imageBackDropSizes = getResources().getStringArray(R.array.backdrop_sizes); //String to load movie poster for collapsing toolbar
                String imageHeader = imageBackDropSizes[1];

                String posterPathHeader = new StringBuilder(base_image_url)
                        .append(imageHeader)
                        .append(mItemData.getBackdrop_path())
                        .toString();
                Picasso.with(this)
                        .load(posterPathHeader)
                        .fit()
                        .into(ivPosterHeader);
                Log.v(MOVIE_EXTRA, "Title: " + ivPosterHeader);

            }
        }
    }
    private int mColor = 0;
    private void setColor()
    {
        mColor = R.color.colorAccent;
    }

    private int getColorCrtWay()
    {
        return getResources().getColor(mColor);
    }

    @Override
    public void onBackPressed() {
        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}