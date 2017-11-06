package com.codepath.com.sffoodtruck.ui.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.codepath.com.sffoodtruck.R;


/**
 * Created by saip92 on 11/6/2017.
 * A custom view that displays yelp style's rating bar
 */

public class YelpRatingBar extends LinearLayout {

    private float rating;

    public YelpRatingBar(Context context) {
        super(context);
    }

    public YelpRatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        setupAttributes(attrs);
    }

    public void setRating(float rating) {
        this.rating = rating;
        init();
        invalidate();
        requestLayout();
    }

    private void setupAttributes(AttributeSet attrs) {
        TypedArray a = getContext()
                .getTheme()
                .obtainStyledAttributes(attrs, R.styleable.YelpRatingBar,0,0);
        try{
            rating = a.getFloat(R.styleable.YelpRatingBar_rating,0.0f);
        }finally {
            a.recycle();
        }
    }

    private void init(){

        float rating;

        if(this.rating > 5.0){
            rating = this.rating % 5;
        }else{
            rating = this.rating;
        }

       // Log.d("RatingBar", "Rating: " +  rating + ", got rating: " + this.rating);
        ImageView imageView = new ImageView(getContext());

        if(rating == 0 || (rating > 0 && rating < 1)){
            imageView.setImageDrawable
                    (ContextCompat.getDrawable(getContext(),R.drawable.stars_regular_0));
        }else if(rating == 1){
            imageView.setImageDrawable
                    (ContextCompat.getDrawable(getContext(),R.drawable.stars_regular_1));
        }else if(rating > 1 && rating < 2){
            imageView.setImageDrawable
                    (ContextCompat.getDrawable(getContext(),R.drawable.stars_regular_1_half));
        }else if(rating == 2){
            imageView.setImageDrawable
                    (ContextCompat.getDrawable(getContext(),R.drawable.stars_regular_2));
        }else if(rating > 2 && rating < 3){
            imageView.setImageDrawable
                    (ContextCompat.getDrawable(getContext(),R.drawable.stars_regular_2_half));
        }else if(rating == 3){
            imageView.setImageDrawable
                    (ContextCompat.getDrawable(getContext(),R.drawable.stars_regular_3));
        }else if(rating > 3 && rating < 4){
            imageView.setImageDrawable
                    (ContextCompat.getDrawable(getContext(),R.drawable.stars_regular_3_half));
        }else if(rating == 4){
            imageView.setImageDrawable
                    (ContextCompat.getDrawable(getContext(),R.drawable.stars_regular_4));
        }else if(rating > 4 && rating < 5){
            imageView.setImageDrawable
                    (ContextCompat.getDrawable(getContext(),R.drawable.stars_regular_4_half));
        }else if(rating == 5){
            imageView.setImageDrawable
                    (ContextCompat.getDrawable(getContext(),R.drawable.stars_regular_5));
        }else{
            imageView.setImageDrawable
                    (ContextCompat.getDrawable(getContext(),R.drawable.stars_regular_0));
        }

        addView(imageView);
    }



}
