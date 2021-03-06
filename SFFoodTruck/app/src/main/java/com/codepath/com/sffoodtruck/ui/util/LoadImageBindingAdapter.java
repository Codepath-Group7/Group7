package com.codepath.com.sffoodtruck.ui.util;

import android.app.Activity;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.codepath.com.sffoodtruck.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by saip92 on 10/15/2017.
 */

public class LoadImageBindingAdapter {

    @BindingAdapter({"bind:imageUrl","bind:error"})
    public static void loadImageUrl(ImageView view, String url, Drawable error){
        Log.d("Adapter","This method is being called with : " + url);
        if(!TextUtils.isEmpty(url))
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


    @BindingAdapter({"bind:roundedCornersImageUrl"})
    public static void loadRoundedCorners(ImageView view,String url){
        Picasso.with(view.getContext())
                .load(url)
                .fit()
                .transform(new RoundedCornersTransformation(12,12))
                .into(view);

    }

    @BindingAdapter({"app:loadingImage"})
    public static void loadToolbarIcon(Toolbar toolbar, Uri url){
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d("HIYA", "onBitmapLoaded");
                Bitmap b = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
                CircleTransform circleTransform = new CircleTransform();
                Bitmap roundedBitmap = circleTransform.transform(b);
                BitmapDrawable icon = new BitmapDrawable(toolbar.getResources(), roundedBitmap);
                toolbar.setLogo(icon);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("HIYA", "onBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d("HIYA", "onPrepareLoad");
            }
        };

        Picasso.with(toolbar.getContext())
                .load(url)
                .into(target);

    }

    @BindingAdapter({"bind:loadCircleImage"})
    public static void loadCircleImage(ImageView view, Uri uri){
        Picasso.with(view.getContext())
                .load(uri)
                .transform(new CircleTransform())
                .into(view);
    }

    @BindingAdapter({"bind:loadCircleImage"})
    public static void loadCircleImage(ImageView view, String url){
        Picasso.with(view.getContext())
                .load(url)
                .transform(new CircleTransform())
                .into(view);
    }



}
