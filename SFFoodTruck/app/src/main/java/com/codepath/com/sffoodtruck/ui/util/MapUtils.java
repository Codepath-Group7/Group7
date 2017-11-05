package com.codepath.com.sffoodtruck.ui.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.com.sffoodtruck.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by robl2e on 10/10/17.
 */

public class MapUtils {

    private static final String TAG = MapUtils.class.getSimpleName();

    public static BitmapDescriptor createBubble(Context context, int style, String title) {
        IconGenerator iconGenerator = new IconGenerator(context);
        iconGenerator.setStyle(style);
        Bitmap bitmap = iconGenerator.makeIcon(title);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        return bitmapDescriptor;
    }

    public static Bitmap getMarkerBitmapFromView(Context context, @DrawableRes int resId, String iconText) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View customMarkerView = inflater.inflate(R.layout.view_food_truck_map_marker, null);

        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.image_icon);
        markerImageView.setImageResource(resId);

        TextView iconTextView = (TextView) customMarkerView.findViewById(R.id.text_name);
        iconTextView.setText(iconText);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();

        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null) drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public static Marker addMarker(GoogleMap map, LatLng point, String title,
                                   String snippet,
                                   BitmapDescriptor icon) {
        // Creates and adds marker to the map
        MarkerOptions options = new MarkerOptions()
                .position(point)
                .title(title)
                .snippet(snippet)
                .draggable(true)
                .icon(icon);

        Marker marker = map.addMarker(options);
        return marker;
    }

    public static String findLocation(Context context, Location location) {
        if (location == null) return null;

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        // lat,lng, your current location
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(), 1);
            if (addresses == null || addresses.isEmpty()) return null;

            return addresses.get(0).getPostalCode();
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    public static Address getLatLngFromAddress(Context context, String addressString){
        List<Address> address;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            address = geocoder.getFromLocationName(addressString,1);
            if (address==null || address.size() == 0) {
                return null;
            }

            /*p1 = new GeoPoint((double) (location.getLatitude() * 1E6),
                    (double) (location.getLongitude() * 1E6));*/
            Log.d(TAG,"Lat from Address--> "+address.get(0).getLatitude());
            Log.d(TAG,"Lng from Address--> "+address.get(0).getLongitude());
            return address.get(0);
        }catch (IOException e){
            return null;
        }
    }
}
