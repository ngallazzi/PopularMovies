package com.nikogalla.myportfolio.popularmovies.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by Nicola on 2016-04-09.
 */
public class TestDb extends AndroidTestCase {

    public static final String TAG = TestDb.class.getSimpleName();

    // The test must start with a clean DB

    void deleteDatabase() {
        mContext.deleteDatabase(MoviesDbHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteDatabase();
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MoviesContract.FavoriteMovieEntry.TABLE_NAME);
        deleteDatabase();

        SQLiteDatabase db = new MoviesDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without favorite movies table", tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MoviesContract.FavoriteMovieEntry.TABLE_NAME + ")", null);

        assertTrue("Error: This means that we were unable to query the database for table information.", c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> favoriteMoviesColumnHashSet = new HashSet<String>();
        favoriteMoviesColumnHashSet.add(MoviesContract.FavoriteMovieEntry._ID);
        favoriteMoviesColumnHashSet.add(MoviesContract.FavoriteMovieEntry.COLUMN_TITLE);
        favoriteMoviesColumnHashSet.add(MoviesContract.FavoriteMovieEntry.COLUMN_POSTER);
        favoriteMoviesColumnHashSet.add(MoviesContract.FavoriteMovieEntry.COLUMN_SYNOPSIS);
        favoriteMoviesColumnHashSet.add(MoviesContract.FavoriteMovieEntry.COLUMN_USER_RATING);
        favoriteMoviesColumnHashSet.add(MoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            favoriteMoviesColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required favorite movies
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required favorite movies entry columns",
                favoriteMoviesColumnHashSet.isEmpty());
        db.close();
    }

}
