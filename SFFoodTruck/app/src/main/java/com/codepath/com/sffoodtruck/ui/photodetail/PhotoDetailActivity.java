package com.codepath.com.sffoodtruck.ui.photodetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.codepath.com.sffoodtruck.R;
import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PHOTO_URL = "photo_url";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        ImageView ivPhoto = (ImageView) findViewById(R.id.iv_photo_detail);
        String photoUrl = getIntent().getStringExtra(EXTRA_PHOTO_URL);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        if(photoUrl != null){
            Picasso.with(this)
                    .load(photoUrl)
                    .resize(width,0)
                    .into(ivPhoto);
        }
    }
}
