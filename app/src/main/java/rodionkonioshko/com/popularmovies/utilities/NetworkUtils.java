package rodionkonioshko.com.popularmovies.utilities;


import android.net.Uri;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import rodionkonioshko.com.popularmovies.BuildConfig;
import rodionkonioshko.com.popularmovies.data_and_adapters.MovieObject;
import rodionkonioshko.com.popularmovies.data_and_adapters.ReviewObject;
import rodionkonioshko.com.popularmovies.data_and_adapters.TrailerObject;

/**
 * Main class for network communication of PopularMovies.
 */
public final class NetworkUtils
{
    /*
     * The way TMDB API should be queried.
     * popular will return the data ordered by the popularity value descending
     * top_rated will return the data ordered by the top_rated value descending
     * URL below will fetch the JSON data of the current page where ? is the current page(page=?).
     * http://api.themoviedb.org/3/movie/popular?api_key=???????&page=?
     * http://api.themoviedb.org/3/movie/top_rated?api_key=???????&page=?
     */

    //class name,mainly for debugging
    private static final String TAG = NetworkUtils.class.getSimpleName();
    //TMDB URL Authority
    private static final String TMDB_URL_AUTHORITY = "http://api.themoviedb.org/";
    //TMDB URL Path
    private static final String TMDB_URL_PATH = "3/movie/";
    //TMDB popular option
    private static final String POPULAR = "popular";
    //TMDB top rated option
    private static final String TOP_RATED = "top_rated";
    //TMDB query begins with ?
    private static final String QUERY = "?";
    //API key for the query
    private static final String API_KEY = "api_key";
    //personal API Key that is used to query TMDB
    private static final String API_KEY_VALUE = BuildConfig.API_KEY;
    //equal sign in order to query page
    public static final String EQUALS = "=";
    //and sing in order to query by page
    public static final String AND = "&";
    //page of the json response
    public static final String PAGE = "page";
    //final URL that will be used to query by popular
    private static final String POPULAR_QUERY = "" +
            TMDB_URL_AUTHORITY + TMDB_URL_PATH + POPULAR + QUERY + API_KEY + EQUALS + API_KEY_VALUE;
    //final URL that will be used to query by page
    private static final String TOP_RATED_QUERY = "" +
            TMDB_URL_AUTHORITY + TMDB_URL_PATH + TOP_RATED + QUERY + API_KEY + EQUALS + API_KEY_VALUE;
    //will represent trailer id,each network request can handle one movie that's why this implementation is ok
    public static int trailerId;
    //videos path for trailer
    private static final String VIDEOS = "/videos";
    // path for reviews
    private static final String REVIEWS = "/reviews";


    /**
     * Builds the URL used to query TMDB.
     * Default option will be popular.
     *
     * @param option used to distinguish between {@link NetworkUtils#POPULAR_QUERY} and{@link NetworkUtils#TOP_RATED_QUERY} .
     * @return The URL to use to query the TMDB.
     */
    public static URL buildUrl(String option)
    {
        Uri builtUri = Uri.parse(option).buildUpon().build();

        URL url = null;
        try
        {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            Log.e(TAG, "Can not build URL" + e.getMessage());
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static MovieObject[] getResponseFromHttpUrlMovies(URL url) throws IOException, JSONException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try
        {

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput)
            {
                String httpResponse = scanner.next();
                return MovieJsonUtils.getJsonMovies(httpResponse);
            } else
            {

                Log.e(TAG, "Can not fetch data");
                return null;
            }
        } finally
        {
            urlConnection.disconnect();
        }
    }


    public static TrailerObject[] getResponseFromHttpUrlTrailers(URL url) throws IOException, JSONException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try
        {

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput)
            {
                String httpResponse = scanner.next();
                return MovieTrailerJsonUtils.getJsonTrailers(httpResponse);
            } else
            {

                Log.e(TAG, "Can not fetch data");
                return null;
            }
        } finally
        {
            urlConnection.disconnect();
        }
    }

    public static ReviewObject[] getResponseFromHttpUrlReviews(URL url) throws IOException, JSONException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try
        {

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput)
            {
                String httpResponse = scanner.next();
                return MovieReviewJsonUtils.getJsonReviews(httpResponse);
            } else
            {

                Log.e(TAG, "Can not fetch data");
                return null;
            }
        } finally
        {
            urlConnection.disconnect();
        }
    }


    /**
     * @return {@link NetworkUtils#POPULAR_QUERY}
     */
    public static String getPopularQuery()
    {
        return POPULAR_QUERY;
    }

    /**
     * @return {@link NetworkUtils#TOP_RATED_QUERY}
     */
    public static String getTopRatedQuery()
    {
        return TOP_RATED_QUERY;
    }

    //for example http://api.themoviedb.org/3/movie/19404/videos?api_key=?????????
    public static String getTrailerQuery(int movieId)
    {
        return
                "" + TMDB_URL_AUTHORITY + TMDB_URL_PATH + Integer.toString(movieId) + VIDEOS + QUERY + API_KEY + EQUALS + API_KEY_VALUE;
    }

    //for example http://api.themoviedb.org/3/movie/346364/reviews?api_key=????????????
    public static String getReviewQuery(int movieId)
    {
        return
                "" + TMDB_URL_AUTHORITY + TMDB_URL_PATH + Integer.toString(movieId) + REVIEWS + QUERY + API_KEY + EQUALS + API_KEY_VALUE;
    }
}

