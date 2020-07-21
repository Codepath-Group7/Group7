package com.codepath.com.sffoodtruck.ui.photodetail;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.codepath.com.sffoodtruck.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class PhotoDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PHOTO_URL = "photo_url";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        supportPostponeEnterTransition();
        ImageView ivPhoto = (ImageView) findViewById(R.id.iv_photo_detail);
        String photoUrl = getIntent().getStringExtra(EXTRA_PHOTO_URL);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        if(photoUrl != null){
            Picasso.with(this)
                    .load(photoUrl)
                    .resize(width,0)
                    .transform(new RoundedCornersTransformation(12,12))
                    .into(ivPhoto, new Callback() {
                        @Override
                        public void onSuccess() {
                            scheduleStartPostponedTransition((View) ivPhoto);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        supportStartPostponedEnterTransition();
                        return true;
                    }
                });
    }
}
