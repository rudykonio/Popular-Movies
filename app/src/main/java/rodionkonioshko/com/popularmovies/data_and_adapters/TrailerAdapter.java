package rodionkonioshko.com.popularmovies.data_and_adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rodionkonioshko.com.popularmovies.R;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>
{
    //class name,mainly for debugging
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private final TrailerObject[] mTrailerMovieObjectArray;


    public TrailerAdapter(TrailerObject[] trailerObjects)
    {
        mTrailerMovieObjectArray = trailerObjects;
    }


    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new TrailerAdapter.TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerViewHolder holder, int position)
    {
        holder.trailerName.setText(mTrailerMovieObjectArray[position].getmName());
        holder.youtubeKey = mTrailerMovieObjectArray[position].getmKey();
    }

    @Override
    public int getItemCount()
    {
        if (mTrailerMovieObjectArray == null)
            return 0;
        return mTrailerMovieObjectArray.length;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder
    {

        private final TextView trailerName;
        private String youtubeKey;

        public TrailerViewHolder(View itemView)
        {
            super(itemView);
            trailerName = itemView.findViewById(R.id.trailer_title);
            itemView.setOnClickListener(v -> watchYoutubeVideo(itemView.getContext(), youtubeKey));
        }


        private void watchYoutubeVideo(Context context, String key)
        {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + key));
            try
            {
                context.startActivity(appIntent);
            } catch (ActivityNotFoundException ex)
            {
                context.startActivity(webIntent);
            }
        }
    }
}