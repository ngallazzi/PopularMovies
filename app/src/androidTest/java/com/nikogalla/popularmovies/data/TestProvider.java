package com.nikogalla.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.nikogalla.popularmovies.data.MoviesContract.FavoriteMovieEntry;

/**
 * Created by Nicola on 2016-04-09.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    // All record delete

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(FavoriteMovieEntry.CONTENT_URI,null,null);

        // Check if deletion has succeeded
        Cursor cursor = mContext.getContentResolver().query(
                FavoriteMovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Favorite Movie table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecordsFromDB() {
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(FavoriteMovieEntry.TABLE_NAME, null, null);
        db.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromDB();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MoviesProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + MoviesContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MoviesContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testGetType() {
        String type = mContext.getContentResolver().getType(MoviesContract.FavoriteMovieEntry.CONTENT_URI);
        assertEquals("Error: the Favorite Movie Entry CONTENT_URI should return FavoriteMovieEntry.CONTENT_TYPE",
                MoviesContract.FavoriteMovieEntry.CONTENT_TYPE, type);
    }

    public void testBasicFavoriteMovieQuery(){
        // insert our test records into the database
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues movieValues = TestUtilities.createFavoriteMoviesTestValues();
        long movieRowId = db.insert(FavoriteMovieEntry.TABLE_NAME, null, movieValues);
        assertTrue("Unable to Insert FavoriteMovieEntry into the Database", movieRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor favoriteMoviesCursor = mContext.getContentResolver().query(
                FavoriteMovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicFavoriteMovieQuery", favoriteMoviesCursor, movieValues);
    }

    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createFavoriteMoviesTestValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FavoriteMovieEntry.CONTENT_URI, true, tco);
        Uri locationUri = mContext.getContentResolver().insert(FavoriteMovieEntry.CONTENT_URI, testValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                FavoriteMovieEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating FavoriteMovieEntry.",
                cursor, testValues);
    }

    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our favorite movies delete.
        TestUtilities.TestContentObserver favoriteMoviesObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FavoriteMovieEntry.CONTENT_URI, true, favoriteMoviesObserver);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        favoriteMoviesObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(favoriteMoviesObserver);
    }
}
