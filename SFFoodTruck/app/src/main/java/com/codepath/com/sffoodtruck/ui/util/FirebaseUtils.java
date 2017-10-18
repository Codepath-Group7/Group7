package com.codepath.com.sffoodtruck.ui.util;

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

    /* Start of Storage References */
    public static StorageReference getBaseStorageReference(){
        return FirebaseStorage.getInstance().getReference();
    }

    public static StorageReference getBusinessPhotoReference(String filename){
        if(filename!=null){
            return getBaseStorageReference().child(BUSINESS_PHOTO_STORAGE).child(filename);
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
    /* End of Database References */
}
