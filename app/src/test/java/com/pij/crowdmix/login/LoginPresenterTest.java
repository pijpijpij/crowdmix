package com.pij.crowdmix.login;

import android.support.annotation.NonNull;

import com.pij.crowdmix.TwitterProxy;
import com.twitter.sdk.android.core.TwitterSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Pierrejean on 19/10/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    @Mock
    private TwitterProxy mockProxy;
    @Mock
    private LoginView mockView;
    @Mock
    private TwitterSession mockSession;

    @NonNull
    private LoginPresenter createDefaultSut() {
        LoginPresenter tested = new LoginPresenter(mockProxy);
        tested.setView(mockView);
        reset(mockView);
        return tested;
    }


    @Test(expected = NullPointerException.class)
    public void test_NullProxy_Throws() {
        //noinspection ConstantConditions
        new LoginPresenter(null);
    }

    @Test
    public void test_setView_UpdatesLoginStateOnView() {
        LoginPresenter tested = new LoginPresenter(mockProxy);
        when(mockProxy.isConnected()).thenReturn(true);

        tested.setView(mockView);

        verify(mockView).setLoggedIn(true);
    }

    @Test
    public void test_setSession_setsSessionOnProxy() {
        LoginPresenter tested = createDefaultSut();

        tested.setSession(mockSession);

        verify(mockProxy).setSession(mockSession);
    }

    @Test
    public void test_setSession_UpdatesLoginStateOnView() {
        LoginPresenter tested = createDefaultSut();
        when(mockProxy.isConnected()).thenReturn(true);

        tested.setSession(mockSession);

        verify(mockView).setLoggedIn(true);
    }

    @Test
    public void test_setSessionNull_setsSessionNullOnProxy() {
        LoginPresenter tested = createDefaultSut();

        tested.setSession(null);

        verify(mockProxy).setSession(null);
    }

}