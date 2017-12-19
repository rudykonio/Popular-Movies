package rodionkonioshko.com.popularmovies.data_and_adapters;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * class to represent each movie trailer as an Object,
 * save its movie name,id,and trailer key in a TrailerObject instance
 */
public class TrailerObject implements Parcelable
{
    //trailer id
    private final String mId;
    //trailer name
    private final String mName;
    //trailer key
    private final String mKey;

    public TrailerObject(String mId, String mName, String mKey)
    {
        this.mId = mId;
        this.mName = mName;
        this.mKey = mKey;
    }

    private TrailerObject(Parcel in)
    {
        mId = in.readString();
        mName = in.readString();
        mKey = in.readString();
    }

    public static final Creator<TrailerObject> CREATOR = new Creator<TrailerObject>()
    {
        @Override
        public TrailerObject createFromParcel(Parcel in)
        {
            return new TrailerObject(in);
        }

        @Override
        public TrailerObject[] newArray(int size)
        {
            return new TrailerObject[size];
        }
    };

    public String getmId()
    {
        return mId;
    }

    public  String getmName()
    {
        return mName;
    }

    public String getmKey()
    {
        return mKey;
    }

    @Override
    public String toString()
    {
        return "TrailerObject{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mKey='" + mKey + '\'' +
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
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mKey);
    }
}
