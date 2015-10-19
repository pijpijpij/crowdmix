package com.pij.crowdmix.login;

import android.support.annotation.NonNull;

import com.pij.crowdmix.TwitterProxy;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import static com.pij.crowdmix.login.LoginView.NOOP_VIEW;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * @author Pierrejean on 19/10/2015.
 */
public class LoginPresenter extends Callback<TwitterSession> {

    private final TwitterProxy tweeter;
    private LoginView view;

    public LoginPresenter(@NonNull TwitterProxy tweeter) {
        this.view = NOOP_VIEW;
        this.tweeter = notNull(tweeter);
    }

    public void setSession(TwitterSession newValue) {
        tweeter.setSession(newValue);
        view.setLoggedIn(tweeter.isConnected());
    }

    public void setView(LoginView newValue) {
        view = defaultIfNull(newValue, NOOP_VIEW);
        view.setLoggedIn(tweeter.isConnected());
    }

    @Override
    public void success(Result<TwitterSession> result) {
        setSession(result.data);
    }

    @Override
    public void failure(TwitterException e) {
        view.setError(e.getMessage());
        view.setLoggedIn(tweeter.isConnected());
    }

}
