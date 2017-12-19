package rodionkonioshko.com.popularmovies.data_and_adapters;


import android.os.Parcel;
import android.os.Parcelable;

import rodionkonioshko.com.popularmovies.utilities.NetworkUtils;

/**
 * <p>This class will be used to initialize movie instances
 * Fetch all the needed data from the json array,example below</p>
 * "original_title":"Dilwale Dulhania Le Jayenge"
 * "poster_path":"\/2gvbZMtV1Zsl7FedJa5ysbpBx2G.jpg"
 * "vote_average":9.1
 * "mPopularity":50.775033
 * "mOverview":"Raj is a rich, carefree, happy-go-lucky second generation NRI. ......
 * "release_date":"1995-10-20"
 * "id":123
 */
public final class MovieObject implements Parcelable
{
    //class name,mainly for debugging
    private static final String TAG = NetworkUtils.class.getSimpleName();
    //original_title from json object
    private final String mOriginalTitle;
    //poster_path size 185 from json object
    private final String mPosterPath185;
    //poster_path size 342 from json object
    private final String mPosterPath342;
    //poster_path size 500 from json object
    private final String mPosterPath500;
    //vote_average floating point value from json object
    private final Double mVoteAverage;
    //popularity floating point value from json object
    private final Double mPopularity;
    //overview from json object
    private final String mOverview;
    //release_date from json object
    private final String mReleaseDate;
    //id of the movie
    private final int mMovieId;
    //TMDB url for getting an image with size 185
    private static final String TMDB_BASE_URL_185 = "https://image.tmdb.org/t/p/w185/";
    //TMDB url for getting an image with size 342
    private static final String TMDB_BASE_URL_342 = "https://image.tmdb.org/t/p/w342/";
    //TMDB url for getting an immage with size 500
    private static final String TMDB_BASE_URL_500 = "https://image.tmdb.org/t/p/w500/";


    /**
     * @param originalTitle original_title from JSON Object
     * @param posterPath    poster_path from JSON Object
     * @param voteAverage   original_title from JSON Object
     * @param popularity    mPopularity from JSON Object
     * @param overview      mOverview from JSON Object
     * @param releaseDate   release_date" from JSON Object
     * @param movieId       movie id
     */
    public MovieObject(String originalTitle, String posterPath, Double voteAverage, Double popularity, String overview, String releaseDate, int movieId)
    {
        this.mOriginalTitle = originalTitle;
        this.mPosterPath185 = TMDB_BASE_URL_185 + posterPath.substring(1, posterPath.length());
        this.mPosterPath342 = TMDB_BASE_URL_342 + posterPath.substring(1, posterPath.length());
        this.mPosterPath500 = TMDB_BASE_URL_500 + posterPath.substring(1, posterPath.length());
        this.mVoteAverage = voteAverage;
        this.mPopularity = popularity;
        this.mOverview = overview;
        this.mReleaseDate = releaseDate;
        this.mMovieId = movieId;
    }


    private MovieObject(Parcel in)
    {
        mOriginalTitle = in.readString();
        mPosterPath185 = in.readString();
        mPosterPath342 = in.readString();
        mPosterPath500 = in.readString();
        if (in.readByte() == 0)
        {
            mVoteAverage = null;
        } else
        {
            mVoteAverage = in.readDouble();
        }
        if (in.readByte() == 0)
        {
            mPopularity = null;
        } else
        {
            mPopularity = in.readDouble();
        }
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mMovieId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mOriginalTitle);
        dest.writeString(mPosterPath185);
        dest.writeString(mPosterPath342);
        dest.writeString(mPosterPath500);
        if (mVoteAverage == null)
        {
            dest.writeByte((byte) 0);
        } else
        {
            dest.writeByte((byte) 1);
            dest.writeDouble(mVoteAverage);
        }
        if (mPopularity == null)
        {
            dest.writeByte((byte) 0);
        } else
        {
            dest.writeByte((byte) 1);
            dest.writeDouble(mPopularity);
        }
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeInt(mMovieId);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<MovieObject> CREATOR = new Creator<MovieObject>()
    {
        @Override
        public MovieObject createFromParcel(Parcel in)
        {
            return new MovieObject(in);
        }

        @Override
        public MovieObject[] newArray(int size)
        {
            return new MovieObject[size];
        }
    };

    //getters to return referenced  MovieObject data
    public String getmOriginalTitle()
    {
        return mOriginalTitle;
    }

    public String getmPosterPath185()
    {
        return mPosterPath185;
    }

    public Double getmVoteAverage()
    {
        return mVoteAverage;
    }

    public Double getmPopularity()
    {
        return mPopularity;
    }

    public String getmOverview()
    {
        return mOverview;
    }

    public String getmReleaseDate()
    {
        return mReleaseDate;
    }

    public String getmPosterPath342()
    {
        return mPosterPath342;
    }

    public String getmPosterPath500()
    {
        return mPosterPath500;
    }

    public int getmMovieId()
    {
        return mMovieId;
    }

}
