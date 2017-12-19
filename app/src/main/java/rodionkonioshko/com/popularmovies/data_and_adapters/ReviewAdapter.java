package rodionkonioshko.com.popularmovies.data_and_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rodionkonioshko.com.popularmovies.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>
{
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private final ReviewObject[] mReviewMovieObjectArray;

    public ReviewAdapter(ReviewObject[] reviewObjects)
    {
        mReviewMovieObjectArray = reviewObjects;
    }


    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new ReviewAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, int position)
    {
        holder.reviewerName.setText(mReviewMovieObjectArray[position].getmAuthor());
        holder.review.setText(mReviewMovieObjectArray[position].getmReview());
    }

    @Override
    public int getItemCount()
    {
        if (mReviewMovieObjectArray == null)
            return 0;
        return mReviewMovieObjectArray.length;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView reviewerName;
        private final TextView review;

        public ReviewViewHolder(View itemView)
        {
            super(itemView);
            reviewerName = itemView.findViewById(R.id.favoriteMovieTitle);
            review = itemView.findViewById(R.id.favoriteMovieDescription);
        }
    }

}
