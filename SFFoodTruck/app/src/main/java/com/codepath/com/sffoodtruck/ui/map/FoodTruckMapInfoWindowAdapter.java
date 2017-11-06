package com.codepath.com.sffoodtruck.ui.map;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.ui.util.DeviceDimensionsUtil;
import com.codepath.com.sffoodtruck.ui.util.YelpRatingBar;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.Map;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by robl2e on 10/24/17.
 */

public class FoodTruckMapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final LayoutInflater inflater;
    private final Map<String, FoodTruckMapViewModel> viewModelMap;

    public FoodTruckMapInfoWindowAdapter(LayoutInflater inflater, Map<String
            , FoodTruckMapViewModel> viewModelMap) {
        this.inflater = inflater;
        this.viewModelMap = viewModelMap;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        String id = (String) marker.getTag();
        FoodTruckMapViewModel viewModel = viewModelMap.get(id);
        if (viewModel == null) return null;


        // Getting view from the layout file
        View view = inflater.inflate(R.layout.view_food_truck_map_info_window, null);

        TextView nameTextView = (TextView)
                view.findViewById(R.id.text_name);
        nameTextView.setText(viewModel.getName());

        YelpRatingBar ratingBar = (YelpRatingBar)
                view.findViewById(R.id.rating_bar);
        ratingBar.setRating(viewModel.getRating());

        TextView priceTextView = (TextView)
                view.findViewById(R.id.text_price);
        priceTextView.setText(viewModel.getPrice());

        ImageView imageIcon = (ImageView) view.findViewById(R.id.image_icon);
        int radius = (int) DeviceDimensionsUtil
                .convertDpToPixel(8, imageIcon.getContext());
        Picasso.with(imageIcon.getContext())
                .load(viewModel.getImageUrl())
                .transform(new RoundedCornersTransformation(radius, 0))
                .resize(250, 250)
                .centerCrop()
                .into(imageIcon, new MarkerCallback(marker));
        return view;
    }
}
