package rodionkonioshko.com.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rodionkonioshko.com.popularmovies.MovieFragmentActivity;
import rodionkonioshko.com.popularmovies.R;
import rodionkonioshko.com.popularmovies.data_and_adapters.TrailerAdapter;

public class MovieTrailerFragment extends Fragment
{
    public MovieTrailerFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.movie_trailers, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_trailer);
        TrailerAdapter trailerAdapter = new TrailerAdapter(MovieFragmentActivity.trailerObjectArr);
        recyclerView.setAdapter(trailerAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        return view;
    }
}
