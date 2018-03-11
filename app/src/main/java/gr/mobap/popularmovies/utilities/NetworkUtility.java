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

import static android.content.Context.CONNECTIVITY_SERVICE;
import static gr.mobap.popularmovies.data.MoviePreferences.API_KEY;
import static gr.mobap.popularmovies.data.MoviePreferences.APP_KEY;
import static gr.mobap.popularmovies.data.MoviePreferences.base_api;
import static gr.mobap.popularmovies.data.MoviePreferences.movie_path;

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


    static String getResponseFromHttpUrl(URL uri) throws IOException {
        URL url = new URL(uri.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
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