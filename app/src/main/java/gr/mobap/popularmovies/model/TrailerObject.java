package gr.mobap.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TrailerObject implements Parcelable {

    private final String id;
    private final String iso_639_1;
    private final String iso_3166_1;
    private final String key;
    private final String name;
    private final String site;
    private final Integer size;
    private final String type;

    public TrailerObject(String id, String iso_639_1, String iso_3166_1, String key, String name, String site, Integer size, String type) {
        this.id = id;
        this.iso_639_1 = iso_639_1;
        this.iso_3166_1 = iso_3166_1;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    private TrailerObject(Parcel trailer) {
        id = trailer.readString();
        iso_639_1 = trailer.readString();
        iso_3166_1 = trailer.readString();
        key = trailer.readString();
        name = trailer.readString();
        site = trailer.readString();
        size = trailer.readInt();
        type = trailer.readString();
    }

    public String getId() {
        return id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public Integer getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(iso_3166_1);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }

    public static final Parcelable.Creator<TrailerObject> CREATOR = new Parcelable.Creator<TrailerObject>() {

        @Override
        public TrailerObject createFromParcel(Parcel trailer) {
            return new TrailerObject(trailer);
        }

        @Override
        public TrailerObject[] newArray(int size) {
            return new TrailerObject[size];
        }
    };
}