package gr.mobap.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class MoviesContract {
    public static final String CONTENT_AUTHORITY = "gr.mobap.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "favorites";

    public static class MoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_HAS_VIDEO = "has_video";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_POSTER_IMAGE = "movie_poster_image";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_GENRE_ID = "genre_id";
        public static final String COLUMN_BACKDROP_IMAGE = "movie_backdrop_image";
        public static final String COLUMN_IS_ADULT = "is_adult";
        public static final String COLUMN_OVERVIEW = "movie_overview";
        public static final String COLUMN_RELEASE_DATE = "movie_release_date";
        public static final String COLUMN_IS_FAVORITE = "is_favorite";
        public static final String COLUMN_POSTER_FILE_PATH = "poster_file_path";
        public static final String COLUMN_BACKDROP_FILE_PATH = "backdrop_file_path";

        public static final int INDEX_VOTE_COUNT = 0;
        public static final int INDEX_MOVIE_ID = 1;
        public static final int INDEX_HAS_VIDEO = 2;
        public static final int INDEX_VOTE_AVERAGE = 3;
        public static final int INDEX_MOVIE_TITLE = 4;
        public static final int INDEX_POPULARITY = 5;
        public static final int INDEX_POSTER_IMAGE = 6;
        public static final int INDEX_ORIGINAL_LANGUAGE = 7;
        public static final int INDEX_ORIGINAL_TITLE = 8;
        public static final int INDEX_GENRE_ID = 9;
        public static final int INDEX_BACKDROP_IMAGE = 10;
        public static final int INDEX_IS_ADULT = 11;
        public static final int INDEX_OVERVIEW = 12;
        public static final int INDEX_RELEASE_DATE = 13;
        public static final int INDEX_IS_FAVORITE = 14;
        public static final int INDEX_POSTER_FILE_PATH = 15;
        public static final int INDEX_BACKDROP_FILE_PATH = 16;
    }

}
