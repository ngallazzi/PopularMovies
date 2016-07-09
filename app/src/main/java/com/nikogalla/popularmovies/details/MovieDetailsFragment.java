package com.nikogalla.popularmovies.details;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nikogalla.popularmovies.R;
import com.nikogalla.popularmovies.data.MoviesContract;
import com.nikogalla.popularmovies.data.MoviesProvider;
import com.nikogalla.popularmovies.model.Movie;
import com.nikogalla.popularmovies.model.Review;
import com.nikogalla.popularmovies.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.nikogalla.popularmovies.details.FetchVideosTask.*;

import org.w3c.dom.Text;

/**
 * Created by Nicola on 2016-04-13.
 */
public class MovieDetailsFragment extends Fragment implements OnVideoTaskCompleteListener, FetchReviewsTask.OnReviewTaskCompleteListener {
    // Butterknife library
    private String TAG = MovieDetailsFragment.class.getSimpleName();
    @Bind(R.id.tvOriginalTitle) TextView tvOriginalTitle;
    @Bind(R.id.ivPoster) ImageView ivPoster;
    @Bind(R.id.tvReleaseDate) TextView tvReleaseDate;
    @Bind(R.id.tvUserRating) TextView tvUserRating;
    @Bind(R.id.tvPlot) TextView tvPlot;
    @Bind(R.id.btMarkAsFavorite) Button btMarkAsFavorite;
    @Bind(R.id.llTrailers) LinearLayout llTrailers;
    @Bind(R.id.llReviews) LinearLayout llReviews;
    Movie mParentMovie;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_details,container,false);
        ButterKnife.bind(this,v);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // rvVideos
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            mParentMovie = bundle.getParcelable(getString(R.string.movie_argument_name));
        }
        Log.v(TAG,"Activity created");
    }



    @Override
    public void onStart() {
        super.onStart();

    }

    private Uri insertMovieAsFavorite (Movie movie){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.FavoriteMovieEntry.COLUMN_ID, movie.getId());;
        contentValues.put(MoviesContract.FavoriteMovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MoviesContract.FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(MoviesContract.FavoriteMovieEntry.COLUMN_POSTER, movie.getPosterPath());
        contentValues.put(MoviesContract.FavoriteMovieEntry.COLUMN_SYNOPSIS, movie.getOverview());
        contentValues.put(MoviesContract.FavoriteMovieEntry.COLUMN_USER_RATING, movie.getVoteAverage());
        contentValues.put(MoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        Log.v(TAG,"Release date: " + movie.getReleaseDate());
        Uri uri = mContext.getContentResolver().insert(MoviesContract.FavoriteMovieEntry.CONTENT_URI,contentValues);
        return uri;
    }

    private void deleteMovieFromFavorites(Movie movie){
        Uri movieByIdUri = MoviesContract.FavoriteMovieEntry.buildMovieUriWithMovieId(String.valueOf(movie.getId()));
        int deletedRows = mContext.getContentResolver().delete(movieByIdUri,null,null);
        if (deletedRows>0){
            setButtonToNormalState();
        }
    }

    private boolean isFavorite (Movie movie){
        Uri movieByIdUri = MoviesContract.FavoriteMovieEntry.buildMovieUriWithMovieId(String.valueOf(movie.getId()));
        Cursor cursor = mContext.getContentResolver().query(movieByIdUri,null,null,null,null);
        if (cursor.moveToFirst()){
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mParentMovie!=null){
            // Original Title
            String originalTitle = mParentMovie.getOriginalTitle();
            tvOriginalTitle.setText(originalTitle);
            // Poster Image
            String posterPath = mParentMovie.getPosterPath();
            Picasso.with(mContext).load(posterPath).into(ivPoster);
            // Release Date
            String releaseDate = mParentMovie.getReleaseDate();
            if (releaseDate!=null){
                try{
                    releaseDate = releaseDate.substring(0,4);
                }catch (Exception e){
                    Log.d(TAG,e.getMessage());
                }
                tvReleaseDate.setText(releaseDate);
            }
            // Users rating
            Double rating = mParentMovie.getVoteAverage();
            tvUserRating.setText(String.valueOf(rating) +"/10");
            // Plot
            String plot = mParentMovie.getOverview();
            tvPlot.setText(plot);

            // Check if movie is favorite
            if (isFavorite(mParentMovie)){
                setButtonToFavoriteState();
            }else{
                setButtonToNormalState();
            }

            btMarkAsFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFavorite(mParentMovie)){
                        deleteMovieFromFavorites(mParentMovie);
                    }else{
                        insertMovieAsFavorite(mParentMovie);
                        setButtonToFavoriteState();
                    }
                }
            });
            // Fetch videos
            new FetchVideosTask(this).execute(mParentMovie.getId());
            // Fetch reviews
            new FetchReviewsTask(this).execute(mParentMovie.getId());
        }
    }

    private void setButtonToFavoriteState(){
        btMarkAsFavorite.setText(getString(R.string.remove_from_favorites));
        btMarkAsFavorite.getBackground().setColorFilter(ContextCompat.getColor(mContext,R.color.colorAccentMovies), PorterDuff.Mode.MULTIPLY);
    }
    private void setButtonToNormalState(){
        btMarkAsFavorite.setText(getString(R.string.add_to_favorites));
        btMarkAsFavorite.getBackground().setColorFilter(ContextCompat.getColor(mContext,R.color.colorPrimaryDarkMovies), PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void onVideosTaskComplete(ArrayList<Video> videos) {
        if (isAdded()) {
            if (videos.size() > 0) {
                LinearLayout titleView = (LinearLayout) View.inflate(mContext, R.layout.title_view, null);
                TextView tvTitle = (TextView) titleView.findViewById(R.id.tvTitle);
                tvTitle.setText(getString(R.string.trailers_label));
                llTrailers.addView(titleView);
                for (final Video video : videos) {
                    View videoView = View.inflate(mContext, R.layout.list_item_video, null);
                    TextView tvVideoLabel = (TextView) videoView.findViewById(R.id.tvVideoLabel);
                    tvVideoLabel.setText(video.getName());
                    llTrailers.addView(videoView);
                    ImageView ivPlayVideo = (ImageView) videoView.findViewById(R.id.ivPlayVideo);
                    ivPlayVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String videoPath = mContext.getString(R.string.youtube_watch_url) + video.getKey();
                            Log.v(TAG, "Video path: " + videoPath);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoPath));
                            mContext.startActivity(intent);
                        }
                    });
                    videoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, getString(R.string.tap_play_button), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onReviewsTaskComplete(ArrayList<Review> reviews) {
        if (isAdded()){
            if (reviews.size()>0){
                LinearLayout titleView = (LinearLayout) View.inflate(mContext, R.layout.title_view, null);
                TextView tvReviewsLabel = (TextView) titleView.findViewById(R.id.tvTitle);
                tvReviewsLabel.setText(getString(R.string.reviews_label));
                llReviews.addView(titleView);
                for (final Review review: reviews){
                    View reviewView = View.inflate(mContext,R.layout.list_item_review,null);
                    TextView tvAuthor = (TextView) reviewView.findViewById(R.id.tvAuthor);
                    tvAuthor.setText(review.getAuthor());
                    TextView tvContent = (TextView) reviewView.findViewById(R.id.tvContent);
                    tvContent.setText(review.getContent());
                    TextView tvUrl = (TextView) reviewView.findViewById(R.id.tvUrl);
                    tvUrl.setText(review.getUrl());
                    llReviews.addView(reviewView);
                }
            }
        }
    }
}
