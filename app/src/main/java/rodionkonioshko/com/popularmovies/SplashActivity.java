package rodionkonioshko.com.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import rodionkonioshko.com.popularmovies.data_and_adapters.MovieObject;
import rodionkonioshko.com.popularmovies.utilities.NetworkUtils;

public class SplashActivity extends AppCompatActivity
{

    //class name,mainly for debugging
    private static final String TAG = MainActivity.class.getSimpleName();
    //parsed JSON response ordered by popularity
    public static MovieObject[] popularityMovieObjectArray;
    //parsed JSON response ordered by vote_average
    public static MovieObject[] voteAverageMovieObjectArray;
    //TextView that will display error data
    private TextView mErrorTextView;
    //error message that will be displayed when no internet
    private static String error;
    //will indicate if the data has been fetched
    private static boolean notAlreadyConnected = false;
    //byPopularityAndVoteAverage will hold an arrayList of the Json responses
    private final ArrayList<MovieObject[]> byPopularityAndVoteAverage = new ArrayList<>();
    //URL object that will be queried
    private URL queryUrlObjectPopularity;
    //URL object that will be queried
    private URL queryUrlObjectVoteAverage;
    //splash image
    private ImageView splashImage;
    //restart image button
    private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashImage = findViewById(R.id.splashImage);
        imageButton = findViewById(R.id.imageButton);
        Picasso.with(this).load(R.drawable.s720_1280).into(splashImage);


        //will display an error message if there is no network and will wait for the user to get online to start the app
        if (!isNetworkAvailable())
        {
            error = this.getResources().getString(R.string.no_inernet);
            mErrorTextView = findViewById(R.id.error_message);
            mErrorTextView.setText(error);
            mErrorTextView.setVisibility(View.VISIBLE);
            setTitle(R.string.app_name);
        }

        //user is online
        else
        {
            imageButton.setVisibility(View.INVISIBLE);
            splashImage.setVisibility(View.VISIBLE);
            String[] popularityAndVoteAverage = {NetworkUtils.getPopularQuery(), NetworkUtils.getTopRatedQuery()};
            new MovieQueryTask().execute(popularityAndVoteAverage);
        }
    }

