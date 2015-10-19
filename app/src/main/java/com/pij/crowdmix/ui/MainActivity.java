package com.pij.crowdmix.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.pij.crowdmix.R;
import com.pij.crowdmix.TwitterProxy;
import com.pij.crowdmix.list.TweetListFragment;
import com.pij.crowdmix.list.TweetListPresenter;
import com.pij.crowdmix.list.TweetListPresenterProvider;
import com.pij.crowdmix.login.LoginPresenter;
import com.pij.crowdmix.login.LoginView;
import com.pij.crowdmix.update.TweetUpdatePresenter;
import com.pij.crowdmix.update.TweetUpdatePresenterProvider;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements TweetListPresenterProvider, TweetUpdatePresenterProvider {

    private final TwitterProxy twitterProxy = new TwitterProxy();
    private final TweetListPresenter tweeterListPresenter = new TweetListPresenter(twitterProxy);
    private final TweetUpdatePresenter tweeterUpdatePresenter = new TweetUpdatePresenter(twitterProxy);
    private final LoginPresenter loginPresenter = new LoginPresenter(twitterProxy);
    @Bind(R.id.login)
    TwitterLoginButton login;
    @Bind(R.id.login_panel)
    View loginPanel;
    @Bind(R.id.tweet)
    View tweetPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterProxy.initialise(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        login.setCallback(loginPresenter);
        loginPresenter.setView(new LoginViewDelegate());
        loginPresenter.setSession(Twitter.getSessionManager().getActiveSession());
    }

    @Override
    protected void onDestroy() {
        loginPresenter.setView(null);
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
    private void updateListPanel(boolean loggedIn) {
        if (loggedIn) {
            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.tweet_list, TweetListFragment.newInstance())
                                       .commit();
        }
    }

    /**
     * Hides the panel if there is no session.
     */
    private void updateTweetPanel(boolean loggedIn) {
        int tweetPanelVisibility = loggedIn ? View.VISIBLE : View.GONE;
        tweetPanel.setVisibility(tweetPanelVisibility);
    }

    /**
     * Shows the panel if there is no session.
     */
    private void updateLoginPanel(boolean loggedIn) {
        int loginPanelVisibility = loggedIn ? View.GONE : View.VISIBLE;
        loginPanel.setVisibility(loginPanelVisibility);
    }

    @Override
    public TweetListPresenter getTweetListPresenter() {
        return tweeterListPresenter;
    }

    @Override
    public TweetUpdatePresenter getTweetUpdatePresenter() {
        return tweeterUpdatePresenter;
    }

    private class LoginViewDelegate implements LoginView {

        @Override
        public void setLoggedIn(boolean newValue) {
            updateLoginPanel(newValue);
            updateListPanel(newValue);
            updateTweetPanel(newValue);
        }

        @Override
        public void setError(String cause) {
            Toast.makeText(MainActivity.this, "Failed login", Toast.LENGTH_LONG).show();
        }
    }

}
