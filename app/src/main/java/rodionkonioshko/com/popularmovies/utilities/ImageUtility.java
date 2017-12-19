package rodionkonioshko.com.popularmovies.utilities;

import android.content.Context;
import android.util.DisplayMetrics;


/**
 * Helper class to decide how many images will fit in the grid.
 */
public class ImageUtility
{
    /**
     * @param context MainActivity context
     * @return the number of cols possible within the device
     */
    public static int calculateNoOfColumns(Context context)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);

        if (noOfColumns < 2)
            return 2;
        else
            return noOfColumns;
    }
}
