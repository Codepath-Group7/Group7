package com.codepath.com.sffoodtruck.infrastructure.service;

import com.google.firebase.iid.FirebaseInstanceIdService;

public class AppInstanceIdListenerService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify of changes
        FirebaseRegistrationIntentService.start(this, true);
    }
}