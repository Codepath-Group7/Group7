package com.codepath.com.sffoodtruck.ui.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by saip92 on 10/18/2017.
 * Source from stackoverflow : https://stackoverflow.com/questions/18000093/how-to-marshall-and-unmarshall-a-parcelable-to-a-byte-array-with-help-of-parcel
 */

public class ParcelableUtil {

    public static byte[] marshall(Parcelable parcelable){
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel,0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static Parcel unmarshall(byte[] bytes){
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes,0,bytes.length);
        parcel.setDataPosition(0);//This is extremely important
        return parcel;
    }

    public static <T> T unmarshall(byte[] bytes, Parcelable.Creator<T> creator) {
        Parcel parcel = unmarshall(bytes);
        T result = creator.createFromParcel(parcel);
        parcel.recycle();
        return result;
    }

/*
Way to use this util class
        Unmarshalling (with CREATOR)

        byte[] bytes = …
        MyClass myclass = ParcelableUtil.unmarshall(bytes, MyClass.CREATOR);


        Unmarshalling (without CREATOR)

        byte[] bytes = …
        Parcel parcel = ParcelableUtil.unmarshall(bytes);
        MyClass myclass = new MyClass(parcel); // Or MyClass.CREATOR.createFromParcel(parcel).


        Marshalling

        MyClass myclass = …
        byte[] bytes = ParcelableUtil.marshall(myclass);
*/

}
