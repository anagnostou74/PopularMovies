package gr.mobap.popularmovies;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import gr.mobap.popularmovies.adapters.MovieAdapter;
import gr.mobap.popularmovies.data.MoviesContract;
import gr.mobap.popularmovies.details.DetailActivity;
import gr.mobap.popularmovies.model.MovieObject;
import gr.mobap.popularmovies.utilities.MovieLoader;
import gr.mobap.popularmovies.utilities.NetworkUtility;

import static gr.mobap.popularmovies.MoviePreferences.favorites_path;
import static gr.mobap.popularmovies.MoviePreferences.popular_path;
import static gr.mobap.popularmovies.MoviePreferences.top_rated_path;
import static gr.mobap.popularmovies.details.DetailActivity.FAVORITES_ACTIVITY_RESULT;

// Main Activity creates a 2 columns grid layout, settings with 'popular' and 'top rating' radio choices for users
public class MainActivity extends AppCompatActivity implements
        MovieAdapter.PosterClickListener,
        android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<MovieObject>>, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID_INTERNET = 11;
    private static final int LOADER_ID_DATABASE = 22;
    private static final int CHANGED_FAVORITES_REQUEST = 33;
    private static final String LOADER_BUNDLE = "movie_loader_bundle";
    public static final String INTENT_MOVIE_EXTRA = "intent_movie_extra";
    public static final String INTENT_EXTRA_IS_FAVORITE = "intent_extra_is_favorite";
    private static final String SAVED_INSTANCE_STATE_KEY = "saved_instance_bundle";
    private static String movieSection;
    private Integer outStateRecyclerViewPosition = null;

    private PopupWindow popupWindow;
    private MovieAdapter movieAdapter;
    private int selectedRadioId;
    @BindView(R.id.recyclerview_movies)
    RecyclerView gridRecyclerView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar loading_indicator;
    @BindView(R.id.error_message)
    TextView mErrorMessageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        int spanCount = 2;
        GridLayoutManager manager = new GridLayoutManager(this, spanCount);
        gridRecyclerView.setLayoutManager(manager);
        gridRecyclerView.setHasFixedSize(true);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        } else {
            gridRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        movieAdapter = new MovieAdapter(this, this);
        gridRecyclerView.setAdapter(movieAdapter);


        //Open the radio choices to the last selected
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        movieSection = sharedPrefs.getString(getString(R.string.movie_rank), popular_path);
        selectedRadioId = sharedPrefs.getInt(getString(R.string.radio_selected_key), R.id.radio_popular);
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);

        int loaderID;
        String stringURI;
        if (movieSection.equals(favorites_path)) {
            loaderID = LOADER_ID_DATABASE;
            stringURI = MoviesContract.MoviesEntry.CONTENT_URI.toString();
        } else {
            showLoadingView();
            loaderID = LOADER_ID_INTERNET;
            stringURI = Objects.requireNonNull(NetworkUtility.buildUriRankMovies(movieSection)).toString();
        }

        Bundle bundleURI = new Bundle(1);
        bundleURI.putString(LOADER_BUNDLE, stringURI);
        getSupportLoaderManager().initLoader(loaderID, bundleURI, this);

        if (savedInstanceState != null) {
            outStateRecyclerViewPosition = savedInstanceState.getInt(SAVED_INSTANCE_STATE_KEY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuID = item.getItemId();
        switch (menuID) {
            case R.id.overflow_menu: {
                View menuItemView = findViewById(R.id.overflow_menu);

                if (popupWindow == null) {
                    onOverFlowMenuClick(menuItemView);
                } else if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    onOverFlowMenuClick(menuItemView);
                }

                break;
            }
            case R.id.menu_refresh: {
                restartLoader(movieSection);
                Log.d(TAG, "menu_refresh: " + movieSection);

                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void onOverFlowMenuClick(View menuItemView) {
        LinearLayout viewGroup = findViewById(R.id.linearSettings);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        final View popupLayout = layoutInflater.inflate(R.layout.activity_settings, viewGroup);

        popupWindow = new PopupWindow(
                popupLayout,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(() -> {

        });
        popupWindow.setAnimationStyle(1);
        popupWindow.showAsDropDown(menuItemView);

        RadioGroup radioGroup = popupLayout.findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        radioGroup.check(selectedRadioId);

        RadioButton radioButtonPopular = popupLayout.findViewById(R.id.radio_popular);
        View.OnClickListener radioPopularListener = v -> {
            restartLoader(popular_path);
            radioGroup.clearCheck();
            radioGroup.check(R.id.radio_popular);
            popupWindow.dismiss();
            saveMovieSectionPreference(popular_path, R.id.radio_popular);
            Log.d(TAG, "popular_path: " + popular_path);
        };
        radioButtonPopular.setOnClickListener(radioPopularListener);
        popupLayout.findViewById(R.id.menu_textView_popular).setOnClickListener(radioPopularListener);

        RadioButton radioButtonTopRated = popupLayout.findViewById(R.id.radio_top_rated);
        View.OnClickListener radio_top_rated = v -> {
            restartLoader(top_rated_path);
            radioGroup.clearCheck();
            radioGroup.check(R.id.radio_top_rated);
            popupWindow.dismiss();
            saveMovieSectionPreference(top_rated_path, R.id.radio_top_rated);
            Log.d(TAG, "top_rated_path: " + top_rated_path);
        };
        radioButtonTopRated.setOnClickListener(radio_top_rated);
        popupLayout.findViewById(R.id.menu_textView_top_rated).setOnClickListener(radio_top_rated);

        RadioButton radioButtonFavorite = popupLayout.findViewById(R.id.radio_favorites);
        View.OnClickListener radio_favorites = v -> {
            restartLoader(favorites_path);
            radioGroup.clearCheck();
            radioGroup.check(R.id.radio_favorites);
            popupWindow.dismiss();
            saveMovieSectionPreference(favorites_path, R.id.radio_favorites);
            Log.d(TAG, "menu_refresh: " + favorites_path);
        };
        radioButtonFavorite.setOnClickListener(radio_favorites);
        popupLayout.findViewById(R.id.menu_textView_favorites).setOnClickListener(radio_favorites);

    }

    private void saveMovieSectionPreference(String section, int radioId) {
        selectedRadioId = radioId;
        movieSection = section;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefs.edit().putString(this.getString(R.string.movie_rank), section).apply();
        sharedPrefs.edit().putInt(this.getString(R.string.radio_selected_key), radioId).apply();
    }

    private void restartLoader(String rank) {

        String stringURI;
        int loaderID;
        if (rank.equals(favorites_path)) {
            loaderID = LOADER_ID_DATABASE;
            stringURI = MoviesContract.MoviesEntry.CONTENT_URI.toString();
        } else {
            showLoadingView();
            loaderID = LOADER_ID_INTERNET;
            stringURI = Objects.requireNonNull(NetworkUtility.buildUriRankMovies(movieSection)).toString();
        }

        Bundle bundleURI = new Bundle(1);
        bundleURI.putString(LOADER_BUNDLE, stringURI);
        getSupportLoaderManager().restartLoader(loaderID, bundleURI, this);

        gridRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onBackPressed() {
        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            } else {
                android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
                        .unregisterOnSharedPreferenceChangeListener(this);
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @NonNull
    @Override
    public Loader<ArrayList<MovieObject>> onCreateLoader(int id, Bundle args) {
        Uri queryUri = Uri.parse(args.getString(LOADER_BUNDLE));

        return new MovieLoader(getApplicationContext(), queryUri, id == LOADER_ID_DATABASE);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<MovieObject>> loader, ArrayList<MovieObject> data) {
        Boolean isFavorite = loader.getId() == LOADER_ID_DATABASE;
        movieAdapter.swapMovieData(data, isFavorite);
        showDataView();
        if (outStateRecyclerViewPosition != null) {
            gridRecyclerView.smoothScrollToPosition(outStateRecyclerViewPosition);
            outStateRecyclerViewPosition = null;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<MovieObject>> loader) {
        Boolean isFavorite = loader.getId() == LOADER_ID_DATABASE;
        movieAdapter.swapMovieData(null, isFavorite);
    }

    private void showDataView() {
        gridRecyclerView.setVisibility(View.VISIBLE);
        loading_indicator.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void showLoadingView() {
        gridRecyclerView.setVisibility(View.INVISIBLE);
        loading_indicator.setVisibility(View.VISIBLE);
        if (!NetworkUtility.isConnected(MainActivity.this)) {
            Toast.makeText(MainActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            mErrorMessageDisplay.setText(getResources().getString(R.string.no_internet));
            loading_indicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int position = ((GridLayoutManager) gridRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        outState.putInt(SAVED_INSTANCE_STATE_KEY, position);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        restartLoader(movieSection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    @SuppressLint("RestrictedApi")
    public void onPosterClicked(MovieObject movie, Boolean isFavorite, View itemView) {
        Intent detailsIntent = new Intent(MainActivity.this, DetailActivity.class);
        detailsIntent.putExtra(INTENT_MOVIE_EXTRA, movie);
        detailsIntent.putExtra(INTENT_EXTRA_IS_FAVORITE, isFavorite);
        ActivityOptions activityOptions = ActivityOptions.makeScaleUpAnimation(itemView, 0, 0, itemView.getWidth(), itemView.getHeight());
        startActivityForResult(detailsIntent, CHANGED_FAVORITES_REQUEST, activityOptions.toBundle());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHANGED_FAVORITES_REQUEST && resultCode == RESULT_OK && data.getBooleanExtra(FAVORITES_ACTIVITY_RESULT, false)) {
            restartLoader(favorites_path);
            saveMovieSectionPreference(favorites_path, R.id.radio_favorites);
        }
    }
}
