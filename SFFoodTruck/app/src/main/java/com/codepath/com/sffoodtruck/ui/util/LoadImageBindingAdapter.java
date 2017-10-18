package com.codepath.com.sffoodtruck.ui.util;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by saip92 on 10/15/2017.
 */

public class LoadImageBindingAdapter {

    @BindingAdapter({"bind:imageUrl","bind:error"})
    public static void loadImageUrl(ImageView view, String url, Drawable error){
        Log.d("Adapter","This method is being called with : " + url);
        Picasso.with(view.getContext())
                .load(url)
                .fit()
                .error(error)
                .into(view);
    }
}
