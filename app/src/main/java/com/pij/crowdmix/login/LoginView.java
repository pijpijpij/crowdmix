package com.pij.crowdmix.login;

/**
 * @author Pierrejean on 19/10/2015.
 */
public interface LoginView {

    void setLoggedIn(boolean newValue);

    void setError(String cause);

    /**
     * Utility class that does nothing, whatever the call.
     */
    class Noop implements LoginView {

        @Override
        public void setLoggedIn(boolean newValue) { }

        @Override
        public void setError(String cause) { }
    }
}
