package rodionkonioshko.com.popularmovies.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract
{


    private MovieContract()
    {
    }

    public static final String AUTHORITY = "rodionkonioshko.com.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";

    public static class MovieEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favorite_movies";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_DESCRIPTION = "description";
        public static final String COLUMN_SMALL_MOVIE_POSTER = "poster";
    }
}








