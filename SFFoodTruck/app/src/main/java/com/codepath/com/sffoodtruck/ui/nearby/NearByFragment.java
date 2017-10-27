package com.codepath.com.sffoodtruck.ui.nearby;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.DBPayloads;
import com.codepath.com.sffoodtruck.data.model.MessagePayload;
import com.codepath.com.sffoodtruck.databinding.FragmentNearByBinding;
import com.codepath.com.sffoodtruck.ui.util.ParcelableUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearByFragment extends Fragment {


    private FragmentNearByBinding mBinding;
    private static final String TAG = NearByFragment.class.getSimpleName();

    private NearByAdapter mAdapter;
    private List<MessagePayload> messagePayloads = new ArrayList<>();
    private FirebaseUser mFirebaseUser;

    private onNearByFragmentListener mNearByFragmentListener;



    public interface onNearByFragmentListener{
        void onSendClick(MessagePayload messagePayload);
    }

    public NearByFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG,"UserID is " + mFirebaseUser.getUid());
        Log.d(TAG,"Got the payloads: " + DBPayloads.getInstance().getMessagePayloads());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mNearByFragmentListener = (onNearByFragmentListener) context;
        }catch (Exception e){
            Log.e(TAG,"Must implement interface onNearbyFragmentListener",e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_near_by,container,false);

        setupChatView();
        mBinding.btnSend.setOnClickListener(view -> {
            Log.d(TAG,"Publishing the message from send button");
            publish(mBinding.etSendMessage.getText().toString());

        });
        return mBinding.getRoot();
    }

    private void setupChatView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        mAdapter = new NearByAdapter(getActivity(),messagePayloads, mFirebaseUser.getUid());
        mBinding.rvGroupChat.setAdapter(mAdapter);
        mBinding.rvGroupChat.setLayoutManager(linearLayoutManager);
    }



    private void publish(String message){
            MessagePayload payload = new MessagePayload();
            if(mFirebaseUser == null || TextUtils.isEmpty(message)) return;
            payload.setUserId(mFirebaseUser.getUid());
            payload.setMessage(message);
            payload.setImageUrl(mFirebaseUser.getPhotoUrl() + "");
            Log.d(TAG,"Sending the payload to HomeActivity" + payload);
            mNearByFragmentListener.onSendClick(payload);
    }


    public void publishSuccessful(boolean result, UUID messageId) {
        Log.d(TAG,"Publish was successful" + result + ", for messageId " + messageId);
    }

    public void loadMessagesFromDB(){
        Log.d(TAG,"loaded messages from database: "
                + DBPayloads.getInstance().getMessagePayloads());
    }






}
