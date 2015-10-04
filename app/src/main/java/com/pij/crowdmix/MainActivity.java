package com.pij.crowdmix;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TweetListFragment.Events {

    @Bind(R.id.login)
    TwitterLoginButton login;

    @Bind(R.id.login_panel)
    View loginPanel;

    private TweetLoader tweetLoader;
    private TwitterSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweetLoader = new TweetLoader(this);
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

        setSession(savedInstanceState == null ? null : Twitter.getSessionManager().getActiveSession());
    }

    @Override
    protected void onDestroy() {
        session = null;
        tweetLoader = null;
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
        getSupportFragmentManager().beginTransaction().replace(R.id.tweet_list,
                                                               TweetListFragment.newInstance()).commit();
    }

    private void updateLoginPanel() {
        int loginPanelVisibility = (session == null) ? View.VISIBLE : View.GONE;
        loginPanel.setVisibility(loginPanelVisibility);
    }

    @Override
    public TwitterSession getSession() {
        return session;
    }

    private void setSession(@Nullable TwitterSession newSession) {
        session = newSession;
        tweetLoader.setSession(session);
        updateLoginPanel();
        updateListPanel();
        invalidateOptionsMenu();
    }

    @Override
    public void loadTweets(Callback<List<Tweet>> callback) {
        tweetLoader.load(callback);

    }
}
