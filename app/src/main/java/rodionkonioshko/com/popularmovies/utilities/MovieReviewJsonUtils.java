package rodionkonioshko.com.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rodionkonioshko.com.popularmovies.data_and_adapters.ReviewObject;

public class MovieReviewJsonUtils
{
    //class name,mainly for debugging
    private static final String TAG = NetworkUtils.class.getSimpleName();
    //place that holds the reviews
    private static final String RESULTS = "results";
    //author of review
    private static final String AUTHOR = "author";
    //content of review
    private static final String CONTENT = "content";

    /**
     *
     * @param reviewJson raw Json review Object
     * @return parsed Json ReviewObject array
     * @throws JSONException
     */
    public static ReviewObject[] getJsonReviews(String reviewJson) throws JSONException
    {
        //Main Json Object
        JSONObject mainJson = new JSONObject(reviewJson);

        //Return the full results array of the current page
        JSONArray reviewArray = mainJson.getJSONArray(RESULTS);

        ReviewObject[] jsonReviewObjects = new ReviewObject[reviewArray.length()];

        for (int i = 0; i < reviewArray.length(); i++)
        {
            JSONObject reviewObject = reviewArray.getJSONObject(i);
            String author = reviewObject.getString(AUTHOR);
            String content = reviewObject.getString(CONTENT);

            jsonReviewObjects[i] = new ReviewObject(author, content);
        }
        return jsonReviewObjects;
    }
}
