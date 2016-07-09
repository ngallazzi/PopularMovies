package com.nikogalla.popularmovies.discover;


import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.nikogalla.popularmovies.R;
import com.nikogalla.popularmovies.SettingsActivity;
import com.nikogalla.popularmovies.details.MovieDetailsActivity;
import com.nikogalla.popularmovies.details.MovieDetailsFragment;
import com.nikogalla.popularmovies.model.Movie;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DiscoverMoviesActivity extends AppCompatActivity implements MovieAdapter.OnMovieSelectedListener {
    private final String TAG = getClass().getSimpleName();
    Context mContext;
    FrameLayout flMovieDetailsContainer;
    private static boolean twoPaneUI;
    public static boolean isTwoPaneUI() {
        return twoPaneUI;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_discover_movies);
        flMovieDetailsContainer = (FrameLayout) findViewById(R.id.flMovieDetailsContainer);
        if (flMovieDetailsContainer!=null){
            twoPaneUI = true;
        }else{
            twoPaneUI = false;
        }
        Log.v(TAG, "iTwoPaneUI: " + twoPaneUI);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMovieSelected(Movie movie) {
        if (twoPaneUI){
            setUpDetailsFragment(movie);
        }else{
            // start Activity Movie Details Activity
            Intent intent = new Intent (mContext,MovieDetailsActivity.class);
            intent.putExtra(getString(R.string.movie_argument_name),movie);
            startActivity(intent);
        }
    }

    public void setUpDetailsFragment(Movie movie){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        if (movie!=null){
            Bundle args = new Bundle();
            args.putParcelable(getString(R.string.movie_argument_name),movie);
            fragment.setArguments(args);
        }
        fragmentTransaction.replace(R.id.flMovieDetailsContainer, fragment);
        fragmentTransaction.commit();
    }
}