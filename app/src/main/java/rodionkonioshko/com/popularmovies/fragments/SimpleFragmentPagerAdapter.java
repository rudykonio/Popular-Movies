package rodionkonioshko.com.popularmovies.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import rodionkonioshko.com.popularmovies.R;

/**
 * adapter class for the fragments
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter
{
    private final Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        mContext = context;
    }

    /**
     *
     * @param position current fragment
     * @return current Fragment new Object
     */
    @Override
    public Fragment getItem(int position)
    {
        if (position == 0)
        {
            return new MovieClickFragment();
        } else if (position == 1)
        {
            return new MovieTrailerFragment();
        } else
            return new MovieReviewFragment();
    }

    /**
     *
     * @return number of fragments
     */
    @Override
    public int getCount()
    {
        return 3;
    }

    /**
     *
     * @param position position of fragment
     * @return CharSequence value of fragment title
     */
    @Override
    public CharSequence getPageTitle(int position)
    {
        if (position == 0)
            return mContext.getString(R.string.plot);
        else if (position == 1)
            return mContext.getString(R.string.trailers);
        else return mContext.getString(R.string.reviews);
    }
}