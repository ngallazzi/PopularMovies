package com.nikogalla.myportfolio.popularmovies.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Nicola on 2016-03-10.
 */
public interface TheMovieDbApiService {
    final String API_KEY = "";
    @GET("/3/discover/movie?api_key="+API_KEY)
    Call<ApiResponses.Discover> listMovies(@Query("sort_by") String sortBy);
    @GET("/3/movie/{id}/videos?api_key="+API_KEY)
    Call<ApiResponses.Videos> listMovieVideos(@Path("id") Integer movieId);
    @GET("/3/movie/{id}/reviews?api_key="+API_KEY)
    Call<ApiResponses.Reviews> listMovieReviews(@Path("id") Integer movieId);
}
