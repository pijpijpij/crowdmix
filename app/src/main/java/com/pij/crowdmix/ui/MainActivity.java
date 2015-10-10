package com.pij.crowdmix.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.pij.android.DelegatingCallback;
import com.pij.crowdmix.R;
import com.pij.crowdmix.TwitterProxy;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TweetListFragment.Events, TweetFragment.Events {

    @Bind(R.id.login)
    TwitterLoginButton login;

    @Bind(R.id.login_panel)
    View loginPanel;

    @Bind(R.id.tweet)
    View tweetPanel;

    private TwitterProxy twitterProxy = new TwitterProxy();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterProxy.initialise(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        login.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                setSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(MainActivity.this, "Failed login", Toast.LENGTH_LONG).show();
            }
        });

        setSession(Twitter.getSessionManager().getActiveSession());
    }

    @Override
    protected void onDestroy() {
        twitterProxy = null;
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    /**
     * Pass callback to login button.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        login.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Beefy I know, but re-creating the fragment every time simplifies the API between activity and fragment.
     */
    private void updateListPanel() {
        if (isLoggedIn()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.tweet_list,
                                                                   TweetListFragment.newInstance()).commit();
        }
    }

    /**
     * Hides the panel if there is no session.
     */
    private void updateTweetPanel() {
        int tweetPanelVisibility = isLoggedIn() ? View.VISIBLE : View.GONE;
        tweetPanel.setVisibility(tweetPanelVisibility);
    }

    /**
     * Shows the panel if there is no session.
     */
    private void updateLoginPanel() {
        int loginPanelVisibility = isLoggedIn() ? View.GONE : View.VISIBLE;
        loginPanel.setVisibility(loginPanelVisibility);
    }

    private void setSession(@Nullable TwitterSession newSession) {
        StatusesService service = TwitterProxy.createLoggedInClient(newSession);
        twitterProxy.setService(service);
        updateLoginPanel();
        updateListPanel();
        updateTweetPanel();
    }

    @Override
    public boolean isLoggedIn() {
        return twitterProxy.isConnected();
    }

    @Override
    public void loadTweets(Callback<List<Tweet>> callback) {
        twitterProxy.load(callback);

    }

    @Override
    public void tweet(String message, final Callback<Tweet> callback) {
        twitterProxy.sendUpdate(message, new DelegatingCallback<Tweet>(callback) {
            @Override
            public void success(Result<Tweet> result) {
                super.success(result);
                updateListPanel();
            }
        });
    }
}
