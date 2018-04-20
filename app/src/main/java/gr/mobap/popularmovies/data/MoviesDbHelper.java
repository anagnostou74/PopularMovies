package gr.mobap.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;


class MoviesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;


    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_MOVIE_FAVORITE_TABLE = " CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, " +
                MoviesContract.MoviesEntry.COLUMN_HAS_VIDEO + " INTEGER, " +
                MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_POPULARITY + " REAL, " +
                MoviesContract.MoviesEntry.COLUMN_POSTER_IMAGE + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_GENRE_ID + " TEXT, " + // save int array as JSON
                MoviesContract.MoviesEntry.COLUMN_BACKDROP_IMAGE + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_IS_ADULT + " INTEGER, " + //  0 - false, 1 - true
                MoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_IS_FAVORITE + " INTEGER, " + //  0 - false, 1 - true
                MoviesContract.MoviesEntry.COLUMN_POSTER_FILE_PATH + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_BACKDROP_FILE_PATH + " TEXT, " +
                " UNIQUE (" + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + ")" +
                ");";

        sqLiteDatabase.execSQL(CREATE_MOVIE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        if (!db.isReadOnly() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // Enable foreign key constraints
            // db.execSQL("PRAGMA foreign_keys=ON;");
            db.setForeignKeyConstraintsEnabled(true);
        }
    }
}
