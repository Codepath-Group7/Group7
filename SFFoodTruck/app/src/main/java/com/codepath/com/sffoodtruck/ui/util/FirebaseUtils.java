package com.codepath.com.sffoodtruck.ui.util;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by akshaymathur on 10/17/17.
 */

public class FirebaseUtils {

    private static final String BUSINESS_PHOTO_STORAGE = "business_photos";
    private static final String BUSINESS_DATABASE = "businesses";
    private static final String PHOTOS = "photos";
    private static final String REVIEWS = "reviews";

    /* Start of Storage References */
    public static StorageReference getBaseStorageReference(){
        return FirebaseStorage.getInstance().getReference();
    }
    public static StorageReference getBusinessStorageReference(){
        return getBaseStorageReference().child(BUSINESS_PHOTO_STORAGE);
    }
    public static StorageReference getBusinessPhotoReference(String filename){
        if(filename!=null){
            return getBusinessStorageReference().child(filename);
        }
        return null;
    }

    /* End of Storage References */

    /* Start of Database References */

    public static DatabaseReference getBaseDatabaseRef(){
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getBusinessDatabaseRef(String businessId){
        if(businessId!=null){
            return getBaseDatabaseRef().child(BUSINESS_DATABASE).child(businessId);
        }
        return null;
    }

    public static DatabaseReference getBusinessDatabasePhotoRef(String businessId){
        if(businessId!=null){
            return getBusinessDatabaseRef(businessId).child(PHOTOS);
        }
        return null;
    }

    public static DatabaseReference getBusinessDatabaseReviewsRef(String businessId){
        if(businessId!=null){
            return getBusinessDatabaseRef(businessId).child(REVIEWS);
        }
        return null;
    }

    private static DatabaseReference getBaseUserDatabaseRef(){
        return getBaseDatabaseRef().child("user");
    }

    public static DatabaseReference getUserDatabaseRef(String userId){
        return getBaseUserDatabaseRef().child(userId);
    }

    public static DatabaseReference getCurrentUserDatabaseRef(){
        if(getCurrentUserId() == null) return null;
        return getBaseDatabaseRef().child(getCurrentUserId());
    }
    /* End of Database References */

    public static FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static String getCurrentUserId(){
        if(getCurrentUser() != null){
            FirebaseUser user = getCurrentUser();
            return user.getUid();
        }
        return null;
    }
}
