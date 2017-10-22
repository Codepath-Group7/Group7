package com.codepath.com.sffoodtruck.ui.nearby;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearByFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private FragmentNearByBinding mBinding;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = NearByFragment.class.getSimpleName();

    private Message mActiveMessage;
    private MessageListener mMessageListener;
    private NearByAdapter mAdapter;
    private List<MessagePayload> messagePayloads = new ArrayList<>();
    private String userId;

    public NearByFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG,"UserID is " + userId);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .enableAutoManage(getActivity(),this)
                .build();

        mMessageListener = new MessageListener(){
            @Override
            public void onFound(Message message) {
                byte[] bytes = message.getContent();
                MessagePayload payload = ParcelableUtil.unmarshall(bytes,MessagePayload.CREATOR);
                messagePayloads.add(0, payload);
                mAdapter.notifyDataSetChanged();
                mBinding.rvGroupChat.scrollToPosition(0);
                //String messageAsString = new String(message.getContent());
                Log.d(TAG,"Found message: " + payload);
            }

            @Override
            public void onLost(Message message) {
                String messageAsString = new String(message.getContent());
                Log.d(TAG, "Lost sight of message: " + messageAsString);
            }
        };
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
        mAdapter = new NearByAdapter(getActivity(),messagePayloads, userId);
        mBinding.rvGroupChat.setAdapter(mAdapter);
        mBinding.rvGroupChat.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG,"It is connected");
        subscribe();
    }

    @Override
    public void onStop() {
        unpublish();
        unsubscribe();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"On Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"On Connection failed");
    }

    private void publish(String message){
        if(mGoogleApiClient.isConnected()){
            MessagePayload payload = new MessagePayload();
            Log.d(TAG,"Current session user id is : "
                    + userId);

            payload.setUserId(userId);
            payload.setMessage(message);

            mActiveMessage = new Message(ParcelableUtil.marshall(payload));
            //mActiveMessage = new Message(message.getBytes());
            PublishOptions options = new PublishOptions.Builder().setCallback(new PublishCallback(){
                @Override
                public void onExpired() {
                    Log.d(TAG,"Message has expired " + message);
                }
            }).build();
            Nearby.Messages.publish(mGoogleApiClient,mActiveMessage,options)
                    .setResultCallback(status ->{
                        messagePayloads.add(0, payload);
                        mAdapter.notifyDataSetChanged();
                        mBinding.rvGroupChat.scrollToPosition(0);
                        mBinding.etSendMessage.setText(null);

                        Log.d(TAG,"Status of publishing message: "
                                + message + ", status:  "
                                + status.getStatusMessage() + " "
                                + status.getStatus()  + " "
                                + status.getStatusCode());
                    });
        }
    }

    private void unpublish(){
        Log.i(TAG,"Unpublishing");
        if(mActiveMessage != null){
            Nearby.Messages.unpublish(mGoogleApiClient,mActiveMessage);
            mActiveMessage = null;
        }
    }

    private void subscribe(){
        Log.i(TAG,"Subscribing");
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setCallback(new SubscribeCallback(){
                    @Override
                    public void onExpired() {
                        Log.d(TAG,"subscription has expired");
                    }
                }).build();
        Nearby.Messages.subscribe(mGoogleApiClient,mMessageListener,options)
                .setResultCallback(status -> Log.d(TAG,"Status of subscription: "
                        + status.getStatusMessage() + " "
                        + status.getStatus()  + " "
                        + status.getStatusCode() ));
    }

    private void unsubscribe(){
        Log.i(TAG,"Unsubscribing");
        Nearby.Messages.unsubscribe(mGoogleApiClient,mMessageListener);
    }
}
