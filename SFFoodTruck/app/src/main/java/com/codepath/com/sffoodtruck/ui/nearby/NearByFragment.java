package com.codepath.com.sffoodtruck.ui.nearby;


import android.content.Context;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.DBPayloads;
import com.codepath.com.sffoodtruck.data.model.MessagePayload;
import com.codepath.com.sffoodtruck.databinding.FragmentNearByBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedList;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearByFragment extends Fragment {


    private FragmentNearByBinding mBinding;
    private static final String TAG = NearByFragment.class.getSimpleName();

    private NearByAdapter mAdapter;
    private LinkedList<MessagePayload> messagePayloads = new LinkedList<>();
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
        mAdapter = new NearByAdapter(getActivity(),messagePayloads ,mFirebaseUser.getUid());
        loadMessagesFromDB();
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
        if(mFirebaseUser == null || TextUtils.isEmpty(message)) return;
        MessagePayload payload = getMessagePayload(message);
        Log.d(TAG,"Sending the payload to HomeActivity" + payload);
        mAdapter.addMessagePayload(payload);
        mBinding.rvGroupChat.smoothScrollToPosition(0);
        mNearByFragmentListener.onSendClick(payload);
        mBinding.etSendMessage.setText("");
    }

    public MessagePayload getMessagePayload(String message){
        MessagePayload payload = new MessagePayload();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        payload.setUserId(mFirebaseUser.getUid());
        payload.setMessage(message);
        payload.setImageUrl(mFirebaseUser.getPhotoUrl() + "");
        payload.setUserName(mFirebaseUser.getDisplayName());
        payload.setUserEmail(mFirebaseUser.getEmail());
        return payload;
    }

    public void publishSuccessful(boolean result, UUID messageId) {
        Log.d(TAG,"Publish was successful" + result + ", for messageId " + messageId);

    }

    public void loadMessagesFromDB(){
        Log.d(TAG,"loaded messages from database: "
                + DBPayloads.getInstance().getMessagePayloads());
        mAdapter.clearAll();
        mAdapter.addAllMessagePayloads(DBPayloads.getInstance().getMessagePayloads());
    }

    public void addMessagePayload(MessagePayload payload){
        Log.d(TAG,"Calling MessagePayload" + payload);
        mAdapter.addMessagePayload(payload);
        mBinding.rvGroupChat.smoothScrollToPosition(0);
    }






}
