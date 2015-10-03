package com.pij.android;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author pierrejean
 */
public abstract class Utils {

    /**
     * To allow extension.
     */
    private Utils() {
        // Does nothing.
    }

    /**
     * Casts the object to a target class, it makes the ClassCastException more friendly.
     * @param <T>        Makes this generic-friendly
     * @param toBeCasted object to cast
     * @param target     class to cast to
     * @throws ClassCastException if the toBeCasted cannot be casted
     */
    @Nullable
    public static <T> T cast(@Nullable final Object toBeCasted, @NonNull Class<T> target) {

        try {
            @SuppressWarnings("unchecked") final T result = (T)toBeCasted;
            return result;

        } catch (ClassCastException e) {
            final String message = toBeCasted + " must implement " + target.getName() + "(Original message was " + e.getMessage() + ")";
            Log.e(tag(Utils.class), message, e);
            throw new ClassCastException(message);
        }
    }

    /**
     * The only point of this is to eliminate the deprecation warning on {@link org.apache.commons.lang3.ObjectUtils#toString(Object,
     * String)}.
     * @see {@link org.apache.commons.lang3.ObjectUtils#toString(Object, String)}
     */
    @SuppressWarnings("deprecation")
    @NonNull
    public static String toString(@NonNull Object toStringify, @Nullable final String defaultString) {
        return org.apache.commons.lang3.ObjectUtils.toString(toStringify, defaultString);
    }

    /**
     * Utility method that provides a simple tag for a class. The tag is the simple name of the argument.
     * @param tagged may be <code>null</code>.
     * @return simple name of the class <code>tagged</code>
     */
    @NonNull
    public static String tag(Class<?> tagged) {
        return (tagged == null) ? "null Tag!" : tagged.getSimpleName();
    }

    /**
     * Utility method that provides a simple tag for an object. The tag is the simple class name of the argument
     * <code>tagged</code>.
     * @param tagged may be <code>null</code>.
     * @return simple class name of the argument <code>tagged</code>.
     */
    @NonNull
    public static String tag(@Nullable final Object tagged) {
        return (tagged == null) ? "null Tag!" : tag(tagged.getClass());
    }

}
