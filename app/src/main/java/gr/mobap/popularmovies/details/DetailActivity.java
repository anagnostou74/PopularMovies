package gr.mobap.popularmovies.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import gr.mobap.popularmovies.R;
import gr.mobap.popularmovies.adapters.ViewPagerAdapter;
import gr.mobap.popularmovies.model.MovieObject;

import static gr.mobap.popularmovies.MainActivity.LOADER_BUNDLE;
import static gr.mobap.popularmovies.MoviePreferences.base_image_url;

// The Detail Activity responsible to show Title, release date, rating, overview and to display poster
public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    private ViewPager viewPager;
    OverviewFragment overviewFragment;
    TrailerFragment trailerFragment;
    ReviewsFragment reviewsFragment;
    MenuItem prevMenuItem;

    private static final String MOVIE_EXTRA = "MOVIE";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setColor();
        cardView.setCardBackgroundColor(getColorCrtWay());

        Intent intentDetailActivity = getIntent();
        if (intentDetailActivity != null) {
            if (intentDetailActivity.hasExtra(LOADER_BUNDLE)) {
                MovieObject mItemData = intentDetailActivity.getParcelableExtra(LOADER_BUNDLE);

                String title = mItemData.getOriginal_title();
                tvDisplayData.setText(title);
                SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date sourceDate = null;
                try {
                    sourceDate = sourceDateFormat.parse(mItemData.getRelease_date());
                } catch (ParseException e) {
                    Log.v(MOVIE_EXTRA, "tvReleaseDate: " + tvReleaseDate);
                }

                SimpleDateFormat finalDateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
                String finalDateStr = finalDateFormat.format(sourceDate);
                tvReleaseDate.setText(finalDateStr);

                tvRating.setText(mItemData.getVote_average());

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
                        Log.d("page", "onPageSelected: " + position);
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
                setupViewPager(viewPager);

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
                Log.v(MOVIE_EXTRA, "posterPath: " + posterPath);

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


                cardViewShare.setOnClickListener(view -> {
                    String appName = ("#" + getString(R.string.app_name));
                    String text = String.format(getString(R.string.share), title, appName);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                    shareIntent.setType("text/plain");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_txt)));
                });
            }
        }
    }

    private int mColor = 0;

    private void setColor() {
        mColor = R.color.colorAccent;
    }

    private int getColorCrtWay() {
        return getResources().getColor(mColor);
    }

    private void setupViewPager(ViewPager viewPager) {
        Intent intentDetailActivity = getIntent();
        MovieObject mItemData = intentDetailActivity.getParcelableExtra(LOADER_BUNDLE);
        Bundle bundle = new Bundle();
        bundle.putString("overview", mItemData.getOverview());
        bundle.putInt("ID", mItemData.getId());

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        overviewFragment = new OverviewFragment();
        overviewFragment.setArguments(bundle);
        adapter.addFragment(overviewFragment);

        trailerFragment = new TrailerFragment();
        trailerFragment.setArguments(bundle);
        adapter.addFragment(trailerFragment);

        reviewsFragment = new ReviewsFragment();
        reviewsFragment.setArguments(bundle);
        adapter.addFragment(reviewsFragment);

        viewPager.setAdapter(adapter);
    }

}