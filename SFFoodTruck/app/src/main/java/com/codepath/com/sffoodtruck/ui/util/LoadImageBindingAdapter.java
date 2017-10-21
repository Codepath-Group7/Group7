package com.codepath.com.sffoodtruck.ui.util;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
    @BindingAdapter({"bind:imageUrl"})
    public static void loadSmallImage(ImageView view, String url){
        Picasso.Builder builder = new Picasso.Builder(view.getContext());
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }});

        builder.build().load(url).into(view);
    }
}
