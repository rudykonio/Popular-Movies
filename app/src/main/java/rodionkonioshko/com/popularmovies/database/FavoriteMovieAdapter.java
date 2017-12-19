package rodionkonioshko.com.popularmovies.database;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import rodionkonioshko.com.popularmovies.MainActivity;
import rodionkonioshko.com.popularmovies.MovieFragmentActivity;
import rodionkonioshko.com.popularmovies.R;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>
{
    private final Cursor mCursor;
    private final Context mContext;

    public FavoriteMovieAdapter(Cursor mCursor, Context mContext)
    {
        this.mCursor = mCursor;
        this.mContext = mContext;
    }

    @Override
    public FavoriteMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.favorite_list_item, parent, false);
        return new FavoriteMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteMovieViewHolder holder, int position)
    {
        if (!mCursor.moveToPosition(position))
            return;

        int favoriteMovieID = mCursor.getInt(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));

        holder.favoriteMovieID.setText(Integer.toString(favoriteMovieID));
        Picasso.with(mContext).load(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SMALL_MOVIE_POSTER)))
                .into(holder.smallPoster);

        holder.smallPoster.setOnClickListener(v ->
        {

            for (int i = 0; i < MainActivity.voteAverageMovieObjectArray.length; i++)
            {
                if (favoriteMovieID == MainActivity.voteAverageMovieObjectArray[i].getmMovieId())
                {
                    Intent intent = new Intent(v.getContext(), MovieFragmentActivity.class);
                    intent.putExtra("mOriginalTitle", MainActivity.voteAverageMovieObjectArray[i].getmOriginalTitle());
                    intent.putExtra("mPosterPath185", MainActivity.voteAverageMovieObjectArray[i].getmPosterPath185());
                    intent.putExtra("mVoteAverage", MainActivity.voteAverageMovieObjectArray[i].getmVoteAverage());
                    intent.putExtra("mPopularity", MainActivity.voteAverageMovieObjectArray[i].getmPopularity());
                    intent.putExtra("mOverview", MainActivity.voteAverageMovieObjectArray[i].getmOverview());
                    intent.putExtra("mReleaseDate", MainActivity.voteAverageMovieObjectArray[i].getmReleaseDate());
                    intent.putExtra("mPosterPath342", MainActivity.voteAverageMovieObjectArray[i].getmPosterPath342());
                    intent.putExtra("mPosterPath500", MainActivity.voteAverageMovieObjectArray[i].getmPosterPath500());
                    intent.putExtra("mId", MainActivity.voteAverageMovieObjectArray[i].getmMovieId());
                    v.getContext().startActivity(intent);
                }
            }

            for (int i = 0; i < MainActivity.popularityMovieObjectArray.length; i++)
            {
                if (favoriteMovieID == MainActivity.popularityMovieObjectArray[i].getmMovieId())
                {
                    Intent intent = new Intent(v.getContext(), MovieFragmentActivity.class);
                    intent.putExtra("mOriginalTitle", MainActivity.popularityMovieObjectArray[i].getmOriginalTitle());
                    intent.putExtra("mPosterPath185", MainActivity.popularityMovieObjectArray[i].getmPosterPath185());
                    intent.putExtra("mVoteAverage", MainActivity.popularityMovieObjectArray[i].getmVoteAverage());
                    intent.putExtra("mPopularity", MainActivity.popularityMovieObjectArray[i].getmPopularity());
                    intent.putExtra("mOverview", MainActivity.popularityMovieObjectArray[i].getmOverview());
                    intent.putExtra("mReleaseDate", MainActivity.popularityMovieObjectArray[i].getmReleaseDate());
                    intent.putExtra("mPosterPath342", MainActivity.popularityMovieObjectArray[i].getmPosterPath342());
                    intent.putExtra("mPosterPath500", MainActivity.popularityMovieObjectArray[i].getmPosterPath500());
                    intent.putExtra("mId", MainActivity.popularityMovieObjectArray[i].getmMovieId());
                    v.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return mCursor.getCount();
    }


    public class FavoriteMovieViewHolder extends RecyclerView.ViewHolder
    {
        final TextView favoriteMovieID;
        final ImageView smallPoster;

        FavoriteMovieViewHolder(View itemView)
        {
            super(itemView);

            favoriteMovieID = itemView.findViewById(R.id.favoriteMovieId);
            smallPoster = itemView.findViewById(R.id.smallPoster);
        }
    }

}
