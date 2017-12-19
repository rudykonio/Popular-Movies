package rodionkonioshko.com.popularmovies.data_and_adapters;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * class to represent each movie review as an Object,
 * save its author and review in a ReviewObject instance
 */
public class ReviewObject implements Parcelable
{
    //reviewer name
    private final String mAuthor;
    //actual review
    private final String mReview;

    public ReviewObject(String mName, String mReview)
    {
        this.mAuthor = mName;
        this.mReview = mReview;
    }

    private ReviewObject(Parcel in)
    {
        mAuthor = in.readString();
        mReview = in.readString();
    }

    public static final Creator<ReviewObject> CREATOR = new Creator<ReviewObject>()
    {
        @Override
        public ReviewObject createFromParcel(Parcel in)
        {
            return new ReviewObject(in);
        }

        @Override
        public ReviewObject[] newArray(int size)
        {
            return new ReviewObject[size];
        }
    };

    public String getmAuthor()
    {
        return mAuthor;
    }

    public String getmReview()
    {
        return mReview;
    }

    @Override
    public String toString()
    {
        return "ReviewObject{" +
                "mAuthor='" + mAuthor + '\'' +
                ", mReview='" + mReview + '\'' +
                '}';
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mAuthor);
        dest.writeString(mReview);
    }
}
