package com.pij.crowdmix.update;

/**
 * @author Pierrejean on 18/10/2015.
 */
public interface TweetUpdateView {

    void setInProgress(boolean newValue);

    void setLoggedIn(boolean newValue);

    void setUpdate(String message);

    void setError(String cause);

    /**
     * Utility class that does nothing, whatever the call.
     * @author Pierrejean on 18/10/2015.
     */
    class Noop implements TweetUpdateView {

        @Override
        public void setInProgress(boolean ignored) { }

        @Override
        public void setLoggedIn(boolean ignored) { }

        @Override
        public void setUpdate(String message) { }

        @Override
        public void setError(String ignored) { }
    }
}
