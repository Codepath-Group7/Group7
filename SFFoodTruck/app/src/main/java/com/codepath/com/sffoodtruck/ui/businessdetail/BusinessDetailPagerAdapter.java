package com.codepath.com.sffoodtruck.ui.businessdetail;

import android.content.Context;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.businessdetail.info.BusinessDetailFragment;
import com.codepath.com.sffoodtruck.ui.businessdetail.photos.BusinessPhotosFragment;
import com.codepath.com.sffoodtruck.ui.businessdetail.reviews.BusinessReviewsFragment;
import com.codepath.com.sffoodtruck.ui.util.SmartFragmentStatePagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * Created by akshaymathur on 10/23/17.
 */

public class BusinessDetailPagerAdapter extends SmartFragmentStatePagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Info","Photos","Reviews"};
    private Context context;
    private Business mBusiness;

    public BusinessDetailPagerAdapter(FragmentManager fm, Context context, Business business) {
        super(fm);
        this.context = context;
        this.mBusiness = business;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return BusinessDetailFragment.newInstance(mBusiness);
            case 1: return BusinessPhotosFragment.newInstance(mBusiness);
            case 2: return BusinessReviewsFragment.newInstance(mBusiness);
            default: return BusinessDetailFragment.newInstance(mBusiness);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
