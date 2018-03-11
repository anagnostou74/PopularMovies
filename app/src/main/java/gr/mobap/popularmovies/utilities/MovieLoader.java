package gr.mobap.popularmovies.utilities;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import gr.mobap.popularmovies.R;
import gr.mobap.popularmovies.model.MovieObject;

public class MovieLoader extends AsyncTaskLoader<ArrayList<MovieObject>> {

    private ArrayList<MovieObject> movieObjectArrayList = null;
    private final String movieRankPath;

    public MovieLoader(Context context, String movieRankPath) {
        super(context);
        this.movieRankPath = movieRankPath;
    }

    public ArrayList<MovieObject> loadInBackground() {
        ArrayList<MovieObject> movieObjects;
        URL uri = NetworkUtility.buildUriRankMovies(movieRankPath);

        String jsonData = null;
        try {
            jsonData = NetworkUtility.getResponseFromHttpUrl(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonData == null) return null;
        movieObjects = JsonUtils.createMovieArrayList(jsonData);

        return movieObjects;
    }

    @Override
    protected void onStartLoading() {
        if (!NetworkUtility.isConnected(this.getContext())) {
            Toast.makeText(getContext(),
                    R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        if (movieObjectArrayList != null) {
            deliverResult(movieObjectArrayList);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(ArrayList<MovieObject> data) {
        movieObjectArrayList = data;
        super.deliverResult(data);
    }

}
