package com.codepath.com.sffoodtruck.data.remote;

/**
 * Created by robl2e on 10/13/17.
 */

public interface ResponseHandler<DATA> {
    void onComplete(DATA data);
    void onFailed(String message, Exception exception);
}
