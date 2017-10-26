package com.codepath.com.sffoodtruck.ui.util;

import android.app.Activity;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.codepath.com.sffoodtruck.R;
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
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        Picasso.Builder builder = new Picasso.Builder(view.getContext());
        builder.listener((picasso, uri, exception) -> exception.printStackTrace());
        builder.build()
                .load(url)
                .placeholder(R.drawable.placeholder160x160)
                .resize(width/2,0)
                .into(view);
    }

    @BindingAdapter({"bind:userImageUrl"})
    public static void loadReviewImage(ImageView view, String url){
        Picasso.Builder builder = new Picasso.Builder(view.getContext());
        builder.listener((picasso, uri, exception) -> exception.printStackTrace());
        builder.build()
                .load(url)
                .transform(new CircleTransform())
                .fit()
                .into(view);
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
