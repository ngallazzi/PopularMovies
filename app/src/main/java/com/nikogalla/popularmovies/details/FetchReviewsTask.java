package com.nikogalla.popularmovies.details;

import android.os.AsyncTask;
import android.util.Log;

import com.nikogalla.popularmovies.model.Review;
import com.nikogalla.popularmovies.service.ApiResponses;
import com.nikogalla.popularmovies.service.TheMovieDbApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nicola on 2016-04-20.
 */
public class FetchReviewsTask extends AsyncTask<Integer,Void,ArrayList<Review>> {
    final String TAG = FetchReviewsTask.class.getSimpleName();
    private OnReviewTaskCompleteListener taskCompleteListener;

    public FetchReviewsTask(OnReviewTaskCompleteListener listener) {
        this.taskCompleteListener = listener;
    }

    @Override
    protected ArrayList<Review> doInBackground(Integer... params) {
        ArrayList<Review> reviews;
        try{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MovieDetailsActivity.MOVIE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            TheMovieDbApiService service = retrofit.create(TheMovieDbApiService.class);
            Log.v(TAG, "Movie id: " + params[0]);
            Call<ApiResponses.Reviews> listMovieReviews = service.listMovieReviews(params[0]);
            ApiResponses.Reviews myResponse = listMovieReviews.execute().body();
            reviews = myResponse.getReviews();
        }catch (Exception e){
            reviews = null;
            Log.d(TAG, "Error fetching reviews: " + e.getMessage());
        }
        return reviews;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        if (reviews != null){
            Log.v(TAG,"Reviews " + reviews.toString());
        }else{
            Log.d(TAG, "Error occurred during reviews fetch");
        }
        taskCompleteListener.onReviewsTaskComplete(reviews);
        super.onPostExecute(reviews);
    }

    public interface OnReviewTaskCompleteListener{
        void onReviewsTaskComplete(ArrayList<Review> reviews);
    }
}
