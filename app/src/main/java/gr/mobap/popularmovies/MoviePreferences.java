package gr.mobap.popularmovies;


import gr.mobap.popularmovies.BuildConfig;

// class for useful data used to fetch movies
public class MoviePreferences {

    public static String movieSection;
    public static final String base_api = "https://api.themoviedb.org/3/";
    public static final String movie_path = "movie";
    public static final String video_path = "videos";
    public static final String reviews_path = "reviews";
    public static final String popular_path = "popular";
    public static final String top_rated_path = "top_rated";
    public static final String favorites_path = "favorites";
    public static final String base_image_url = "http://image.tmdb.org/t/p/";
    public static final int LOADER_ID = 1;
    public static final String APP_KEY = BuildConfig.APP_KEY;
    public static final String API_KEY = "api_key";
    //private static final String PAGE = "page";
    //private static final String LANG = "language"; el-GR or en-US

}
