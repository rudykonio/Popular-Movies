package rodionkonioshko.com.popularmovies.fragments;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import rodionkonioshko.com.popularmovies.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieClickFragment extends Fragment
{
    //movie original title
    private String mOriginalTitle;
    //185 poster path
    private String mPosterPath185;
    //vote value for that movie
    private Double mVoteAverage;
    //popularity of the movie
    private Double mPopularity;
    //description/plot of movie
    private String mOverview;
    //movie release data
    private String mReleaseDate;
    //342 poster path
    private String mPosterPath342;
    //500 poster path
    private String mPosterPath500;
    //movie id
    private int mId;

    public MovieClickFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.movie_click_fragment, container, false);


        //no need to save data twice because it is saved already in extras bundle
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null)
        {
            restoreExtras(extras);
        }
//        }
        //set poster image
        ImageView posterImageThumbnail = rootView.findViewById(R.id.poster_thumbnail);
        ProgressBar progressBar = rootView.findViewById(R.id.homeprogress);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && isTablet(getActivity().getBaseContext()))
        {
            Picasso.with(getActivity().getBaseContext()).load(mPosterPath342).into(posterImageThumbnail, new Callback()
            {
                @Override
                public void onSuccess()
                {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError()
                {

                }
            });
        } else
        {
            Picasso.with(getActivity().getBaseContext()).load(mPosterPath500).into(posterImageThumbnail, new Callback()
            {
                @Override
                public void onSuccess()
                {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError()
                {

                }
            });
        }
        //set movie rating+stars
        TextView movieVoteAverage = rootView.findViewById(R.id.movie_rating);
        movieVoteAverage.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "DroidSerif-Bold.ttf"));
        movieVoteAverage.setText(String.valueOf(mVoteAverage).concat("/10"));
        RatingBar ratingBar = rootView.findViewById(R.id.ratingBar);
        ratingBar.setRating(Float.parseFloat(String.valueOf(mVoteAverage / 2)));

        TextView inCinemas = rootView.findViewById(R.id.in_cinemas);
        inCinemas.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "DroidSerif-Regular.ttf"));

        //set release date
        TextView inCinemasValue = rootView.findViewById(R.id.in_cinemas_value);
        inCinemasValue.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "DroidSerif-Regular.ttf"));
        inCinemasValue.setText(mReleaseDate);

        //set movie title
        TextView movieTitle = rootView.findViewById(R.id.movie_title);
        movieTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "DroidSerif-Bold.ttf"));
        movieTitle.setText(mOriginalTitle);

        //set movie plot
        TextView moviePlot = rootView.findViewById(R.id.movie_plot);
        moviePlot.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "DroidSerif-Regular.ttf"));
        moviePlot.setText(mOverview);

        return rootView;
    }


    /**
     * checks and determines if we are working with a tablet
     *
     * @param context content of MovieClickActivity
     * @return true if tablet,false otherwise
     */

    private boolean isTablet(Context context)
    {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    /**
     * will restore the data from MainActivity
     *
     * @param extras that we got from MovieClickActivity
     */
    private void restoreExtras(Bundle extras)
    {
        mOriginalTitle = extras.getString("mOriginalTitle");
        mPosterPath185 = extras.getString("mPosterPath185");
        mVoteAverage = extras.getDouble("mVoteAverage");
        mPopularity = extras.getDouble("mPopularity");
        mOverview = extras.getString("mOverview");
        mReleaseDate = extras.getString("mReleaseDate");
        mPosterPath342 = extras.getString("mPosterPath342");
        mPosterPath500 = extras.getString("mPosterPath500");
        mId = extras.getInt("mId");
    }


    @Override
    public void onStart()
    {
        super.onStart();
    }


}
