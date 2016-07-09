package com.nikogalla.myportfolio.popularmovies.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nikogalla.myportfolio.R;
import com.nikogalla.myportfolio.popularmovies.SettingsActivity;
import com.nikogalla.myportfolio.popularmovies.model.Movie;

import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {
    private final String TAG = MovieDetailsActivity.class.getSimpleName();
    public static final String MOVIE_API_URL = "http://api.themoviedb.org/3/discover/movie/";
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        // Butterknife library binding
        ButterKnife.bind(this);
        mContext = this;
        try{
            // Inflating fragment details
            Movie attachedMovie = getIntent().getParcelableExtra(getString(R.string.movie_argument_name));
            if (savedInstanceState == null){
                MovieDetailsFragment fragment = new MovieDetailsFragment();
                Bundle args = new Bundle();
                args.putParcelable(getString(R.string.movie_argument_name),attachedMovie);
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.flMovieDetailsContainer, fragment)
                        .commit();
            }
        }catch (Exception e){
            Log.d(TAG,"Error updating UI: " + e.getMessage());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try{
            getMenuInflater().inflate(R.menu.main,menu);
        }catch (Exception e){
            Log.e(TAG, "Unable to inflate options menu: " +e.getMessage());
        }
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(mContext,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
