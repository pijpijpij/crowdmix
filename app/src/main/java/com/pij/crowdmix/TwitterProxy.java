package com.pij.crowdmix;

import android.support.annotation.Nullable;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

/**
 * @author Pierrejean on 19/10/2015.
 */
public interface TwitterProxy {

    void setSession(@Nullable TwitterSession newValue);

    void setService(@Nullable StatusesService service);

    boolean isConnected();

    void load(Callback<List<Tweet>> sink);

    void sendUpdate(String message, Callback<Tweet> callback);
}
