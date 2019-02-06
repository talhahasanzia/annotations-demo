package com.testapps.annotation.initializers;

import com.sun.istack.internal.NotNull;

public class Initializer {

    private Initializer() {
        throw new SecurityException("Cannot create instance of this class");
    }

    public static void init(@NotNull Object target) {

    }
}
