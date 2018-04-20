package gr.mobap.popularmovies.utilities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import gr.mobap.popularmovies.data.MoviesContract;
import gr.mobap.popularmovies.model.MovieObject;

public class MovieLoader extends AsyncTaskLoader<ArrayList<MovieObject>> {
    private final WeakReference<Context> weakContext;
    private final Uri queryUri;
    private final Boolean getFavorites;
    private ArrayList<MovieObject> movieObjectArrayList = null;
    private static final String TAG = MovieLoader.class.getSimpleName();

    public MovieLoader(Context context, Uri uri, Boolean getFavorites) {
        super(context);
        this.weakContext = new WeakReference<>(context);
        this.queryUri = uri;
        this.getFavorites = getFavorites;
    }

    public ArrayList<MovieObject> loadInBackground() {
        ArrayList<MovieObject> movieObjects;

        try {
            if (getFavorites) {
                Cursor cursor = weakContext.get()
                        .getContentResolver()
                        .query(
                                queryUri,
                                null,
                                null,
                                null,
                                null);
                movieObjects = getArrayListFromCursor(Objects.requireNonNull(cursor));

            } else {
                String jsonData = NetworkUtility.getResponseFromHttpUrl(queryUri);
                if (jsonData == null) return null;
                movieObjects = JsonUtils.createMovieArrayList(jsonData);
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to load data");
            e.printStackTrace();
            return null;
        }

        super.deliverResult(movieObjects);
        return movieObjects;
    }

    @Override
    protected void onStartLoading() {

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

    private ArrayList<MovieObject> getArrayListFromCursor(Cursor cursor) {
        int cursorCount = cursor.getCount();
        ArrayList<MovieObject> arrayList = new ArrayList<>(cursorCount);

        for (int i = 0; i < cursorCount; i++) {
            while (cursor.moveToNext()) {
                arrayList.add(
                        new MovieObject(

                                cursor.getInt(MoviesContract.MoviesEntry.INDEX_VOTE_COUNT),
                                cursor.getInt(MoviesContract.MoviesEntry.INDEX_MOVIE_ID),
                                cursor.getInt(MoviesContract.MoviesEntry.INDEX_HAS_VIDEO) == 1,
                                cursor.getFloat(MoviesContract.MoviesEntry.INDEX_VOTE_AVERAGE),
                                cursor.getString(MoviesContract.MoviesEntry.INDEX_MOVIE_TITLE),
                                cursor.getFloat(MoviesContract.MoviesEntry.INDEX_POPULARITY),
                                cursor.getString(MoviesContract.MoviesEntry.INDEX_POSTER_IMAGE),
                                cursor.getString(MoviesContract.MoviesEntry.INDEX_ORIGINAL_LANGUAGE),
                                cursor.getString(MoviesContract.MoviesEntry.INDEX_ORIGINAL_TITLE),
                                JsonUtils.getIntArrayFromJSON(cursor.getString(MoviesContract.MoviesEntry.INDEX_GENRE_ID)),
                                cursor.getString(MoviesContract.MoviesEntry.INDEX_BACKDROP_IMAGE),
                                cursor.getInt(MoviesContract.MoviesEntry.INDEX_IS_ADULT) == 1,
                                cursor.getString(MoviesContract.MoviesEntry.INDEX_OVERVIEW),
                                cursor.getString(MoviesContract.MoviesEntry.INDEX_RELEASE_DATE),
                                cursor.getInt(MoviesContract.MoviesEntry.INDEX_IS_FAVORITE) == 1,
                                cursor.getString(MoviesContract.MoviesEntry.INDEX_POSTER_FILE_PATH),
                                cursor.getString(MoviesContract.MoviesEntry.INDEX_BACKDROP_FILE_PATH)
                        )); 
            }
        }
        
        return arrayList;
    }



}
