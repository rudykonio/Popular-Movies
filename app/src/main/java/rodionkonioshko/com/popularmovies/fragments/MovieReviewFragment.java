package rodionkonioshko.com.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rodionkonioshko.com.popularmovies.MovieFragmentActivity;
import rodionkonioshko.com.popularmovies.R;
import rodionkonioshko.com.popularmovies.data_and_adapters.ReviewAdapter;

public class MovieReviewFragment extends android.support.v4.app.Fragment
{

    public MovieReviewFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.movie_reviews, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_reviews);
        ReviewAdapter reviewAdapter = new ReviewAdapter(MovieFragmentActivity.reviewObjectArr);
        recyclerView.setAdapter(reviewAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        return view;
    }


}
