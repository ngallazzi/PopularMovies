package com.nikogalla.popularmovies.details;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nikogalla.popularmovies.model.Movie;
import com.nikogalla.popularmovies.model.Video;
import com.nikogalla.popularmovies.service.ApiResponses;
import com.nikogalla.popularmovies.service.TheMovieDbApiService;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nicola on 2016-04-20.
 */
public class FetchVideosTask extends AsyncTask<Integer,Void,ArrayList<Video>> {
    final String TAG = FetchVideosTask.class.getSimpleName();
    private OnVideoTaskCompleteListener taskCompleteListener;

    public FetchVideosTask(OnVideoTaskCompleteListener listener) {
        this.taskCompleteListener = listener;
    }

    @Override
    protected ArrayList<Video> doInBackground(Integer... params) {
        ArrayList<Video> videos;
        try{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MovieDetailsActivity.MOVIE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            TheMovieDbApiService service = retrofit.create(TheMovieDbApiService.class);
            Call<ApiResponses.Videos> listMovieVideo = service.listMovieVideos(params[0]);
            ApiResponses.Videos myResponse = listMovieVideo.execute().body();
            videos = myResponse.getVideos();
        }catch (Exception e){
            videos = null;
            Log.d(TAG, "Error fetching videos: " + e.getMessage());
        }
        return videos;
    }

    @Override
    protected void onPostExecute(ArrayList<Video> videos) {
        if (videos != null){
            Log.v(TAG,"Videos " + videos.toString());
            taskCompleteListener.onVideosTaskComplete(videos);
        }else{
            Log.d(TAG, "Error occurred during movies fetch");
        }
        super.onPostExecute(videos);
    }

    public interface OnVideoTaskCompleteListener{
        void onVideosTaskComplete(ArrayList<Video> videos);
    }
}
