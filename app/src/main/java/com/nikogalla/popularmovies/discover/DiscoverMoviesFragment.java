package com.nikogalla.popularmovies.discover;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.nikogalla.popularmovies.R;
import com.nikogalla.popularmovies.data.MoviesContract;
import com.nikogalla.popularmovies.model.Movie;
import com.nikogalla.popularmovies.service.ApiResponses;
import com.nikogalla.popularmovies.service.TheMovieDbApiService;

import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nicola on 2016-04-13.
 */
public class DiscoverMoviesFragment extends Fragment {
    private final String TAG = DiscoverMoviesFragment.this.getClass().getSimpleName();
    MovieAdapter moviesAdapter;
    @Bind(R.id.rvDiscoverMovies) RecyclerView rvDiscoverMovies;
    Context mContext;
    private DiscoverMoviesActivity mActivity;
    private ShareActionProvider mShareActionProvider;
    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG,"On create");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discover_movies,container,false);
        ButterKnife.bind(this,v);
        rvDiscoverMovies = (RecyclerView) v.findViewById(R.id.rvDiscoverMovies);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (DiscoverMoviesActivity)getActivity();
    }

    private ArrayList<Movie> getMoviesFromCursor (Cursor c){
        ArrayList<Movie> movies = new ArrayList<>();
        if (c.moveToFirst()){
            do{
                Movie currentMovie = new Movie();
                String id = c.getString(c.getColumnIndex(MoviesContract.FavoriteMovieEntry.COLUMN_ID));
                currentMovie.setId(Integer.valueOf(id));
                String title = c.getString(c.getColumnIndex(MoviesContract.FavoriteMovieEntry.COLUMN_TITLE));
                currentMovie.setTitle(title);
                String originalTitle = c.getString(c.getColumnIndex(MoviesContract.FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE));
                currentMovie.setOriginalTitle(originalTitle);
                String posterPath = c.getString(c.getColumnIndex(MoviesContract.FavoriteMovieEntry.COLUMN_POSTER));
                currentMovie.setPosterPath(posterPath);
                String overview = c.getString(c.getColumnIndex(MoviesContract.FavoriteMovieEntry.COLUMN_SYNOPSIS));
                currentMovie.setOverview(overview);
                Double userRating = c.getDouble(c.getColumnIndex(MoviesContract.FavoriteMovieEntry.COLUMN_USER_RATING));
                currentMovie.setVoteAverage(userRating);
                String releaseDate = c.getString(c.getColumnIndex(MoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE));
                currentMovie.setReleaseDate(String.valueOf(releaseDate));
                Log.v(TAG,"Release date: " + currentMovie.getReleaseDate());
                movies.add(currentMovie);
            }while(c.moveToNext());
        }
        c.close();
        return movies;
    }

    @Override
    public void onResume() {
        super.onResume();
        GridLayoutManager grid = new GridLayoutManager(mContext,detectRecyclerViewColsNumber());
        rvDiscoverMovies.setLayoutManager(grid);
        rvDiscoverMovies.setItemAnimator(new DefaultItemAnimator());
        moviesAdapter = new MovieAdapter(mContext);
        rvDiscoverMovies.setAdapter(moviesAdapter);
        // Retrieve preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String sortMode = preferences.getString(getString(R.string.pref_sorting_mode), getString(R.string.pref_sorting_default));
        if (!sortMode.matches("favorites")){
            new FetchMoviesTask().execute(sortMode);
        }else{
            Cursor mCursor = mContext.getContentResolver().query(MoviesContract.FavoriteMovieEntry.CONTENT_URI,null,null,null,null);
            ArrayList<Movie> movies = getMoviesFromCursor(mCursor);
            moviesAdapter.setItems(movies);
            moviesAdapter.notifyDataSetChanged();
        }
    }

    public class FetchMoviesTask extends AsyncTask<String,Void,ArrayList<Movie>> {
        final String TAG = FetchMoviesTask.class.getSimpleName();
        final String DISCOVER_MOVIES_API_URL = "http://api.themoviedb.org/3/discover/movie/";
        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            ArrayList<Movie> movies;
            try{
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(DISCOVER_MOVIES_API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                TheMovieDbApiService service = retrofit.create(TheMovieDbApiService.class);
                Call<ApiResponses.Discover> listMovies = service.listMovies(params[0]);
                ApiResponses.Discover myResponse = listMovies.execute().body();
                movies = myResponse.getMovies();
            }catch (Exception e){
                movies = null;
                Log.d(TAG, "Error fetching movies: " + e.getMessage());
            }
            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if (movies != null){
                moviesAdapter.setItems(movies);
                moviesAdapter.notifyDataSetChanged();

            }else{
                Log.d(TAG, "Error occurred during movies fetch");
            }
            super.onPostExecute(movies);
        }
    }

    public int detectRecyclerViewColsNumber(){
        int columns = 2;
        try{
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);
            float density  = getResources().getDisplayMetrics().density;
            float dpWidth;
            if (DiscoverMoviesActivity.isTwoPaneUI()){
                dpWidth = (outMetrics.widthPixels /2) / density;
            }else{
                dpWidth = (outMetrics.widthPixels) / density;
            }
            float defImgWidth = (int) (getResources().getDimension(R.dimen.pop_movies_image_width) / density);
            Log.v(TAG, "Def img width: " + defImgWidth);
            columns = Math.round(dpWidth/defImgWidth);
        }catch (Exception e){
            Log.d(TAG,"Cannot detect number of columns");
        }
        return columns;
    }
}
