package rodionkonioshko.com.popularmovies.utilities;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rodionkonioshko.com.popularmovies.data_and_adapters.MovieObject;

/**
 * ImageUtility functions to handle TMDB JSON data.
 */

public final class MovieJsonUtils
{
    //class name,mainly for debugging
    private static final String TAG = NetworkUtils.class.getSimpleName();
    //const variable to access results
    private static final String RESULTS = "results";
    //original_title from json object
    private static final String ORIGINAL_TITLE = "original_title";
    //poster_path from json object
    private static final String POSTER_PATH = "poster_path";
    //vote_average from json object
    private static final String VOTE_AVERAGE = "vote_average";
    //popularity from json object
    private static final String POPULARITY = "popularity";
    //overview from json object
    private static final String OVERVIEW = "overview";
    //release_date from json object
    private static final String RELEASE_DATE = "release_date";
    //id from jston object
    private static final String ID = "id";


    /**
     * @param MovieJson Json response from server
     * @return Array of MovieObject describing Movie data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static MovieObject[] getJsonMovies(String MovieJson) throws JSONException
    {
        //Main JSON Object
        JSONObject mainJson = new JSONObject(MovieJson);
        //Return the full results array of the current page
        JSONArray movieArray = mainJson.getJSONArray(RESULTS);
        //MovieObject array that will store all the parsed json movie objects
        MovieObject[] jsonMovieObjects = new MovieObject[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++)
        {
            JSONObject movieObject = movieArray.getJSONObject(i);
            String originalTitle = movieObject.getString(ORIGINAL_TITLE);
            String posterPath = movieObject.getString(POSTER_PATH);
            Double voteAverage = movieObject.getDouble(VOTE_AVERAGE);
            Double popularity = movieObject.getDouble(POPULARITY);
            String overview = movieObject.getString(OVERVIEW);
            String releaseDate = movieObject.getString(RELEASE_DATE);
            int id = movieObject.getInt(ID);
            jsonMovieObjects[i] = new MovieObject(originalTitle, posterPath, voteAverage
                    , popularity, overview, releaseDate,id);
        }


        return jsonMovieObjects;
    }
}
