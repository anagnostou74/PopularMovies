package gr.mobap.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
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
                String vote_average;
                String title;
                Long popularity;
                String poster_path;
                String original_language;
                String original_title;
                JSONArray genreIds;
                String backdrop_path;
                Boolean adult;
                String overview;
                String release_date;

                JSONObject nthJSONObject = resultsJSONArray.optJSONObject(i);
                vote_count = nthJSONObject.optInt(VOTE_COUNT);
                id = nthJSONObject.optInt(ID);
                video = nthJSONObject.optBoolean(VIDEO);
                vote_average = nthJSONObject.getString(VOTE_AVERAGE);
                title = nthJSONObject.optString(TITLE);
                popularity = nthJSONObject.optLong(POPULARITY);
                poster_path = nthJSONObject.optString(POSTER_PATH);
                original_language = nthJSONObject.optString(ORIGINAL_LANGUAGE);
                original_title = nthJSONObject.optString(ORIGINAL_TITLE);
                genreIds = nthJSONObject.optJSONArray(GENRE_IDS);
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
                        genreIds,
                        backdrop_path,
                        adult,
                        overview,
                        release_date);

                movieArrayList.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieArrayList;

    }
}