package gr.mobap.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
//
public class MovieObject implements Parcelable {

    private final Integer vote_count;
    private final Integer id;
    private Boolean video;
    private final String vote_average;
    private final String title;
    private final Long popularity;
    private final String poster_path;
    private final String original_language;
    private final String original_title;
    private JSONArray genre_ids;
    private final String backdrop_path;
    private Boolean adult;
    private final String overview;
    private final String release_date;


    public MovieObject(Integer vote_count, Integer id, Boolean video, String vote_average, String title, Long popularity, String poster_path, String original_language, String original_title, JSONArray genre_ids, String backdrop_path, Boolean adult, String overview, String release_date) {
        this.vote_count = vote_count;
        this.id = id;
        this.video = video;
        this.vote_average = vote_average;
        this.title = title;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.original_language = original_language;
        this.original_title = original_title;
        this.genre_ids = genre_ids;
        this.backdrop_path = backdrop_path;
        this.adult = adult;
        this.overview = overview;
        this.release_date = release_date;
    }

    private MovieObject(Parcel movie) {
        vote_count = movie.readInt();
        id = movie.readInt();
        //video = movie.readSparseBooleanArray();
        vote_average = movie.readString();
        title = movie.readString();
        popularity = movie.readLong();
        poster_path = movie.readString();
        original_language = movie.readString();
        original_title = movie.readString();
        //genre_ids = movie.readArray();
        backdrop_path = movie.readString();
        //movie.readBooleanArray(new Boolean[]{adult});
        overview = movie.readString();
        release_date = movie.readString();
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getVideo() {
        return video;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getTitle() {
        return title;
    }

    public Long getPopularity() {
        return popularity;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public JSONArray getGenre_ids() {
        return genre_ids;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public Boolean getAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(vote_count);
        dest.writeInt(id);
        //dest.writeSparseBooleanArray(video);
        dest.writeString(vote_average);
        dest.writeString(title);
        dest.writeLong(popularity);
        dest.writeString(poster_path);
        dest.writeString(original_language);
        dest.writeString(original_title);
        //dest.writeArray(new JSONArray[]{genre_ids});
        dest.writeString(backdrop_path);
        //dest.writeBooleanArray(new Boolean[]{adult});
        dest.writeString(overview);
        dest.writeString(release_date);
    }

    public static final Parcelable.Creator<MovieObject> CREATOR = new Parcelable.Creator<MovieObject>() {

        @Override
        public MovieObject createFromParcel(Parcel movie) {
            return new MovieObject(movie);
        }

        @Override
        public MovieObject[] newArray(int size) {
            return new MovieObject[size];
        }
    };
}