package gr.mobap.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

//
public class MovieObject implements Parcelable {

    private final Integer vote_count;
    private final Integer id;
    private final Boolean video;
    private final Float vote_average;
    private final String title;
    private final Float popularity;
    private final String poster_path;
    private final String original_language;
    private final String original_title;
    private final List<Integer> genre_ids;
    private final String backdrop_path;
    private final Boolean adult;
    private final String overview;
    private final String release_date;

    private Boolean is_favorite;
    private String poster_local_path;
    private String backdrop_local_path;


    public MovieObject(Integer vote_count, Integer id, Boolean video, Float vote_average, String title, Float popularity, String poster_path, String original_language, String original_title, List<Integer> genre_ids, String backdrop_path, Boolean adult, String overview, String release_date, Boolean is_favorite, String poster_local_path, String backdrop_local_path) {
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

        this.is_favorite = is_favorite;
        this.poster_local_path = poster_local_path;
        this.backdrop_local_path = backdrop_local_path;
    }

    private MovieObject(Parcel movie) {
        vote_count = movie.readByte() == 0x00 ? null : movie.readInt();
        id = movie.readByte() == 0x00 ? null : movie.readInt();
        byte video_value = movie.readByte();
        video = video_value == 0x02 ? null : video_value != 0x00;
        vote_average = movie.readByte() == 0x00 ? null : movie.readFloat();
        title = movie.readString();
        popularity = movie.readByte() == 0x00 ? null : movie.readFloat();
        poster_path = movie.readString();
        original_language = movie.readString();
        original_title = movie.readString();
        if (movie.readByte() == 0x01) {
            genre_ids = new ArrayList<>();
            movie.readList(genre_ids, Integer.class.getClassLoader());
        } else {
            genre_ids = null;
        }
        backdrop_path = movie.readString();
        byte adult_value = movie.readByte();
        adult = adult_value == 0x02 ? null : adult_value != 0x00;
        overview = movie.readString();
        release_date = movie.readString();

        byte is_favorite_value = movie.readByte();
        is_favorite = is_favorite_value == 0x02 ? null : is_favorite_value != 0x00;
        poster_local_path = movie.readString();
        backdrop_local_path = movie.readString();

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

    public Float getVote_average() {
        return vote_average;
    }

    public String getTitle() {
        return title;
    }

    public Float getPopularity() {
        return popularity;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public List<Integer> getGenre_ids() {
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


    public Boolean getFavorite() {
        return is_favorite;
    }

    public String getPosterFilePath() {
        return poster_local_path;
    }

    public String getBackdropFilePath() {
        return backdrop_local_path;
    }

    public void setFavorite(Boolean favorite) {
        is_favorite = favorite;
    }

    public void setPosterFilePath(String set_poster_local_path) {
        this.poster_local_path = set_poster_local_path;
    }

    public void setBackdropFilePath(String set_backdrop_local_path) {
        this.backdrop_local_path = set_backdrop_local_path;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (vote_count == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(vote_count);
        }
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        if (video == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (video ? 0x01 : 0x00));
        }
        if (vote_average == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(vote_average);
        }
        dest.writeString(title);
        if (popularity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(popularity);
        }
        dest.writeString(poster_path);
        dest.writeString(original_language);
        dest.writeString(original_title);
        if (genre_ids == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genre_ids);
        }
        dest.writeString(backdrop_path);
        if (adult == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (adult ? 0x01 : 0x00));
        }
        dest.writeString(overview);
        dest.writeString(release_date);

        if (is_favorite == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (is_favorite ? 0x01 : 0x00));
        }
        dest.writeString(poster_local_path);
        dest.writeString(backdrop_local_path);
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