//    asynctask to fetch all the data to start the MainActivity,no need to worry about a zombie process
//    possibility since the orientation can not be changed in this Activity.
    @SuppressLint("StaticFieldLeak")
    public class MovieQueryTask extends AsyncTask<String, Void, ArrayList<MovieObject[]>>
    {

        /**
         * Background task to fetch query data
         *
         * @param strings URL of TMDB query
         * @return JSON response of TMDB query
         */
        @Override
        protected ArrayList<MovieObject[]> doInBackground(String... strings)
        {

            //if params is empty,there is nothing to query
            if (strings.length == 0)
            {
                return null;
            }

            //URL string that will be queried by popularity
            String queryUrlStringPopularity = strings[0];

            //URL string that will be queried by vote_average
            String queryUrlStringVoteAverage = strings[1];

            for (int i = 0; i < 5; i++)
            {
                queryUrlObjectPopularity = NetworkUtils.buildUrl
                        (queryUrlStringPopularity + NetworkUtils.AND + NetworkUtils.PAGE + NetworkUtils.EQUALS + Integer.toString(i + 1));
                queryUrlObjectVoteAverage = NetworkUtils.buildUrl
                        (queryUrlStringVoteAverage + NetworkUtils.AND + NetworkUtils.PAGE + NetworkUtils.EQUALS + Integer.toString(i + 1));
                getPopularityAndVoteAverage();
                Log.v(TAG, "after job");
            }
            notAlreadyConnected = true;
            return byPopularityAndVoteAverage;

        }

        /**
         *
         * @param movieObjects the pared result from the TMDB query,will be put into a parcel and sent to MainActivity
         */
        @Override
        protected void onPostExecute(ArrayList<MovieObject[]> movieObjects)
        {
            popularityMovieObjectArray = new MovieObject[100];
            //fills the popularity array
            fillPopularity(popularityMovieObjectArray, movieObjects);

            voteAverageMovieObjectArray = new MovieObject[100];
            //fills the vote_average array
            fillVoteAverage(voteAverageMovieObjectArray, movieObjects);
            //clear the movieObjects ArrayList since no longer needed
            movieObjects.clear();
            //send the data from SplashActivity to MainActivity and start the MainActivity
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArray("popularityMovieObjectArray", popularityMovieObjectArray);
            bundle.putParcelableArray("voteAverageMovieObjectArray", voteAverageMovieObjectArray);
            intent.putExtra("parent", bundle);
            startActivity(intent);

        }
    }

    /**
     * get the required data for voteAverage array
     *
     * @param voteAverage  voteAverage MovieObject array
     * @param movieObjects ArrayList of all movies that we got from json
     */
    private void fillVoteAverage(MovieObject[] voteAverage, ArrayList<MovieObject[]> movieObjects)
    {
        MovieObject[] pageOne = movieObjects.get(1);
        MovieObject[] pageTwo = movieObjects.get(3);
        MovieObject[] pageThree = movieObjects.get(5);
        MovieObject[] pageFour = movieObjects.get(7);
        MovieObject[] pageFiver = movieObjects.get(9);
        for (int i = 0; i < 100; i++)
        {
            if (i >= 0 && i <= 19)
                voteAverage[i] = pageOne[i];
            else if (i >= 20 && i <= 39)
                voteAverage[i] = pageTwo[i - 20];
            else if (i >= 40 && i <= 59)
                voteAverage[i] = pageThree[i - 40];
            else if (i >= 60 && i <= 79)
                voteAverage[i] = pageFour[i - 60];
            else if (i >= 80 && i <= 99)
                voteAverage[i] = pageFiver[i - 80];
        }
    }

    /**
     * get the required data for popularity array
     *
     * @param popularity   popularity MovieObject array
     * @param movieObjects ArrayList of all movies that we got from json
     */
    private void fillPopularity(MovieObject[] popularity, ArrayList<MovieObject[]> movieObjects)
    {
        MovieObject[] pageOne = movieObjects.get(0);
        MovieObject[] pageTwo = movieObjects.get(2);
        MovieObject[] pageThree = movieObjects.get(4);
        MovieObject[] pageFour = movieObjects.get(6);
        MovieObject[] pageFive = movieObjects.get(8);
        for (int i = 0; i < 100; i++)
        {
            if (i >= 0 && i <= 19)
                popularity[i] = pageOne[i];
            else if (i >= 20 && i <= 39)
                popularity[i] = pageTwo[i - 20];
            else if (i >= 40 && i <= 59)
                popularity[i] = pageThree[i - 40];
            else if (i >= 60 && i <= 79)
                popularity[i] = pageFour[i - 60];
            else if (i >= 80 && i <= 99)
                popularity[i] = pageFive[i - 80];
        }
    }

    /**
     * @return true indicating there is internet connection,false otherwise.
     */
    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null)
        {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();


    }

    /**
     * will fetch the data for the current page given
     *
     * @return true if network operation succeeded,false otherwise
     */
    private boolean getPopularityAndVoteAverage()
    {
        try
        {
            popularityMovieObjectArray = NetworkUtils.getResponseFromHttpUrlMovies(queryUrlObjectPopularity);
            voteAverageMovieObjectArray = NetworkUtils.getResponseFromHttpUrlMovies(queryUrlObjectVoteAverage);
            byPopularityAndVoteAverage.add(popularityMovieObjectArray);
            byPopularityAndVoteAverage.add(voteAverageMovieObjectArray);
        } catch (IOException e)
        {
            mErrorTextView.setText(error);
            mErrorTextView.setVisibility(TextView.VISIBLE);
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * will check if there is network to actually start the app
     * @param view the reset button image
     */
    public void loadData(View view)
    {
        if (isNetworkAvailable())
        {
            if (!notAlreadyConnected)
            {

                mErrorTextView.setVisibility(View.INVISIBLE);
                splashImage.setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.INVISIBLE);
                String[] popularityAndVoteAverage = {NetworkUtils.getPopularQuery(), NetworkUtils.getTopRatedQuery()};
                new MovieQueryTask().execute(popularityAndVoteAverage);
            }
        }
    }

}
