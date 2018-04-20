package gr.mobap.popularmovies.details;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import gr.mobap.popularmovies.R;
import gr.mobap.popularmovies.adapters.TrailerAdapter;
import gr.mobap.popularmovies.utilities.NetworkUtility;

public class TrailerFragment extends Fragment {
    private Integer mLabel;
    private static final String TAG = TrailerFragment.class.getSimpleName();
    private TrailerAdapter trailerAdapter;
    @BindView(R.id.trailers_recycler_view)
    RecyclerView gridTrailersRecyclerView;
    private final ArrayList<String> trailers = new ArrayList<>();
    private final ArrayList<String> trailersName = new ArrayList<>();
    private static final String MOVIE_EXTRA = "MOVIE";

    public TrailerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = lf.inflate(R.layout.detail_movie_trailer, container, false);
        ButterKnife.bind(this, view);
        Log.v(MOVIE_EXTRA, "ID in TrailerFragment:" + mLabel);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gridTrailersRecyclerView.setLayoutManager(layoutManager);

        URL videoUri = NetworkUtility.buildMoviesTrailer(String.valueOf(mLabel));

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Objects.requireNonNull(videoUri).toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.getJSONArray("results").length(); i++) {
                            JSONObject obj = response.getJSONArray("results").getJSONObject(i);
                            trailers.add(obj.getString("key"));
                            trailersName.add(obj.getString("name"));
                        }
                        trailerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> Log.e(TAG, error.toString()));

        queue.add(request);

        trailerAdapter = new TrailerAdapter(getContext(), trailers, trailersName);
        gridTrailersRecyclerView.setAdapter(trailerAdapter);

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
