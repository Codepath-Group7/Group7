package com.codepath.com.sffoodtruck.data.local;



import android.util.Log;

import com.codepath.com.sffoodtruck.data.model.MessagePayload;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by saip92 on 10/27/2017.
 */

public class DBPayloads {

    private LinkedList<MessagePayload> mMessagePayloads = new LinkedList<>();
    private HashSet<UUID> uniquePayloads = new HashSet<>();

    private static final DBPayloads ourInstance = new DBPayloads();

    public static DBPayloads getInstance() {
        return ourInstance;
    }

    private DBPayloads() {
    }


    public boolean storeMessagePayload(MessagePayload payload){
        if(!uniquePayloads.contains(payload.getUUID())){
            uniquePayloads.add(payload.getUUID());
            mMessagePayloads.addLast(payload);
            return false;
        }
        return true;
    }

    public List<MessagePayload> getMessagePayloads(){
        return mMessagePayloads;
    }

    public boolean isDuplicate(MessagePayload payload){
        Log.d("Actual Payload:",uniquePayloads + " " + payload);
        return uniquePayloads.contains(payload.getUUID());
    }

}
