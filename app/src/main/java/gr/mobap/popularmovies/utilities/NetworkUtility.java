package gr.mobap.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static gr.mobap.popularmovies.data.MoviePreferences.API_KEY;
import static gr.mobap.popularmovies.data.MoviePreferences.APP_KEY;
import static gr.mobap.popularmovies.data.MoviePreferences.base_api;
import static gr.mobap.popularmovies.data.MoviePreferences.movie_path;
import static gr.mobap.popularmovies.data.MoviePreferences.video_path;

public class NetworkUtility {
    private static final String TAG = NetworkUtility.class.getSimpleName();

    static URL buildUriRankMovies(String movieRankPath) { // Builds uri to get MoviesDb Api
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

    public static String getResponseFromHttpUrl(URL uri) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(uri)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static boolean isConnected(Context ctx) {
        boolean flag = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) ctx.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            flag = true;
        }
        return flag;
    }


}