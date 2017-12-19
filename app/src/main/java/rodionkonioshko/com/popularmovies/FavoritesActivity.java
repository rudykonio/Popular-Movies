package rodionkonioshko.com.popularmovies;


import android.annotation.SuppressLint;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import rodionkonioshko.com.popularmovies.database.FavoriteMovieAdapter;
import rodionkonioshko.com.popularmovies.database.MovieContract;
import rodionkonioshko.com.popularmovies.database.MovieContract.MovieEntry;
import rodionkonioshko.com.popularmovies.database.MovieDbHelper;
import rodionkonioshko.com.popularmovies.utilities.ImageUtility;

//made everything static so i can work with it from an the FragmentActivity
public class FavoritesActivity extends AppCompatActivity
{

    private static SQLiteDatabase mDb;
    public static MovieDbHelper movieDbHelper;
    @SuppressLint("StaticFieldLeak")
    private static FavoriteMovieAdapter favoriteMovieAdapter;
    @SuppressLint("StaticFieldLeak")
    private static RecyclerView favoriteMovieRecycler;
    private static final String TAG = FavoritesActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;
    private static android.app.LoaderManager loaderManager;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static final LoaderCallbacks<Cursor> queryLoader = new LoaderCallbacks<Cursor>()
    {

        @SuppressLint("StaticFieldLeak")
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args)
        {
            return new AsyncTaskLoader<Cursor>(context)
            {
                @Override
                protected void onStartLoading()
                {
                    forceLoad();
                }

                @Override
                public Cursor loadInBackground()
                {
                    try
                    {
                        return context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                MovieEntry.COLUMN_MOVIE_ID);

                    } catch (Exception e)
                    {
                        Log.e(TAG, "Failed to asynchronously load data.");
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(Cursor data)
                {
                    super.deliverResult(data);
                }


            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data)
        {
            favoriteMovieAdapter = new FavoriteMovieAdapter(data, context);
            favoriteMovieRecycler.setAdapter(favoriteMovieAdapter);
//            favoriteMovieRecycler.addItemDecoration(new DividerItemDecoration(context,
//                    DividerItemDecoration.HORIZONTAL));


        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader)
        {
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        FavoritesActivity.context = getApplicationContext();

        int mNoOfColumns = ImageUtility.calculateNoOfColumns(getApplicationContext());
        if (mNoOfColumns > 2 && mNoOfColumns <= 4)
            mNoOfColumns = 4;
        else if (mNoOfColumns >= 5 && mNoOfColumns < 10)
            mNoOfColumns = 10;

        if (!isTablet(this))
            mNoOfColumns = 4;

        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(FavoritesActivity.this, mNoOfColumns);

        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        favoriteMovieRecycler = findViewById(R.id.recyclerview_favorites);
        favoriteMovieRecycler.setLayoutManager(gridLayoutManager);


        movieDbHelper = new MovieDbHelper(context);
        mDb = movieDbHelper.getWritableDatabase();

        loaderManager = getLoaderManager();
        loaderManager.initLoader(TASK_LOADER_ID, null, queryLoader);


    }

    /**
     * @param cursor the cursor that queries the entire table
     * @param title  movie title
     * @return returns the _id of the movie that needs to deleted not the TMDB movie id but its row id
     */
    public static int deleteFavoriteMovie(Cursor cursor, String title)
    {
        int id;
        try
        {
            while (cursor.moveToNext())
            {
                if (title.equals(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE))))
                {
                    id = cursor.getInt(cursor.getColumnIndex(MovieEntry._ID));
                    return id;
                }
            }
            return 0;
        } finally
        {
            cursor.close();
        }
    }

    /**
     * @param cursor the cursor that queries the entire table
     * @param title  movie title
     * @return true if found in the db(single table),false otherwise
     */
    public static boolean alreadyInDb(Cursor cursor, String title)
    {
        try
        {
            while (cursor.moveToNext())
            {
                if (title.equals(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE))))
                    return true;
            }
            return false;
        } finally
        {
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item_delete_favorites, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @param item the remove everything item
     * @return true if no errors occurred,false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_delete_all_favorites)
        {
            DialogInterface.OnClickListener dialogClickListener = (dialog, choice) ->
            {
                switch (choice)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteAndCreateNewDb();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("this action will delete all of your favorite movies, are you sure?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * deletes the db and creates a new one
     */
    private void deleteAndCreateNewDb()
    {
        context.deleteDatabase("movies.db");
        movieDbHelper = new MovieDbHelper(context);
        mDb = movieDbHelper.getWritableDatabase();
        //it it ok to query this way because the table is now empty
        Cursor cursor = getAllFavoriteMovies();

        favoriteMovieAdapter = new FavoriteMovieAdapter(cursor, context);
        favoriteMovieRecycler.setAdapter(favoriteMovieAdapter);

        favoriteMovieRecycler.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));
    }

    /**
     * queries the table on the ui thread,only ok if used in an empty table/db context
     *
     * @return Cursor object which has an empty table(only used with deleteAndCreateNewDb() method)
     */
    private static Cursor getAllFavoriteMovies()
    {
        return mDb.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID);
    }

    /**
     * @param movieID     id of movie Object
     * @param title       title of movie Object
     * @param description plot of movie Object
     * @param poster      URL poster of movie Object
     * @return the ContentValues Object that has all this data
     */
    public static ContentValues insertContentValues(int movieID, String title, String description, String poster)
    {


        ContentValues insertValues = new ContentValues();
        insertValues.put(MovieEntry.COLUMN_MOVIE_ID, movieID);
        insertValues.put(MovieEntry.COLUMN_MOVIE_TITLE, title);
        insertValues.put(MovieEntry.COLUMN_MOVIE_DESCRIPTION, description);
        insertValues.put(MovieEntry.COLUMN_SMALL_MOVIE_POSTER, poster);

        return insertValues;
    }

    private boolean isTablet(Context context)
    {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

}

