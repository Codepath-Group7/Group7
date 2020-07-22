package com.codepath.com.sffoodtruck.ui.homefeed;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.core.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.databinding.DialogShareBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Created by saip92 on 10/31/2017.
 */

public class ShareBottomSheet extends BottomSheetDialogFragment {

    public static final String EXTRA_BUSINESS = "ShareBottomSheet.EXTRA_BUSINESS";
    private DialogShareBottomSheetBinding mBinding;
    private Business mBusiness;

    public static BottomSheetDialogFragment newInstance(Business business){
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_BUSINESS, business);
        ShareBottomSheet shareBottomSheet = new ShareBottomSheet();
        shareBottomSheet.setArguments(args);
        return shareBottomSheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mBusiness = getArguments().getParcelable(EXTRA_BUSINESS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.dialog_share_bottom_sheet,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.btnGroup.setOnClickListener(v-> {
            sendResults(mBusiness);
            dismiss();
        });
        mBinding.btnSocialMedia.setOnClickListener(v -> {
            ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(getActivity());
            intentBuilder.setType("text/plain");
            intentBuilder.setChooserTitle(getString(R.string.share_food_truck));
            intentBuilder.setText(getString(R.string.share_food_truck_content,mBusiness.getUrl()));
            startActivity(intentBuilder.createChooserIntent());
            dismiss();
        });
    }

    void sendResults(Business business){
        if(getTargetFragment() == null) return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_BUSINESS,business);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
    }
}
