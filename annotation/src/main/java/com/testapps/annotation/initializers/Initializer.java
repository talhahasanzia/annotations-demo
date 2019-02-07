package com.testapps.annotation.initializers;

import com.testapps.annotation.Init;

import java.lang.reflect.Field;

/**
 * This class initializes variables marked with {@link com.testapps.annotation.Init} annotation.
 * This class uses reflection to find Init annotation on fields in a class and initialize them by calling their default constructor.
 */
public class Initializer {

    private Initializer() {
        throw new SecurityException("Cannot create instance of this class");
    }

    public static void init(Object parent) {

        try {

            // get all fields
            Field[] fields = parent.getClass().getDeclaredFields();

            // process fields
            processFields(fields, parent);


        } catch (Exception ex) {
            ex.printStackTrace();

        }


    }

    // process fields 1 by 1, call their default constructor and set that initialized instance in that field
    private static void processFields(Field[] fields, Object parent) throws IllegalAccessException, InstantiationException {

        // (pick all fields)
        for (Field field : fields) {

            // set fields accessible
            field.setAccessible(true);

            // filter only "Init" annotated fields
            if (field.isAnnotationPresent(Init.class)) {

                // get class type
                Class<?> objectType = field.getType();

                // call default constructor
                Object instance = objectType.newInstance();

                // set initialized instance
                field.set(parent, instance);

            }

        }
    }
}
