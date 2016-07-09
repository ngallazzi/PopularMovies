package com.nikogalla.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.nikogalla.popularmovies.util.TypeConverters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nicola on 2016-04-08.
 */
public class MoviesProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mOpenHelper;

    static final int FAVORITE_MOVIES = 100; //DIR
    static final int FAVORITE_MOVIE_WITH_ID = 101; // ITEM

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MoviesContract.PATH_FAVORITE_MOVIE, FAVORITE_MOVIES);
        matcher.addURI(authority,MoviesContract.PATH_FAVORITE_MOVIE + "/#",FAVORITE_MOVIE_WITH_ID);
        return matcher;
    }

    private static final String movieWithMovieIdSelection =
            MoviesContract.FavoriteMovieEntry.TABLE_NAME + "." + MoviesContract.FavoriteMovieEntry.COLUMN_ID + " = ? ";

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_MOVIES:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(MoviesContract.FavoriteMovieEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            }
            case FAVORITE_MOVIE_WITH_ID:
            {
                retCursor = getMovieWithId(uri,projection);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getMovieWithId (Uri uri, String[] projection){
        String movieId = MoviesContract.FavoriteMovieEntry.getIdFromUri(uri);
        Cursor movieCursor = mOpenHelper.getReadableDatabase().query(MoviesContract.FavoriteMovieEntry.TABLE_NAME, projection, movieWithMovieIdSelection, new String[]{movieId}, null, null,null,null);
        return movieCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            // Student: Uncomment and fill out these two cases
            case FAVORITE_MOVIES:
                return MoviesContract.FavoriteMovieEntry.CONTENT_TYPE;
            case FAVORITE_MOVIE_WITH_ID:
                return MoviesContract.FavoriteMovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case FAVORITE_MOVIES: {
                long _id = db.insert(MoviesContract.FavoriteMovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.FavoriteMovieEntry.buildFavoriteMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection) selection = "1";
        switch (match) {
            case FAVORITE_MOVIES: {
                rowsDeleted = db.delete(MoviesContract.FavoriteMovieEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case FAVORITE_MOVIE_WITH_ID: {
                String movieId = MoviesContract.FavoriteMovieEntry.getIdFromUri(uri);
                rowsDeleted = db.delete(MoviesContract.FavoriteMovieEntry.TABLE_NAME,movieWithMovieIdSelection, new String[]{movieId});
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Student: return the actual rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
