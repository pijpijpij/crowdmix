package com.pij.crowdmix.update;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pij.crowdmix.R;

import org.apache.commons.lang3.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.pij.android.Utils.cast;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.stripToNull;

public class TweetUpdateFragment extends Fragment {

    @Bind(R.id.message)
    TextView messageView;

    @Bind(R.id.button)
    View button;

    private String message;

    private TweetUpdatePresenter.Provider provider;
    private TweetUpdatePresenter presenter;

    public TweetUpdateFragment() {
    }

    public static TweetUpdateFragment newInstance() {
        return new TweetUpdateFragment();
    }

    /**
     * Get access to the business layer.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        provider = cast(context, TweetUpdatePresenter.Provider.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tweet, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        presenter = provider.getTweetUpdatePresenter();
        presenter.setView(new ViewDelegate());
        setMessage(null);
    }

    @Override
    public void onDestroyView() {
        presenter.setView(null);
        presenter = null;
        super.onDestroyView();
    }

    /**
     * Reset the active events interface to null.
     */
    @Override
    public void onDetach() {
        provider = null;
        super.onDetach();
    }

    /**
     * Spaces or null are stored as null.
     */
    @OnTextChanged(R.id.message)
    void setMessage(CharSequence newValue) {
        message = stripToNull(defaultIfEmpty(newValue, StringUtils.EMPTY).toString());
        button.setEnabled(isMessageValid());
    }

    private boolean isMessageValid() {
        return message != null;
    }

    @OnClick(R.id.button)
    void postTweet() {
        presenter.tweet(message);
    }

    private class ViewDelegate implements TweetUpdateView {

        @Override
        public void setInProgress(boolean newValue) {
            if (newValue) button.setEnabled(false);
            else button.setEnabled(isMessageValid());
        }

        @Override
        public void setLoggedIn(boolean newValue) {
            button.setEnabled(newValue && isMessageValid());
        }

        @Override
        public void setUpdate(String message) {
            messageView.setText(message);
        }

        @Override
        public void setError(String cause) {
            Toast.makeText(getActivity(), "Failed to Tweet: " + cause, Toast.LENGTH_LONG).show();
        }
    }
}
