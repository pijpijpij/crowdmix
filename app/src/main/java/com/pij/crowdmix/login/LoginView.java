package com.pij.crowdmix.login;

import hugo.weaving.DebugLog;

/**
 * @author Pierrejean on 19/10/2015.
 */
public interface LoginView {

    LoginView NOOP_VIEW = new Noop();

    void setLoggedIn(boolean newValue);

    void setError(String cause);

    /**
     * Utility class that does nothing, whatever the call.
     */
    class Noop implements LoginView {

        @Override
        @DebugLog
        public void setLoggedIn(boolean newValue) { }

        @Override
        @DebugLog
        public void setError(String cause) { }
    }
}
