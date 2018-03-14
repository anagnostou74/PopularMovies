package gr.mobap.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import gr.mobap.popularmovies.data.MovieAdapter;
import gr.mobap.popularmovies.model.MovieObject;
import gr.mobap.popularmovies.utilities.MovieLoader;
import gr.mobap.popularmovies.utilities.NetworkUtility;

import static gr.mobap.popularmovies.data.MoviePreferences.LOADER_ID;
import static gr.mobap.popularmovies.data.MoviePreferences.popular_path;
import static gr.mobap.popularmovies.data.MoviePreferences.movieSection;
import static gr.mobap.popularmovies.data.MoviePreferences.top_rated_path;

// Main Activity creates a 2 columns grid layout, settings with 'popular' and 'top rating' radio choices for users
public class MainActivity extends AppCompatActivity implements
        MovieAdapter.PosterClickListener,
        android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<MovieObject>> {
    public static final String LOADER_BUNDLE = "movie_loader_bundle";

    private static final String TAG = MainActivity.class.getSimpleName();
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
        movieAdapter = new MovieAdapter(this, this);
        gridRecyclerView.setAdapter(movieAdapter);

        showLoadingView();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        movieSection = sharedPrefs.getString(getString(R.string.movie_rank), popular_path);
        selectedRadioId = sharedPrefs.getInt(getString(R.string.radio_selected_key), R.id.radio_popular);

        Bundle bundle = new Bundle(1);
        bundle.putString(LOADER_BUNDLE, movieSection);
        getSupportLoaderManager().initLoader(LOADER_ID, bundle, this).startLoading();
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
        setRadioSelection(radioGroup, selectedRadioId, false);

        RadioButton radioButtonPopular = popupLayout.findViewById(R.id.radio_popular);
        View.OnClickListener radioPopularListener = v -> {
            loadPopular();
            setRadioSelection(radioGroup, R.id.radio_popular, true);
        };
        radioButtonPopular.setOnClickListener(radioPopularListener);
        popupLayout.findViewById(R.id.menu_textView_popular).setOnClickListener(radioPopularListener);

        RadioButton radioButtonTopRated = popupLayout.findViewById(R.id.radio_top_rated);
        View.OnClickListener radio_top_rated = v -> {
            loadTopRated();
            setRadioSelection(radioGroup, R.id.radio_top_rated, true);
        };
        radioButtonTopRated.setOnClickListener(radio_top_rated);
        popupLayout.findViewById(R.id.menu_textView_top_rated).setOnClickListener(radio_top_rated);

    }

    private void setRadioSelection(RadioGroup radioGroup, int radioId, boolean dismiss) {
        radioGroup.clearCheck();
        radioGroup.check(radioId);
        selectedRadioId = radioId;
        if (dismiss) popupWindow.dismiss();
    }

    private void loadPopular() {
        restartLoader(popular_path);
        saveMovieSectionPreference(popular_path, R.id.radio_popular);
    }

    private void loadTopRated() {
        restartLoader(top_rated_path);
        saveMovieSectionPreference(top_rated_path, R.id.radio_top_rated);
    }

    private void saveMovieSectionPreference(String section, int radioId) {
        selectedRadioId = radioId;
        movieSection = section;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefs.edit().putString(this.getString(R.string.movie_rank), section).apply();
        sharedPrefs.edit().putInt(this.getString(R.string.radio_selected_key), radioId).apply();
    }

    private void restartLoader(String rank) {
        showLoadingView();
        Bundle bundle = new Bundle(1);
        bundle.putString(LOADER_BUNDLE, rank);
        getSupportLoaderManager().restartLoader(LOADER_ID, bundle, this);
        gridRecyclerView.smoothScrollToPosition(0);
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

    @NonNull
    @Override
    public Loader<ArrayList<MovieObject>> onCreateLoader(int id, Bundle args) {
        String section = args.getString(LOADER_BUNDLE);
        return new MovieLoader(getApplicationContext(), section);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<MovieObject>> loader, ArrayList<MovieObject> data) {
        movieAdapter.swapMovieData(data);
        showDataView();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<MovieObject>> loader) {
        movieAdapter.swapMovieData(null);
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
            Toast.makeText(MainActivity.this,
                    R.string.no_internet, Toast.LENGTH_SHORT).show();
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            mErrorMessageDisplay.setText(getResources().getString(R.string.no_internet));
            loading_indicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPosterClicked(MovieObject movie) { // Users action starts activity to display movie details
        Intent intentDetailActivity = new Intent(this, DetailActivity.class);
        intentDetailActivity.putExtra(LOADER_BUNDLE, movie);
        startActivity(intentDetailActivity);

        Log.v(TAG, "Title: " + movie.getTitle());
    }

}
