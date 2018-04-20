package gr.mobap.popularmovies.utilities;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gr.mobap.popularmovies.model.MovieObject;

//class to read JSON data from MoviesDb API
final class JsonUtils {

    static ArrayList<MovieObject> createMovieArrayList(String jsonData) {
        ArrayList<MovieObject> movieArrayList = new ArrayList<>();
        MovieObject movie;
        // JSON key names
        final String RESULTS = "results";
        final String VOTE_COUNT = "vote_count";
        final String ID = "id";
        final String VIDEO = "video";
        final String VOTE_AVERAGE = "vote_average";
        final String TITLE = "title";
        final String POPULARITY = "popularity";
        final String POSTER_PATH = "poster_path";
        final String ORIGINAL_LANGUAGE = "original_language";
        final String ORIGINAL_TITLE = "original_title";
        final String GENRE_IDS = "genre_ids";
        final String BACKDROP_PATH = "backdrop_path";
        final String ADULT = "adult";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";

        JSONObject jsonDataObj;
        try {
            jsonDataObj = new JSONObject(jsonData);

            JSONArray resultsJSONArray = jsonDataObj.optJSONArray(RESULTS);
            int size = resultsJSONArray.length();
            for (int i = 0; i < size; i++) {
                Integer vote_count;
                Integer id;
                Boolean video;
                Float vote_average;
                String title;
                Float popularity;
                String poster_path;
                String original_language;
                String original_title;
                String genreIds;
                String backdrop_path;
                Boolean adult;
                String overview;
                String release_date;

                JSONObject nthJSONObject = resultsJSONArray.optJSONObject(i);
                vote_count = nthJSONObject.optInt(VOTE_COUNT);
                id = nthJSONObject.optInt(ID);
                video = nthJSONObject.optBoolean(VIDEO);
                vote_average = ((float) nthJSONObject.optLong(VOTE_AVERAGE, 0));
                title = nthJSONObject.optString(TITLE);
                popularity = ((float) nthJSONObject.optLong(POPULARITY, 0));
                poster_path = nthJSONObject.optString(POSTER_PATH);
                original_language = nthJSONObject.optString(ORIGINAL_LANGUAGE);
                original_title = nthJSONObject.optString(ORIGINAL_TITLE);

                JSONArray jsonGenreIds;
                List<Integer> genre_ids_list = null;
                if (nthJSONObject.has(GENRE_IDS)) {
                    jsonGenreIds = nthJSONObject.optJSONArray(GENRE_IDS);
                    int genreSize = jsonGenreIds.length();
                    genre_ids_list = new ArrayList<>(genreSize);
                    for (int j = 0; j < genreSize; j++) {
                        genre_ids_list.add(jsonGenreIds.optInt(j));
                    }
                }

                backdrop_path = nthJSONObject.optString(BACKDROP_PATH);
                adult = nthJSONObject.optBoolean(ADULT);
                overview = nthJSONObject.optString(OVERVIEW);
                release_date = nthJSONObject.optString(RELEASE_DATE);

                movie = new MovieObject(vote_count,
                        id,
                        video,
                        vote_average,
                        title,
                        popularity,
                        poster_path,
                        original_language,
                        original_title,
                        genre_ids_list,
                        backdrop_path,
                        adult,
                        overview,
                        release_date,
                        null,
                        null,
                        null);

                movieArrayList.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieArrayList;

    }

    public static List<Integer> getIntArrayFromJSON(String stringJSONData) {
        List<Integer> genreIDs;
        if (TextUtils.isEmpty(stringJSONData)) {
            return null;
        } else {
            try {
                JSONArray jsonArray = new JSONArray(stringJSONData);
                int arSize = jsonArray.length();
                genreIDs = new ArrayList<>(arSize);
                for (int i = 0; i < arSize; i++) {
                    genreIDs.add(jsonArray.optInt(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
        return genreIDs;
    }
}