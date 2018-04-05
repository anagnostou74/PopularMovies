package gr.mobap.popularmovies.details;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import gr.mobap.popularmovies.R;
import gr.mobap.popularmovies.data.ReviewsAdapter;
import gr.mobap.popularmovies.utilities.NetworkUtility;

import static gr.mobap.popularmovies.data.MoviePreferences.API_KEY;
import static gr.mobap.popularmovies.data.MoviePreferences.APP_KEY;


public class ReviewsFragment extends Fragment {
    Integer mLabel;
    private static final String TAG = TrailerFragment.class.getSimpleName();
    private ReviewsAdapter reviewsAdapter;
    ArrayList<String> reviews = new ArrayList<>();
    ArrayList<String> reviewer = new ArrayList<>();
    private static final String MOVIE_EXTRA = "MOVIE";

    public ReviewsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.detail_movie_reviews, container, false);
        ButterKnife.bind(this, view);
        Log.v(MOVIE_EXTRA, "ID in ReviewsFragment:" + mLabel);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView reviewsRecyclerView = view.findViewById(R.id.reviews_recycler_view);
        reviewsRecyclerView.setLayoutManager(layoutManager);
        reviewsAdapter = new ReviewsAdapter(getContext(), reviews, reviewer);
        reviewsRecyclerView.setAdapter(reviewsAdapter);

        URL videoUri = NetworkUtility.buildMoviesReviews(String.valueOf(mLabel));

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = videoUri.toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.getJSONArray("results").length(); i++) {
                            JSONObject obj = response.getJSONArray("results").getJSONObject(i);
                            reviews.add(obj.getString("content"));
                            reviewer.add(obj.getString("author"));
                        }
                        reviewsAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> Log.e(TAG, error.toString()));

        queue.add(request);

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        if (data != null) {
            mLabel = data.getInt("ID");
        }
    }
}
