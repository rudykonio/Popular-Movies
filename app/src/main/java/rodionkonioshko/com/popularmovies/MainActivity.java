package rodionkonioshko.com.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import rodionkonioshko.com.popularmovies.data_and_adapters.MovieAdapter;
import rodionkonioshko.com.popularmovies.data_and_adapters.MovieObject;
import rodionkonioshko.com.popularmovies.utilities.ImageUtility;

public class MainActivity extends AppCompatActivity
{
    //class name,mainly for debugging
    private static final String TAG = MainActivity.class.getSimpleName();
    //parsed JSON response ordered by popularity
    public static MovieObject[] popularityMovieObjectArray = new MovieObject[100];
    //parsed JSON response ordered by vote_average
    public static MovieObject[] voteAverageMovieObjectArray = new MovieObject[100];
    //activity_main.xml recycler
    private RecyclerView mRecyclerView;
    //flag to switch between popular and highest rated
    private static boolean firstTime = false;
    //number of cols in the grid
    public static int mNoOfColumns;
    //indicate load from parent intent
    private static boolean loadedFromIntent = false;
    //true if on popular
    private static boolean isPopular = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //retrieve the parcels from SplashActivity and save them in popularityMovieObjectArray & voteAverageMovieObjectArray accordingly
        Bundle bundle = getIntent().getBundleExtra("parent");
        if (savedInstanceState == null)
        {
            if (bundle != null && bundle.getParcelableArray("popularityMovieObjectArray") != null && bundle.getParcelableArray("voteAverageMovieObjectArray") != null)
            {

                Parcelable[] allParcelables = bundle.getParcelableArray("popularityMovieObjectArray");
                if (allParcelables != null)
                {
                    for (int i = 0; i < allParcelables.length; i++)
                    {
                        popularityMovieObjectArray[i] = (MovieObject) allParcelables[i];
                    }
                }
                allParcelables = bundle.getParcelableArray("voteAverageMovieObjectArray");
                if (allParcelables != null)
                {
                    for (int i = 0; i < allParcelables.length; i++)
                    {
                        voteAverageMovieObjectArray[i] = (MovieObject) allParcelables[i];
                    }
                }

                mRecyclerView = findViewById(R.id.recyclerview_movie);
                mRecyclerView.setVisibility(View.VISIBLE);
                //default screen will be by popularity
                changeScreenByChoice(popularityMovieObjectArray);
                firstTime = true;
                loadedFromIntent = true;

            }
        } else
        {
            mRecyclerView = findViewById(R.id.recyclerview_movie);
            mRecyclerView.setVisibility(View.VISIBLE);
            if (!savedInstanceState.getBoolean("isPopular"))
            {
                voteAverageMovieObjectArray = (MovieObject[]) savedInstanceState.getParcelableArray("voteAverageMovieObjectArray");
                changeScreenByChoice(voteAverageMovieObjectArray);
                setTitle(R.string.highest_rated_top_100);
                firstTime = false;
            } else
            {
                popularityMovieObjectArray = (MovieObject[]) savedInstanceState.getParcelableArray("popularityMovieObjectArray");
                changeScreenByChoice(popularityMovieObjectArray);
                firstTime = true;
            }
        }

    }


    /**
     * creates the options menu
     * index 0 for most popular
     * index 1 for highest rated
     * index 2 for favorites
     *
     * @param menu menu of MainActivity
     * @return true for menu creation
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setVisible(true);
        menu.getItem(2).setVisible(true);
        return true;

    }

    /**
     * R.id.most_popular_choice will change the layout to most popular if already in most popular nothing will happen
     * R.id.highest_rated_choice will change layout to highest rated if already in highest rated nothing will happen
     * R.id.favorites will start the favorites activity.
     *
     * @param item menu item clicked
     * @return true if no errors occurred,false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case (R.id.most_popular_choice):
                if (!firstTime)
                {
                    firstTime = true;
                    isPopular = true;
                    changeScreenByChoice(popularityMovieObjectArray);
                    setTitle(R.string.app_name);
                }
                break;

            case (R.id.highest_rated_choice):
                if (firstTime)
                {
                    firstTime = false;
                    isPopular = false;
                    changeScreenByChoice(voteAverageMovieObjectArray);
                    setTitle(R.string.highest_rated_top_100);
                }
                break;

            case (R.id.favorites):
                Intent intent = new Intent(this, FavoritesActivity.class);
                startActivity(intent);

                break;

            default:
                Log.e(TAG, "unknown menu action:" + id);
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * sets&inflates the MainActivity GridLayout to the one that has been request by the user(by popularity or highest votes)
     *
     * @param movieObjects array of MovieObject that will be define the GridLayout.
     */
    private void changeScreenByChoice(MovieObject[] movieObjects)
    {
        mNoOfColumns = ImageUtility.calculateNoOfColumns(getApplicationContext());
        if (mNoOfColumns > 2 && mNoOfColumns <= 4)
            mNoOfColumns = 4;
        else if (mNoOfColumns >= 5 && mNoOfColumns < 10)
            mNoOfColumns = 10;


        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(MainActivity.this, mNoOfColumns);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        MovieAdapter mMovieAdapter = new MovieAdapter(movieObjects);

        mRecyclerView.setAdapter(mMovieAdapter);

        mRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        //main objective is to save it once
        outState.putParcelableArray("popularityMovieObjectArray", popularityMovieObjectArray);
        outState.putParcelableArray("voteAverageMovieObjectArray", voteAverageMovieObjectArray);
        outState.putBoolean("isPopular", isPopular);
        outState.putBoolean("loadedFromIntent", loadedFromIntent);
    }

    /**
     * will make sure that MainActivity won't go back to SplashActivity.
     */
    @Override
    public void onBackPressed()
    {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
