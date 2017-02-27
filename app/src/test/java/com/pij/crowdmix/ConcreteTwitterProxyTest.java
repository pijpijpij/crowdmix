package com.pij.crowdmix;

import android.support.annotation.NonNull;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Pierrejean on 04/10/2015.
 */
public class ConcreteTwitterProxyTest extends TwitterProxyTest {

    @NonNull
    protected ConcreteTwitterProxy createDefaultSut() {
        return new ConcreteTwitterProxy();
    }

}