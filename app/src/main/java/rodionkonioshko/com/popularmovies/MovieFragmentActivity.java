package rodionkonioshko.com.popularmovies;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import rodionkonioshko.com.popularmovies.data_and_adapters.ReviewObject;
import rodionkonioshko.com.popularmovies.data_and_adapters.TrailerObject;
import rodionkonioshko.com.popularmovies.database.MovieContract;
import rodionkonioshko.com.popularmovies.fragments.SimpleFragmentPagerAdapter;
import rodionkonioshko.com.popularmovies.utilities.NetworkUtils;


public class MovieFragmentActivity extends AppCompatActivity
{

    //class name,mainly for debugging
    private static final String TAG = MainActivity.class.getSimpleName();
    //id for trailer loader
    private static final int TRAILER_SEARCH_LOADER = 1;
    //id for review loader
    private static final int REVIEW_SEARCH_LOADER = 2;
    //trailerObjects that were fetched
    public static TrailerObject[] trailerObjectArr;
    //ReviewObjects that were fetched
    public static ReviewObject[] reviewObjectArr;
    //the dest that we will query our trailers from
    private String trailerQuery;
    //the dest that we will query our reviews from
    private String reviewQuery;
    //trailer that will be shared via the share intent
    private String shareTrailer = "";

    //loader callbacks for our trailerLoader
    private final LoaderCallbacks<TrailerObject[]> trailerLoader = new LoaderCallbacks<TrailerObject[]>()
    {
        @SuppressLint("StaticFieldLeak")
        @Override
        public Loader<TrailerObject[]> onCreateLoader(int id, Bundle args)
        {
            return new AsyncTaskLoader<TrailerObject[]>(MovieFragmentActivity.this)
            {
                TrailerObject[] arr;

                @Override
                public TrailerObject[] loadInBackground()
                {
                    String trailerQueryString = args.getString(trailerQuery);
                    if (trailerQueryString == null || trailerQueryString.isEmpty())
                        return null;

                    URL trailerQueryUrl = NetworkUtils.buildUrl(trailerQueryString);

                    try
                    {
                        trailerObjectArr = NetworkUtils.getResponseFromHttpUrlTrailers(trailerQueryUrl);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                        return null;
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                        return null;
                    }

                    return trailerObjectArr;
                }


                @Override
                protected void onStartLoading()
                {
                    super.onStartLoading();
                    if (args == null)
                        return;

                    if (arr != null)
                    {
                        deliverResult(arr);
                    } else
                        forceLoad();
                }

                @Override
                public void deliverResult(TrailerObject[] data)
                {
                    arr = data;
                    super.deliverResult(data);
                }
            };

        }

        @Override
        public void onLoadFinished(Loader<TrailerObject[]> loader, TrailerObject[] data)
        {
            initializeScreen();

            for (TrailerObject trailerObject : data)
            {
                if (trailerObject.getmKey() != null)
                {
                    shareTrailer = "http://www.youtube.com/watch?v=" + trailerObject.getmKey();
                    break;
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<TrailerObject[]> loader)
        {

        }
    };
    //loader callbacks for our reviewLoader
    private final LoaderCallbacks<ReviewObject[]> reviewLoader = new LoaderCallbacks<ReviewObject[]>()
    {
        @SuppressLint("StaticFieldLeak")
        @Override
        public Loader<ReviewObject[]> onCreateLoader(int id, Bundle args)
        {
            return new AsyncTaskLoader<ReviewObject[]>(MovieFragmentActivity.this)
            {
                ReviewObject[] arr;

                @Override
                public ReviewObject[] loadInBackground()
                {
                    String reviewQueryString = args.getString(reviewQuery);
                    if (reviewQueryString == null || reviewQueryString.isEmpty())
                        return null;

                    URL reviewQueryUrl = NetworkUtils.buildUrl(reviewQueryString);

                    try
                    {
                        reviewObjectArr = NetworkUtils.getResponseFromHttpUrlReviews(reviewQueryUrl);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                        return null;
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                        return null;
                    }

                    return reviewObjectArr;
                }

                @Override
                protected void onStartLoading()
                {
                    super.onStartLoading();
                    if (args == null)
                        return;

                    if (arr != null)
                    {
                        deliverResult(arr);
                    } else
                        forceLoad();
                }


                @Override
                public void deliverResult(ReviewObject[] data)
                {
                    arr = data;
                    super.deliverResult(data);
                }

            };
        }

        @Override
        public void onLoadFinished(Loader<ReviewObject[]> loader, ReviewObject[] data)
        {
            initializeScreen();
        }

        @Override
        public void onLoaderReset(Loader<ReviewObject[]> loader)
        {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_tab_viewpager);

        if (savedInstanceState == null)
        {
            Intent intent = getIntent();

            trailerQuery = NetworkUtils.getTrailerQuery(intent.getIntExtra("mId", 0));
            reviewQuery = NetworkUtils.getReviewQuery(intent.getIntExtra("mId", 0));


            Bundle trailerQueryBundle = new Bundle();
            trailerQueryBundle.putString(trailerQuery, trailerQuery);
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(TRAILER_SEARCH_LOADER, trailerQueryBundle, trailerLoader);

            Bundle reviewQueryBundle = new Bundle();
            reviewQueryBundle.putString(reviewQuery, reviewQuery);
            loaderManager.initLoader(REVIEW_SEARCH_LOADER, reviewQueryBundle, reviewLoader);
        } else
        {
            Parcelable[] trailerArr = savedInstanceState.getParcelableArray("trailerArr");
            Parcelable[] reviewArr = savedInstanceState.getParcelableArray("reviewArr");
            if (trailerArr != null)
                for (int i = 0; i < trailerArr.length; i++)
                {
                    trailerObjectArr[i] = (TrailerObject) trailerArr[i];
                }

            if (reviewArr != null)
            {
                for (int i = 0; i < reviewArr.length; i++)
                {
                    reviewObjectArr[i] = (ReviewObject) reviewArr[i];
                }
            }
            initializeScreen();
        }

    }

    /**
     * initialize the fragments,used synchronized to sync the 2 LoaderCallbacks Threads.
     */
    private synchronized void initializeScreen()
    {
        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
        // Find the tab layout that shows the tabs
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putParcelableArray("trailerArr", trailerObjectArr);
        outState.putParcelableArray("reviewArr", reviewObjectArr);
        outState.putInt("progressbarStatus", View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item_share_favorite, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        String mOriginalTitle;
        Cursor cursor;
        switch (id)
        {
            case (R.id.action_share):
                ShareCompat.IntentBuilder.from(MovieFragmentActivity.this)
                        .setChooserTitle("Awesome Movie Alert")
                        .setType("text/plain")
                        .setText
                                (
                                        "check out this movie" + "\n" +
                                                MovieFragmentActivity.this.getIntent().getStringExtra("mOriginalTitle") + "\n" +
                                                MovieFragmentActivity.this.getIntent().getStringExtra("mOverview") + "\n" +
                                                "shall we watch it?" + "\n" +
                                                shareTrailer

                                ).startChooser();
                return true;


            case (R.id.action_favorite):
                mOriginalTitle = MovieFragmentActivity.this.getIntent().getExtras().getString("mOriginalTitle");

                cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, MovieContract.MovieEntry._ID);

                if (!FavoritesActivity.alreadyInDb(cursor, mOriginalTitle))
                {
                    String mPosterPath185 = MovieFragmentActivity.this.getIntent().getExtras().getString("mPosterPath185");
                    String mOverview = MovieFragmentActivity.this.getIntent().getExtras().getString("mOverview");
                    int mId = MovieFragmentActivity.this.getIntent().getExtras().getInt("mId");

                    ContentValues insertValues = FavoritesActivity.insertContentValues(mId, mOriginalTitle, mOverview, mPosterPath185);

                    if (insertValues != null)
                    {
                        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, insertValues);
                        if (uri != null)
                            Toast.makeText(this, "added to favorites", Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    Toast.makeText(this, "already in favorites", Toast.LENGTH_SHORT).show();
                }

                break;


            case (R.id.action_unfavorite):
                cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, MovieContract.MovieEntry._ID);
                mOriginalTitle = MovieFragmentActivity.this.getIntent().getExtras().getString("mOriginalTitle");
                if (FavoritesActivity.alreadyInDb(cursor, mOriginalTitle))
                {
                    //used again because the top one will be closed after alreadyInDb check
                    cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, MovieContract.MovieEntry._ID);
                    int movieRowNumId = FavoritesActivity.deleteFavoriteMovie(cursor, mOriginalTitle);
                    if (movieRowNumId != 0)
                    {
                        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                        uri = uri.buildUpon().appendPath(Integer.toString(movieRowNumId)).build();
                        getContentResolver().delete(uri, null, null);
                        Toast.makeText(this, "removed from favorites", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            default:
                Log.e(TAG, "unknown menu action:" + id);

        }
        return super.onOptionsItemSelected(item);
    }

}
