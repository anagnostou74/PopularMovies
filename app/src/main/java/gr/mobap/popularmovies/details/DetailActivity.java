package gr.mobap.popularmovies.details;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import gr.mobap.popularmovies.R;
import gr.mobap.popularmovies.adapters.ViewPagerAdapter;
import gr.mobap.popularmovies.data.MoviesContract;
import gr.mobap.popularmovies.model.MovieObject;
import gr.mobap.popularmovies.utilities.NetworkUtility;

import static gr.mobap.popularmovies.MainActivity.INTENT_EXTRA_IS_FAVORITE;
import static gr.mobap.popularmovies.MainActivity.INTENT_MOVIE_EXTRA;
import static gr.mobap.popularmovies.MoviePreferences.base_image_url;

// The Detail Activity responsible to show Title, release date, rating, overview and to display poster
public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String FAVORITES_ACTIVITY_RESULT = "activity_result";
    private static final String SAVED_INSTANCE_STATE_KEY = "saved_instance_bundle";
    private static final String LOADER_BUNDLE_MOVIE_ID = "movie_id_bundle";
    private static final String LOADER_BUNDLE_GOT_FAVORITE = "got_favorite_bundle";
    private static final String LOADER_BUNDLE_IS_CONNECTED = "is_connected_bundle";

    private ViewPager viewPager;
    private MenuItem prevMenuItem;
    private MovieObject mMovieDataFromIntent;
    private Context mContext;

    @BindView(R.id.activity_detail_header)
    ImageView ivPosterHeader;
    @BindView(R.id.activity_detail_title)
    TextView tvDisplayData;
    @BindView(R.id.activity_detail_poster)
    ImageView ivPoster;
    @BindView(R.id.activity_detail_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.activity_detail_rating)
    TextView tvRating;
    @BindView(R.id.card_view)
    CardView cardView;
    @BindView(R.id.activity_share)
    CardView cardViewShare;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setColor();
        cardView.setCardBackgroundColor(getColorCrtWay());

        Intent intent = getIntent();
        mMovieDataFromIntent = intent.getParcelableExtra(INTENT_MOVIE_EXTRA);

        //  Check if the movie is favorite
        Uri queryUri = MoviesContract.MoviesEntry.CONTENT_URI.buildUpon().appendPath(mMovieDataFromIntent.getId().toString()).build();
        Cursor queryCursor = getContentResolver().query(queryUri, null, null, null, null);
        if (queryCursor != null) queryCursor.close();

        Boolean gotFavorite = intent.getBooleanExtra(INTENT_EXTRA_IS_FAVORITE, false);
        if (mMovieDataFromIntent == null && !gotFavorite) {
            finish();
            return;
        }
        if (!gotFavorite) mMovieDataFromIntent.setFavorite(false);
        Boolean isConnected = NetworkUtility.isConnected(mContext);
        if (!isConnected) {
            Toast.makeText(mContext, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }

        if (queryCursor != null && (mMovieDataFromIntent.getFavorite() || queryCursor.getCount() > 0)) {
            fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_full, null));

            if (isConnected) {
                String[] imageSizes = getResources().getStringArray(R.array.image_sizes);
                String imagePoster = imageSizes[3];

                String posterPath = new StringBuilder(base_image_url)  //String to load movie poster
                        .append(imagePoster)
                        .append(mMovieDataFromIntent.getPosterPath())
                        .toString();
                Picasso.with(this)
                        .load(posterPath)
                        .fit()
                        .into(ivPoster);
                Log.v(TAG, "posterPath: " + posterPath);

                String[] imageBackDropSizes = getResources().getStringArray(R.array.backdrop_sizes); //String to load movie poster for collapsing toolbar
                String imageHeader = imageBackDropSizes[1];

                String posterPathHeader = new StringBuilder(base_image_url)
                        .append(imageHeader)
                        .append(mMovieDataFromIntent.getBackdrop_path())
                        .toString();
                Picasso.with(this)
                        .load(posterPathHeader)
                        .fit()
                        .into(ivPosterHeader);
                Log.v(TAG, "Title: " + ivPosterHeader);

            }

        } else {
            fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_empty, null));
            if (isConnected) {
                String[] imageSizes = getResources().getStringArray(R.array.image_sizes);
                String imagePoster = imageSizes[3];

                String posterPath = new StringBuilder(base_image_url)  //String to load movie poster
                        .append(imagePoster)
                        .append(mMovieDataFromIntent.getPosterPath())
                        .toString();
                Picasso.with(this)
                        .load(posterPath)
                        .fit()
                        .into(ivPoster);
                Log.v(TAG, "posterPath: " + posterPath);

                String[] imageBackDropSizes = getResources().getStringArray(R.array.backdrop_sizes); //String to load movie poster for collapsing toolbar
                String imageHeader = imageBackDropSizes[1];

                String posterPathHeader = new StringBuilder(base_image_url)
                        .append(imageHeader)
                        .append(mMovieDataFromIntent.getBackdrop_path())
                        .toString();
                Picasso.with(this)
                        .load(posterPathHeader)
                        .fit()
                        .into(ivPosterHeader);
                Log.v(TAG, "Title: " + ivPosterHeader);

            }
        }

        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_overview:
                            viewPager.setCurrentItem(0);
                            break;
                        case R.id.action_trailer:
                            viewPager.setCurrentItem(1);
                            break;
                        case R.id.action_reviews:
                            viewPager.setCurrentItem(2);
                            break;
                    }
                    return false;
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d(TAG, "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        String receivedMovieId = mMovieDataFromIntent.getId().toString();

        Bundle bundle = new Bundle();
        bundle.putString("overview", mMovieDataFromIntent.getOverview());
        bundle.putInt("ID", mMovieDataFromIntent.getId());
        bundle.putString(LOADER_BUNDLE_MOVIE_ID, receivedMovieId);
        bundle.putBoolean(LOADER_BUNDLE_GOT_FAVORITE, gotFavorite);
        bundle.putBoolean(LOADER_BUNDLE_IS_CONNECTED, isConnected);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        OverviewFragment overviewFragment = new OverviewFragment();
        overviewFragment.setArguments(bundle);
        adapter.addFragment(overviewFragment);

        TrailerFragment trailerFragment = new TrailerFragment();
        trailerFragment.setArguments(bundle);
        adapter.addFragment(trailerFragment);

        ReviewsFragment reviewsFragment = new ReviewsFragment();
        reviewsFragment.setArguments(bundle);
        adapter.addFragment(reviewsFragment);

        viewPager.setAdapter(adapter);

        cardViewShare.setOnClickListener(view -> {
            if (!NetworkUtility.isConnected(this)) {
                Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            } else {
                String appName = ("#" + getString(R.string.app_name));
                String text = String.format(getString(R.string.share), mMovieDataFromIntent.getOriginal_title(), appName);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                shareIntent.setType("text/plain");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_txt)));
            }
        });


        tvDisplayData.setText(mMovieDataFromIntent.getTitle());

        SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date sourceDate = null;
        try {
            sourceDate = sourceDateFormat.parse(mMovieDataFromIntent.getRelease_date());
        } catch (ParseException e) {
            Log.v(TAG, "tvReleaseDate: " + tvReleaseDate);
        }
        SimpleDateFormat finalDateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
        String finalDateStr = finalDateFormat.format(sourceDate);
        tvReleaseDate.setText(finalDateStr);

        SpannableStringBuilder ssb = new SpannableStringBuilder(mMovieDataFromIntent.getVote_average().toString());
        ssb.setSpan(new RelativeSizeSpan(2), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append("/10 ");
        tvRating.setText(ssb);

        fab.setOnClickListener(view -> {

            if (queryCursor != null && (mMovieDataFromIntent.getFavorite() || queryCursor.getCount() > 0)) {
                deleteMovieDb();
                super.onNavigateUp();

            } else {
                addMovieDb();
            }
        });
    }

    private void deleteMovieDb() {
        int deletedRow = getContentResolver().delete(
                MoviesContract.MoviesEntry.CONTENT_URI.buildUpon()
                        .appendPath(mMovieDataFromIntent.getId().toString())
                        .build()
                , null, null);

        if (deletedRow == 1) setActivityResult();
        fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_empty, null));
        mMovieDataFromIntent.setFavorite(false);
    }

    private void addMovieDb() {
        getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, saveToContentValues(mMovieDataFromIntent));
        fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_full, null));
        mMovieDataFromIntent.setFavorite(true);
        Toast.makeText(mContext, R.string.add_favorites, Toast.LENGTH_LONG).show();
    }

    private void setActivityResult() {
        Intent intent = new Intent();
        intent.putExtra(FAVORITES_ACTIVITY_RESULT, true);
        setResult(Activity.RESULT_OK, intent);
    }

    private ContentValues saveToContentValues(MovieObject movieObject) {
        ContentValues values = new ContentValues(17);
        values.put(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT, movieObject.getVote_count());
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, movieObject.getId());
        values.put(MoviesContract.MoviesEntry.COLUMN_HAS_VIDEO, movieObject.getVideo());
        values.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, movieObject.getVote_average());
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE, movieObject.getTitle());
        values.put(MoviesContract.MoviesEntry.COLUMN_POPULARITY, movieObject.getPopularity());
        String relativePosterPath = movieObject.getPosterPath();
        values.put(MoviesContract.MoviesEntry.COLUMN_POSTER_IMAGE, relativePosterPath);
        values.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE, movieObject.getOriginal_language());
        values.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE, movieObject.getOriginal_title());
        String jsonGenreIDs = new JSONArray(movieObject.getGenre_ids()).toString();
        values.put(MoviesContract.MoviesEntry.COLUMN_GENRE_ID, jsonGenreIDs);
        String relativeBackdropPath = movieObject.getBackdrop_path();
        values.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_IMAGE, relativeBackdropPath);
        values.put(MoviesContract.MoviesEntry.COLUMN_IS_ADULT, movieObject.getAdult());
        values.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, movieObject.getOverview());
        values.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, movieObject.getRelease_date());
        values.put(MoviesContract.MoviesEntry.COLUMN_IS_FAVORITE, 1);

        return values;
    }

    private int mColor = 0;

    private void setColor() {
        mColor = R.color.colorAccent;
    }

    private int getColorCrtWay() {
        return getResources().getColor(mColor);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int position = coordinator.getScrollY();
        outState.putInt(SAVED_INSTANCE_STATE_KEY, position);
    }

}