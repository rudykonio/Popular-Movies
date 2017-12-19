package rodionkonioshko.com.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rodionkonioshko.com.popularmovies.data_and_adapters.TrailerObject;

public class MovieTrailerJsonUtils
{
    //class name,mainly for debugging
    private static final String TAG = NetworkUtils.class.getSimpleName();
    //place that holds the trailers
    private static final String RESULTS = "results";
    //place that holds the id of the trailer
    private static final String ID = "id";
    //key of the trailer that will be accessed via the youtube app
    private static final String KEY = "key";
    //name of the trailer that will be accessed via the youtube app
    private static final String NAME = "name";

    /**
     * @param trailerJson  raw Json trailer Object
     * @return parsed Json TrailerObject array
     * @throws JSONException
     */
    public static TrailerObject[] getJsonTrailers(String trailerJson) throws JSONException
    {
        //Main Json Object
        JSONObject mainJson = new JSONObject(trailerJson);
        //Return the full results array of the current page
        JSONArray trailerArray = mainJson.getJSONArray(RESULTS);

        TrailerObject[] jsonTrailerObjects = new TrailerObject[trailerArray.length()];

        for (int i = 0; i < trailerArray.length(); i++)
        {
            JSONObject trailerObject = trailerArray.getJSONObject(i);
            String id = trailerObject.getString(ID);
            String key = trailerObject.getString(KEY);
            String name = trailerObject.getString(NAME);

            jsonTrailerObjects[i] = new TrailerObject(id, name, key);
        }

        return jsonTrailerObjects;
    }

}

