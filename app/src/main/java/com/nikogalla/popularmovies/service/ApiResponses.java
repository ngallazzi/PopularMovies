package com.nikogalla.popularmovies.service;

import com.google.gson.annotations.SerializedName;
import com.nikogalla.popularmovies.model.Movie;
import com.nikogalla.popularmovies.model.Review;
import com.nikogalla.popularmovies.model.Video;

import java.util.ArrayList;

/**
 * Created by Nicola on 2016-03-10.
 */
public class ApiResponses {
    public class Discover{
        @SerializedName("page")
        int page;
        @SerializedName("results")
        ArrayList<Movie> movies;
        public ArrayList<Movie> getMovies() {
            return movies;
        }
    }
    public class Videos{
        @SerializedName("id")
        Integer id;
        @SerializedName("results")
        ArrayList<Video> videos;
        public ArrayList<Video> getVideos() {
            return videos;
        }
    }
    public class Reviews{
        @SerializedName("id")
        Integer id;
        @SerializedName("page")
        Integer page;
        @SerializedName("results")
        ArrayList<Review> reviews;
        public ArrayList<Review> getReviews() {
            return reviews;
        }
    }
}
