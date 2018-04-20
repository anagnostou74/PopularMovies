package gr.mobap.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

import static gr.mobap.popularmovies.data.MoviesContract.MoviesEntry.CONTENT_URI;

public class MoviesProvider extends ContentProvider {

    private static final int CODE_MOVIES = 100;
    private static final int CODE_MOVIES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mMovieDbHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MoviesContract.PATH_MOVIE, CODE_MOVIES);
        matcher.addURI(authority, MoviesContract.PATH_MOVIE + "/#", CODE_MOVIES_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIES_WITH_ID: {
                String id = uri.getPathSegments().get(1);
                cursor = mMovieDbHelper.getReadableDatabase().query(MoviesContract.MoviesEntry.TABLE_NAME,
                        new String[]{MoviesContract.MoviesEntry.COLUMN_MOVIE_ID},
                        MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{id},
                        null,
                        null,
                        null);
                break;
            }

            case CODE_MOVIES: {
                cursor = mMovieDbHelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIES_WITH_ID:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE;
            case CODE_MOVIES:
                return ContentResolver.CURSOR_DIR_BASE_TYPE;

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIES:
                long id = database.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();
        int movieDeleted;

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIES:
                movieDeleted = database.delete(MoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                database.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MoviesContract.MoviesEntry.TABLE_NAME + "'");
                break;

            case CODE_MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);

                movieDeleted = database.delete(MoviesContract.MoviesEntry.TABLE_NAME, MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (movieDeleted != 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return movieDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
