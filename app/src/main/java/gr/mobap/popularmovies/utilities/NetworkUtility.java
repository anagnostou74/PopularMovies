package gr.mobap.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static gr.mobap.popularmovies.MoviePreferences.API_KEY;
import static gr.mobap.popularmovies.MoviePreferences.APP_KEY;
import static gr.mobap.popularmovies.MoviePreferences.base_api;
import static gr.mobap.popularmovies.MoviePreferences.movie_path;
import static gr.mobap.popularmovies.MoviePreferences.reviews_path;
import static gr.mobap.popularmovies.MoviePreferences.video_path;

public class NetworkUtility {
    private static final String TAG = NetworkUtility.class.getSimpleName();

    public static URL buildUriRankMovies(String movieRankPath) { // Builds uri to get MoviesDb Api
        Uri builtUri = Uri.parse(base_api).buildUpon()
                .appendPath(movie_path)
                .appendPath(movieRankPath)
                .appendQueryParameter(API_KEY, APP_KEY)
                //.appendQueryParameter(LANG, ...)
                //.appendQueryParameter(PAGE, ...)
                .build();

        try {
            URL moviesQueryUrl = new URL(builtUri.toString());
            Log.v(TAG, "URL: " + moviesQueryUrl);
            return moviesQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildMoviesReviews(String movieReviewsPath) { // Builds uri to get MoviesDb Reviews for specific movie
        Uri builtTrailerUri = Uri.parse(base_api).buildUpon()
                .appendPath(movie_path)
                .appendPath(movieReviewsPath)
                .appendPath(reviews_path)
                .appendQueryParameter(API_KEY, APP_KEY)
                .build();
        try {
            URL moviesTrailerUrl = new URL(builtTrailerUri.toString());
            Log.v(TAG, "Trailer URL: " + moviesTrailerUrl);
            return moviesTrailerUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildMoviesTrailer(String movieTrailerPath) { // Builds uri to get MoviesDb Trailer for specific movie
        Uri builtTrailerUri = Uri.parse(base_api).buildUpon()
                .appendPath(movie_path)
                .appendPath(movieTrailerPath)
                .appendPath(video_path)
                .appendQueryParameter(API_KEY, APP_KEY)
                .build();
        try {
            URL moviesTrailerUrl = new URL(builtTrailerUri.toString());
            Log.v(TAG, "Trailer URL: " + moviesTrailerUrl);
            return moviesTrailerUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpUrl(Uri uri) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(String.valueOf(uri))
                .build();
        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }
}