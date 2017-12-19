package rodionkonioshko.com.popularmovies.data_and_adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import rodionkonioshko.com.popularmovies.MainActivity;
import rodionkonioshko.com.popularmovies.MovieFragmentActivity;
import rodionkonioshko.com.popularmovies.R;

/**
 * The MovieAdapter of the MovieObject
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>
{

    //class name,mainly for debugging
    private static final String TAG = MovieAdapter.class.getSimpleName();
    //parsed JSON response ordered by popularity,index 0
    private final MovieObject[] mPopularityMovieObjectArray;


    public MovieAdapter(MovieObject[] movieObjects)
    {
        mPopularityMovieObjectArray = movieObjects;
    }


    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MovieAdapter.MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position)
    {

        holder.mOriginalTitle = mPopularityMovieObjectArray[position].getmOriginalTitle();
        holder.mPosterPath185 = mPopularityMovieObjectArray[position].getmPosterPath185();
        holder.mPosterPath342 = mPopularityMovieObjectArray[position].getmPosterPath342();
        holder.mPosterPath500 = mPopularityMovieObjectArray[position].getmPosterPath500();
        holder.mVoteAverage = mPopularityMovieObjectArray[position].getmVoteAverage();
        holder.mPopularity = mPopularityMovieObjectArray[position].getmPopularity();
        holder.mOverview = mPopularityMovieObjectArray[position].getmOverview();
        holder.mReleaseDate = mPopularityMovieObjectArray[position].getmReleaseDate();
        holder.mId = mPopularityMovieObjectArray[position].getmMovieId();
        ImageView imageView = holder.mPoster;
        Context context = holder.itemView.getContext();
        //also possible to add placeholder and error but i prefer not to(i will display a message to indicate error instead).
        if (MainActivity.mNoOfColumns == 2)
            Picasso.with(context).load(holder.mPosterPath342).into(imageView);
        else if (MainActivity.mNoOfColumns == 10 || MainActivity.mNoOfColumns == 4)
            Picasso.with(context).load(holder.mPosterPath185).into(imageView);
    }

    @Override
    public int getItemCount()
    {
        if (mPopularityMovieObjectArray == null)
            return 0;
        return mPopularityMovieObjectArray.length;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
    {

        private final ImageView mPoster;
        private String mOriginalTitle;
        private String mPosterPath185;
        private String mPosterPath342;
        private String mPosterPath500;
        private Double mVoteAverage;
        private Double mPopularity;
        private String mOverview;
        private String mReleaseDate;
        private int mId;

        public MovieViewHolder(View itemView)
        {
            super(itemView);
            mPoster = itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(v ->
            {
                Intent i = new Intent(v.getContext(), MovieFragmentActivity.class);
                //put all the needed values for MovieClickActivity
                i.putExtra("mOriginalTitle", mOriginalTitle);
                i.putExtra("mPosterPath185", mPosterPath185);
                i.putExtra("mVoteAverage", mVoteAverage);
                i.putExtra("mPopularity", mPopularity);
                i.putExtra("mOverview", mOverview);
                i.putExtra("mReleaseDate", mReleaseDate);
                i.putExtra("mPosterPath342", mPosterPath342);
                i.putExtra("mPosterPath500", mPosterPath500);
                i.putExtra("mId", mId);
                v.getContext().startActivity(i);
            });
        }

    }

}


