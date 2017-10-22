package com.codepath.com.sffoodtruck.ui.util;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

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
    @BindingAdapter({"bind:imageUrl"})
    public static void loadSmallImage(ImageView view, String url){
        Picasso.Builder builder = new Picasso.Builder(view.getContext());
        builder.listener((picasso, uri, exception) -> exception.printStackTrace());
        builder.build().load(url).into(view);
    }

    @BindingAdapter({"bind:roundedCornersImageUrl"})
    public static void loadRoundedCorners(ImageView view,String url){
        Picasso.with(view.getContext())
                .load(url)
                .fit()
                .transform(new RoundedCornersTransformation(12,12))
                .into(view);
    }
}
